package Hackathon.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModeloTest {

    @Test
    void testModeloAttributes() {
        // Crear una instancia de Modelo
        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setTipoModelo("GPT-4");
        modelo.setLimiteSolicitudes(100);
        modelo.setLimiteTokensGlobal(1000);

        // Validar los atributos
        assertThat(modelo.getId()).isEqualTo(1L);
        assertThat(modelo.getTipoModelo()).isEqualTo("GPT-4");
        assertThat(modelo.getLimiteSolicitudes()).isEqualTo(100);
        assertThat(modelo.getLimiteTokensGlobal()).isEqualTo(1000);
    }
}