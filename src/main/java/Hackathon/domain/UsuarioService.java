package Hackathon.domain;

import Hackathon.dto.RestriccionDTO;
import Hackathon.dto.UsuarioDTO;
import Hackathon.repository.EmpresaRepository;
import Hackathon.repository.SolicitudRepository;
import Hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SolicitudRepository solicitudRepository;

    /**
     * Crear un nuevo usuario asociado a una empresa.
     */
    public Usuario createUser(UsuarioDTO dto) {
        // Buscar la empresa asociada al ID proporcionado
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa con ID " + dto.getEmpresaId() + " no encontrada."));

        // Validar si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + dto.getEmail());
        }

        // Crear una instancia del usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setRol(Rol.ROLE_USER);
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setEmail(dto.getEmail());
        // Encriptar la contraseña antes de guardarla
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setEmpresa(empresa); // Asociar el usuario a la empresa

        // Guardar el usuario en el repositorio
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Agregar el usuario al listado de usuarios de la empresa
        empresa.getUsuarios().add(usuarioGuardado);
        empresaRepository.save(empresa); // Persistir cambios en la empresa (si es necesario)

        return usuarioGuardado;
    }

    /**
     * Listar todos los usuarios de una empresa por su ID.
     */
    public List<Usuario> listUsers(Long empresaId) {
        // Consultar usuarios por empresa
        return usuarioRepository.findByEmpresaId(empresaId);
    }

    /**
     * Obtener un usuario por su ID, verificando que pertenezca a la empresa del usuario autenticado.
     */
    public Usuario getUserByIdAndCompany(Long userId, Long companyId) {
        return usuarioRepository.findById(userId)
                .filter(usuario -> usuario.getEmpresa().getId().equals(companyId)) // Validar que pertenece a la misma empresa
                .orElseThrow(() -> new IllegalArgumentException("El usuario no pertenece a esta empresa o no existe."));
    }

    /**
     * Actualizar un usuario por su ID, verificando que pertenezca a la empresa del usuario autenticado.
     */
    public Usuario updateUser(Long userId, UsuarioDTO dto, Long authenticatedCompanyId) {
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Validar que pertenece a la misma empresa
        if (!usuario.getEmpresa().getId().equals(authenticatedCompanyId)) {
            throw new IllegalArgumentException("El usuario no pertenece a esta empresa.");
        }

        // Actualizar los campos permitidos
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());

        // Si se envía una nueva contraseña, encriptarla antes de actualizar
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(dto.getPassword()); // Importante: agregar encriptación si es necesario (PasswordEncoder)
        }

        // Guardar las actualizaciones en la base de datos
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza las restricciones de un usuario.
     */
    public Usuario assignLimits(Long userId, RestriccionDTO dto, Long authenticatedCompanyId) {
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Validar que pertenece a la misma empresa del usuario autenticado
        if (!usuario.getEmpresa().getId().equals(authenticatedCompanyId)) {
            throw new IllegalArgumentException("El usuario no pertenece a esta empresa.");
        }

        // Asignar nuevas restricciones
        if (dto.getLimiteSolicitudes() != null) {
            usuario.setLimiteSolicitudes(dto.getLimiteSolicitudes());
        }
        if (dto.getLimiteTokensGlobal() != null) {
            usuario.setLimiteTokensGlobal(dto.getLimiteTokensGlobal());
        }
        if (dto.getVentanaTiempo() != null) {
            usuario.setVentanaTiempo(dto.getVentanaTiempo());
        }

        // Guardar y retornar el usuario actualizado
        return usuarioRepository.save(usuario);
    }

    /**
     * Obtener el consumo total de tokens para un usuario.
     */
    public Integer getTotalTokensConsumedByUser(Long userId, Long authenticatedCompanyId) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Validar que pertenece a la misma empresa
        if (!usuario.getEmpresa().getId().equals(authenticatedCompanyId)) {
            throw new IllegalArgumentException("El usuario no pertenece a esta empresa.");
        }

        // Obtener la suma total de tokens consumidos
        Integer totalTokens = solicitudRepository.sumTokensByUsuarioId(userId);

        // Retornar la suma total (manejar si es null)
        return totalTokens != null ? totalTokens : 0;
    }

    public Optional<Usuario> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }
}
