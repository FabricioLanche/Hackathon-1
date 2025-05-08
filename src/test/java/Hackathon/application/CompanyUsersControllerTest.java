package Hackathon.application;

import Hackathon.domain.Usuario;
import Hackathon.dto.UsuarioDTO;
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
class CompanyUsersControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "COMPANY_ADMIN")
    void testListUsers_NoUsers() {
        // Ejecutar GET /api/company/users
        String url = "http://localhost:" + port + "/api/company/users";
        List<?> response = restTemplate.getForObject(url, List.class);

        assertThat(response).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "COMPANY_ADMIN")
    void testCreateUser_Success() {
        // Crear un usuario
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("user@example.com");

        String url = "http://localhost:" + port + "/api/company/users";
        Usuario response = restTemplate.postForObject(url, dto, Usuario.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getEmail()).isEqualTo("user@example.com");
    }
}