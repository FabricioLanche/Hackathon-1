package Hackathon.domain;

import Hackathon.dto.ConsultaDTO;
import Hackathon.githubmodels.AIModel;
import Hackathon.githubmodels.AIModelFactory;
import Hackathon.repository.ModeloRepository;
import Hackathon.repository.SolicitudRepository;
import Hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final AIModelFactory aiModelFactory;

    @Autowired
    public ModeloService(ModeloRepository modeloRepository, SolicitudRepository solicitudRepository,
                         UsuarioRepository usuarioRepository, AIModelFactory aiModelFactory) {
        this.modeloRepository = modeloRepository;
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.aiModelFactory = aiModelFactory;
    }

    public String processRequest(ConsultaDTO consultaDTO) {
        // 1. Validar si el modelo existe
        Optional<Modelo> modeloOptional = modeloRepository.findByTipoModelo(consultaDTO.getModelo());
        if (modeloOptional.isEmpty()) {
            throw new IllegalArgumentException("El modelo especificado no existe: " + consultaDTO.getModelo());
        }

        // 2. Obtener el servicio correspondiente al modelo
        AIModel modeloService = aiModelFactory.getModelService(consultaDTO.getModelo());

        // 3. Llamar al modelo para generar la respuesta
        String respuesta = modeloService.generarRespuesta(consultaDTO.getTexto());

        // 4. Registrar la solicitud
        registrarSolicitud(consultaDTO, respuesta, modeloOptional.get());

        return respuesta;
    }

    private void registrarSolicitud(ConsultaDTO consultaDTO, String respuesta, Modelo modelo) {
        Solicitud solicitud = new Solicitud();
        solicitud.setModelo(modelo);
        solicitud.setPrompt(consultaDTO.getTexto());
        solicitud.setRespuesta(respuesta);
        solicitud.setFechaSolicitud(LocalDateTime.now());

        if (consultaDTO.getUsuarioId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(consultaDTO.getUsuarioId());
            usuarioOptional.ifPresent(solicitud::setUsuario);
        }

        solicitudRepository.save(solicitud);
    }

    public List<Modelo> listarModelos() {
        return modeloRepository.findAll(); // Obtener todos los modelos disponibles
    }

    public List<Solicitud> obtenerHistorialPorUsuario(Long usuarioId) {
        return solicitudRepository.findByUsuarioId(usuarioId);
    }
}
