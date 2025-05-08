package Hackathon.application;

import Hackathon.domain.ModeloService;
import Hackathon.domain.Solicitud;
import Hackathon.domain.Usuario;
import Hackathon.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AllIntegrationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModeloService modeloService;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();

        // Cargar un usuario de prueba en la base de datos
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuarioRepository.save(usuario);
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void testGetUserHistory_NoHistory() {
        // Ejecutar GET /api/ai/history
        String url = "http://localhost:" + port + "/api/ai/history";
        List<?> response = restTemplate.getForObject(url, List.class);

        assertThat(response).isEmpty(); // Validar que el historial esté vacío
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void testGetUserHistory_WithEntries() {
        // Insertar datos simulados en la base de datos
        Solicitud solicitud = new Solicitud();
        solicitud.setPrompt("¿Cuál es la capital de Francia?");
        solicitud.setRespuesta("París");
        solicitud.setUsuario(usuarioRepository.findById(1L).get());
        modeloService.obtenerHistorialPorUsuario(1L).add(solicitud); // Simular datos si fuera necesario.

        // Ejecutar GET /api/ai/history
        String url = "http://localhost:" + port + "/api/ai/history";
        List<?> response = restTemplate.getForObject(url, List.class);

        assertThat(response).isNotEmpty();
        assertThat(response.get(0).toString()).contains("¿Cuál es la capital de Francia?");
    }
}