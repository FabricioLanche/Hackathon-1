package Hackathon.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    @Test
    void testAplicarRestricciones() {
        // Crear instancias de Usuario, Empresa, Modelo, y RestriccionEmpresaModelo
        Usuario usuario = new Usuario();
        Empresa empresa = new Empresa();
        Modelo modelo = new Modelo();
        modelo.setTipoModelo("GPT-4");

        RestriccionEmpresaModelo restriccion = new RestriccionEmpresaModelo();
        restriccion.setModelo(modelo);
        restriccion.setLimiteSolicitudes(5);
        restriccion.setLimiteTokensGlobal(500);

        empresa.agregarRestriccionParaModelo(restriccion);

        usuario.setEmpresa(empresa);
        usuario.setModelo(modelo);

        // Aplicar restricciones al usuario
        usuario.aplicarRestricciones();

        // Validar que las restricciones se apliquen correctamente
        assertThat(usuario.getLimiteSolicitudes()).isEqualTo(5);
        assertThat(usuario.getLimiteTokensGlobal()).isEqualTo(500);
    }

    @Test
    void testGetAuthorities() {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.ROLE_USER);

        // Validar los authorities
        assertThat(usuario.getAuthorities()).hasSize(1);
        assertThat(usuario.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }
}