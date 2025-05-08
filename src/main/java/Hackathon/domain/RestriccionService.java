package Hackathon.domain;


import Hackathon.dto.RestriccionDTO;
import Hackathon.repository.EmpresaRepository;
import Hackathon.repository.ModeloRepository;
import Hackathon.repository.RestriccionEmpresaModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestriccionService {

    @Autowired
    private RestriccionEmpresaModeloRepository restriccionRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    /**
     * Crear una nueva restricción para una empresa y un modelo.
     */
    public RestriccionEmpresaModelo createRestriction(RestriccionDTO dto) {
        // Validar empresa
        Optional<Empresa> empresaOpt = empresaRepository.findById(dto.getEmpresaId());
        if (empresaOpt.isEmpty()) {
            throw new IllegalArgumentException("La empresa con ID " + dto.getEmpresaId() + " no existe.");
        }

        // Validar modelo
        Optional<Modelo> modeloOpt = modeloRepository.findById(dto.getModeloId());
        if (modeloOpt.isEmpty()) {
            throw new IllegalArgumentException("El modelo con ID " + dto.getModeloId() + " no existe.");
        }

        Empresa empresa = empresaOpt.get();
        Modelo modelo = modeloOpt.get();

        // Verificar si ya existe una restricción para esta empresa y modelo
        Optional<RestriccionEmpresaModelo> restriccionExistente =
                restriccionRepository.findByEmpresaAndModelo(empresa, modelo);

        if (restriccionExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una restricción para esta empresa y modelo.");
        }

        // Crear y guardar la nueva restricción
        RestriccionEmpresaModelo restriccion = new RestriccionEmpresaModelo(
                empresa,
                modelo,
                dto.getLimiteSolicitudes(),
                dto.getLimiteTokensGlobal(),
                dto.getVentanaTiempo()
        );

        return restriccionRepository.save(restriccion);
    }


    /**
     * Listar todas las restricciones de una empresa
     */
    public List<RestriccionEmpresaModelo> listRestrictions(Long empresaId) {
        // Validar que la empresa existe
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("La empresa con ID " + empresaId + " no existe."));

        // Obtener las restricciones asociadas con la empresa
        return restriccionRepository.findByEmpresa(empresa);
    }

    /**
     * Actualiza los valores de una restricción existente dado su ID y nuevos datos.
     */

    public RestriccionEmpresaModelo updateRestriction(Long id, RestriccionDTO dto) {
        // Buscar la restricción por ID
        RestriccionEmpresaModelo restriccion = restriccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la restricción con el ID: " + id));

        // Actualizar los valores si son proporcionados (null-safe)
        if (dto.getLimiteSolicitudes() != null) {
            restriccion.setLimiteSolicitudes(dto.getLimiteSolicitudes());
        }
        if (dto.getLimiteTokensGlobal() != null) {
            restriccion.setLimiteTokensGlobal(dto.getLimiteTokensGlobal());
        }
        if (dto.getVentanaTiempo() != null) {
            restriccion.setVentanaTiempo(dto.getVentanaTiempo());
        }

        // Guardar y devolver la restricción actualizada
        return restriccionRepository.save(restriccion);
    }

    /**
     * Eliminar una restricción dado su ID.
     */
    public void deleteRestriction(Long id) {
        // Comprobar si la restricción existe
        RestriccionEmpresaModelo restriccion = restriccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la restricción con el ID: " + id));

        // Realizar la eliminación
        restriccionRepository.delete(restriccion);
    }
}
