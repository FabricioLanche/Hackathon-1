package Hackathon.application;

import Hackathon.domain.Modelo;
import Hackathon.domain.ModeloService;
import Hackathon.domain.Solicitud;
import Hackathon.domain.Usuario;
import Hackathon.dto.ConsultaDTO;
import Hackathon.exceptions.NotFoundException;
import Hackathon.exceptions.UnauthorizedAccessException;
import Hackathon.githubmodels.ChatService;
import Hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AllIntegrationController {

    private final ChatService chatService;
    private final ModeloService modeloService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AllIntegrationController(ChatService chatService, ModeloService modeloService, UsuarioRepository usuarioRepository) {
        this.chatService = chatService;
        this.modeloService = modeloService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/chat")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> chat(@RequestBody ConsultaDTO consulta) {
        String response = modeloService.processRequest(consulta);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/completion")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> completion(@RequestBody ConsultaDTO consulta) {
        consulta.setTexto("COMPLETA EL SIGUIENTE TEXTO: " + consulta.getTexto());
        String response = modeloService.processRequest(consulta);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/multimodal")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> multimodal(@RequestBody ConsultaDTO consulta) {
        consulta.setTexto("BASE64: " + consulta.getTexto());
        consulta.setModelo("MAVERICK");
        String response = modeloService.processRequest(consulta);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/models")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAvailableModels() {
        List<Modelo> modelos = modeloService.listarModelos(); // Obtener modelos desde el service
        return ResponseEntity.ok(modelos); // Devolver en formato JSON
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserHistory() {
        Long userId = getAuthenticatedUserId(); // Obtener el ID del usuario autenticado
        List<Solicitud> historial = modeloService.obtenerHistorialPorUsuario(userId); // Consultar el historial
        return ResponseEntity.ok(historial); // Devolver la lista de solicitudes en JSON
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            String email = springUser.getUsername();
            Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el email: " + email)); // Usamos NotFoundException
            return usuarioAutenticado.getId();
        }
        throw new UnauthorizedAccessException("Error al recuperar el usuario autenticado."); // Usamos UnauthorizedAccessException
    }


}
