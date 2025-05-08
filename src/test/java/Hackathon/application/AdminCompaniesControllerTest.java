package Hackathon.application;

import Hackathon.domain.Empresa;
import Hackathon.domain.EmpresaService;
import Hackathon.dto.EmpresaDTO;
import Hackathon.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql") // Opcional para pre-cargar datos
class AdminCompaniesControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EmpresaRepository empresaRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void testListCompanies_NoCompanies() {
        // Verificar que la base de datos esté vacía
        String url = "http://localhost:" + port + "/api/admin/companies";
        List<?> response = restTemplate.getForObject(url, List.class);

        assertThat(response).isEmpty();
    }

    @Test
    void testGetCompany_NotFound() {
        // Intentar obtener una empresa no existente
        String url = "http://localhost:" + port + "/api/admin/companies/999";

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                restTemplate.getForObject(url, String.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateCompany_Success() {
        // Crear una nueva empresa
        EmpresaDTO nuevaEmpresa = new EmpresaDTO();
        nuevaEmpresa.setNombre("Empresa Test");

        String url = "http://localhost:" + port + "/api/admin/companies";
        Empresa response = restTemplate.postForObject(url, nuevaEmpresa, Empresa.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getNombre()).isEqualTo("Empresa Test");
    }
}