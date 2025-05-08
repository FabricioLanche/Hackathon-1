package Hackathon.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void testPrompt_Success() {
        // Simular un prompt exitoso
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/api/chat";
        String prompt = "Hola, ¿cómo estás?";

        String response = restTemplate.postForObject(url, prompt, String.class);

        // Verificar algún criterio típico del sistema al recibir prompts válidos
        assertThat(response).isNotBlank();
    }

    @Test
    void testPrompt_ErrorNoContent() {
        // Simular un error enviando un prompt vacío
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/api/chat";

        HttpClientErrorException exception = null;

        try {
            restTemplate.postForObject(url, "", String.class);
        } catch (HttpClientErrorException e) {
            exception = e;
        }

        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}