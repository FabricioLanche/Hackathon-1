package Hackathon.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SolicitudTest {

    @Test
    void testSolicitudAttributes() {
        // Crear instancias de Modelo y Usuario
        Modelo modelo = new Modelo();
        Usuario usuario = new Usuario();

        // Crear una instancia de Solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setId(1L);
        solicitud.setModelo(modelo);
        solicitud.setUsuario(usuario);
        solicitud.setPrompt("¿Cuál es la capital de Francia?");
        solicitud.setRespuesta("París");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setTokensConsumidos(50);

        // Validar los atributos
        assertThat(solicitud.getId()).isEqualTo(1L);
        assertThat(solicitud.getModelo()).isEqualTo(modelo);
        assertThat(solicitud.getUsuario()).isEqualTo(usuario);
        assertThat(solicitud.getPrompt()).isEqualTo("¿Cuál es la capital de Francia?");
        assertThat(solicitud.getRespuesta()).isEqualTo("París");
        assertThat(solicitud.getFechaSolicitud()).isNotNull();
        assertThat(solicitud.getTokensConsumidos()).isEqualTo(50);
    }
}