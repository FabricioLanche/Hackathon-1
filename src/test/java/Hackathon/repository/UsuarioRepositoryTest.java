package Hackathon.repository;

import Hackathon.domain.Empresa;
import Hackathon.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        // Crear una empresa de prueba
        empresa = new Empresa();
        empresa.setNombre("Empresa Prueba");
        empresa.setRuc("12345678901");
        empresa.setEstadoActivo(true);
        empresa = empresaRepository.save(empresa);

        // Crear usuarios relacionados con la empresa
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Usuario 1");
        usuario1.setEmail("user1@example.com");
        usuario1.setPassword("password1");
        usuario1.setEmpresa(empresa);
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Usuario 2");
        usuario2.setEmail("user2@example.com");
        usuario2.setPassword("password2");
        usuario2.setEmpresa(empresa);
        usuarioRepository.save(usuario2);
    }

    @Test
    void testFindByEmail() {
        Optional<Usuario> usuario = usuarioRepository.findByEmail("user1@example.com");

        assertThat(usuario).isPresent();
        assertThat(usuario.get().getNombre()).isEqualTo("Usuario 1");
    }

    @Test
    void testFindByEmpresaId() {
        var usuarios = usuarioRepository.findByEmpresaId(empresa.getId());

        assertThat(usuarios).hasSize(2);
    }

    @Test
    void testExistsByEmail() {
        boolean exists = usuarioRepository.existsByEmail("user1@example.com");

        assertThat(exists).isTrue();
    }
}