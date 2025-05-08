package Hackathon.domain;

import Hackathon.repository.EmpresaRepository;
import Hackathon.dto.EmpresaDTO;
import Hackathon.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Empresa createCompany(EmpresaDTO empresaDto) {
        // Convertir el DTO a una entidad Empresa
        Empresa empresa = new Empresa();
        empresa.setNombre(empresaDto.getNombre());
        empresa.setRuc(empresaDto.getRuc());
        empresa.setFechaAfiliacion(empresaDto.getFechaAfiliacion());
        empresa.setEstadoActivo(empresaDto.getEstadoActivo());

        // Manejar el administrador (si está presente en el DTO)
        if (empresaDto.getAdministrador() != null) {
            Usuario administrador = new Usuario();
            administrador.setNombre(empresaDto.getAdministrador().getNombre());
            administrador.setEmail(empresaDto.getAdministrador().getEmail());
            administrador.setPassword(passwordEncoder.encode(empresaDto.getAdministrador().getPassword()));
            administrador.setRol(Rol.ROLE_COMPANY_ADMIN);

            // Asociar al administrador con la empresa
            administrador.setEmpresa(empresa);  // Relación bidireccional: empresa -> administrador
            empresa.setAdministrador(administrador); // Relación bidireccional: administrador -> empresa
        }
        // Guardar la empresa (y de manera automática el administrador gracias a CascadeType.ALL)
        return empresaRepository.save(empresa);
    }



    public List<EmpresaDTO> listAllCompanies() {
        // Obtener todas las entidades Empresa desde el repositorio
        List<Empresa> empresas = empresaRepository.findAll();

        // Convertir las entidades a DTOs
        return empresas.stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<EmpresaDTO> getCompanyById(Long id) {
        return empresaRepository.findById(id)
                .map(this::convertToDto);
    }

    public Empresa updateCompany(Long id, EmpresaDTO empresaDto) {
        // 1. Buscar la empresa por ID
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        //Se comprueba si elnuevo ruc es nulo o ya existente
        if (empresaDto.getRuc() != null && !empresaDto.getRuc().equals(empresa.getRuc())) {
            boolean rucExists = empresaRepository.existsByRuc(empresaDto.getRuc());
            if (rucExists) {
                throw new IllegalArgumentException("El RUC ya está en uso");
            }
            empresa.setRuc(empresaDto.getRuc());
        }

        // 2. Actualizar los atributos
        if (empresaDto.getNombre() != null) {
            empresa.setNombre(empresaDto.getNombre());
        }
        if (empresaDto.getRuc() != null) {
            empresa.setRuc(empresaDto.getRuc());
        }
        if (empresaDto.getFechaAfiliacion() != null) {
            empresa.setFechaAfiliacion(empresaDto.getFechaAfiliacion());
        }
        if (empresaDto.getEstadoActivo() != null) {
            empresa.setEstadoActivo(empresaDto.getEstadoActivo());
        }

        // 3. Guardar los cambios
        return empresaRepository.save(empresa);
    }

    public Empresa changeCompanyStatus(Long id, Boolean isActive) {
        // Buscar la empresa por ID
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Actualizar el estado activo de la empresa
        empresa.setEstadoActivo(isActive);

        // Guardar los cambios en la base de datos
        return empresaRepository.save(empresa);
    }

    public String getCompanyConsumption(Long id) {
        // 1. Buscar la empresa por ID
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // 2. Obtener los usuarios de la empresa
        List<Usuario> usuarios = empresa.getUsuarios();
        if (usuarios.isEmpty()) {
            return "Esta empresa no tiene usuarios registrados.";
        }

        // 3. Estructura para el consumo por modelo
        Map<String, Integer> consumoTotalPorModelo = new HashMap<>();
        Map<String, Map<String, Integer>> consumoPorUsuario = new HashMap<>();

        // 4. Recorrer cada usuario
        for (Usuario usuario : usuarios) {
            // Inicializar el consumo del usuario
            Map<String, Integer> consumoUsuarioPorModelo = new HashMap<>();
            consumoPorUsuario.put(usuario.getNombre(), consumoUsuarioPorModelo);


            // 5. Recorrer cada solicitud del usuario
            for (Solicitud solicitud : usuario.getSolicitudes()) {
                Modelo modelo = solicitud.getModelo();
                Integer tokensConsumidos = solicitud.getTokensConsumidos(); // Ensure Solicitud class has this method

                // Actualizar el total del usuario por modelo
                consumoUsuarioPorModelo.put(String.valueOf(modelo), consumoUsuarioPorModelo.getOrDefault(modelo, 0) + tokensConsumidos);

                // Actualizar el consumo total por modelo (agregado entre usuarios)
                consumoTotalPorModelo.put(String.valueOf(modelo), consumoTotalPorModelo.getOrDefault(modelo, 0) + tokensConsumidos);
            }
        }

        // 6. Formatear el reporte
        return generarReporteConsumo(consumoPorUsuario, consumoTotalPorModelo);
    }

    // Método para generar y estructurar el reporte final
    private String generarReporteConsumo(Map<String, Map<String, Integer>> consumoPorUsuario, Map<String, Integer> consumoTotalPorModelo) {
        StringBuilder reporte = new StringBuilder("Reporte de Consumo por Modelo y Usuario:\n\n");

        // Consumo por usuario
        reporte.append("Consumo por Usuario:\n");
        for (Map.Entry<String, Map<String, Integer>> entradaUsuario : consumoPorUsuario.entrySet()) {
            String usuario = entradaUsuario.getKey();
            Map<String, Integer> consumoPorModelo = entradaUsuario.getValue();
            reporte.append("- ").append(usuario).append(":\n");

            for (Map.Entry<String, Integer> entradaModelo : consumoPorModelo.entrySet()) {
                reporte.append("    Modelo: ").append(entradaModelo.getKey())
                        .append(", Tokens Consumidos: ").append(entradaModelo.getValue()).append("\n");
            }
        }

        // Consumo total por modelo
        reporte.append("\nConsumo Total por Modelo:\n");
        for (Map.Entry<String, Integer> entradaModelo : consumoTotalPorModelo.entrySet()) {
            reporte.append("- Modelo: ").append(entradaModelo.getKey())
                    .append(", Tokens Consumidos: ").append(entradaModelo.getValue()).append("\n");
        }

        return reporte.toString();
    }

        private EmpresaDTO convertToDto(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setRuc(empresa.getRuc());
        dto.setFechaAfiliacion(empresa.getFechaAfiliacion());
        dto.setEstadoActivo(empresa.getEstadoActivo());

        // Incluir el administrador si está presente
        if (empresa.getAdministrador() != null) {
            EmpresaDTO.AdministradorDTO adminDto = new EmpresaDTO.AdministradorDTO();
            adminDto.setId(empresa.getAdministrador().getId());
            adminDto.setNombre(empresa.getAdministrador().getNombre());
            adminDto.setEmail(empresa.getAdministrador().getEmail());
            dto.setAdministrador(adminDto);
        }
        return dto;
    }
}
