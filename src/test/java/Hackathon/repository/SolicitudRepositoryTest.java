package Hackathon.repository;

import Hackathon.domain.Modelo;
import Hackathon.domain.Solicitud;
import Hackathon.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    private Usuario usuario;
    private Modelo modelo;

    @BeforeEach
    void setUp() {
        // Crear usuario y modelo de prueba
        usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("user@example.com");
        usuario.setPassword("password123");
        usuario = usuarioRepository.save(usuario);

        modelo = new Modelo();
        modelo.setTipoModelo("GPT-4");
        modelo.setLimiteSolicitudes(500);
        modelo.setLimiteTokensGlobal(5000);
        modelo = modeloRepository.save(modelo);

        // Crear solicitudes de prueba
        Solicitud solicitud1 = new Solicitud();
        solicitud1.setUsuario(usuario);
        solicitud1.setModelo(modelo);
        solicitud1.setTokensConsumidos(200);
        solicitud1.setPrompt("¿Qué es Java?");
        solicitud1.setRespuesta("Un lenguaje de programación.");
        solicitud1.setFechaSolicitud(LocalDateTime.now());
        solicitudRepository.save(solicitud1);

        Solicitud solicitud2 = new Solicitud();
        solicitud2.setUsuario(usuario);
        solicitud2.setModelo(modelo);
        solicitud2.setTokensConsumidos(100);
        solicitud2.setPrompt("¿Qué es Spring?");
        solicitud2.setRespuesta("Un framework.");
        solicitud2.setFechaSolicitud(LocalDateTime.now());
        solicitudRepository.save(solicitud2);
    }

    @Test
    void testFindByUsuarioId() {
        List<Solicitud> solicitudes = solicitudRepository.findByUsuarioId(usuario.getId());

        assertThat(solicitudes).hasSize(2);
    }

    @Test
    void testSumTokensByUsuarioId() {
        Integer totalTokens = solicitudRepository.sumTokensByUsuarioId(usuario.getId());

        assertThat(totalTokens).isEqualTo(300);
    }
}