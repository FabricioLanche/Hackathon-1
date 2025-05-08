package Hackathon.application;

import Hackathon.dto.RestriccionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyRestrictionsControllerTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    @WithMockUser(roles = "COMPANY_ADMIN")
    void testListRestrictions_Empty() {
        // Ejecutar GET /api/company/restrictions
        String url = "http://localhost:" + port + "/api/company/restrictions";
        RestriccionDTO[] response = restTemplate.getForObject(url, RestriccionDTO[].class);

        assertThat(response).isEmpty();
    }

    @Test
    @WithMockUser(roles = "COMPANY_ADMIN")
    void testCreateRestriction_Success() {
        // Crear una nueva restricción
        RestriccionDTO dto = new RestriccionDTO();
        dto.setModeloId(1L);
        dto.setLimiteSolicitudes(100);

        String url = "http://localhost:" + port + "/api/company/restrictions";
        RestriccionDTO response = restTemplate.postForObject(url, dto, RestriccionDTO.class);

        assertThat(response.getModeloId()).isEqualTo(1L);
        assertThat(response.getLimiteSolicitudes()).isEqualTo(100);
    }
}