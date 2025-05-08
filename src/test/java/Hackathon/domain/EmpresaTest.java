package Hackathon.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmpresaTest {

    @Test
    void testAgregarRestriccionParaModelo() {
        // Crear instancias de Empresa y RestriccionEmpresaModelo
        Empresa empresa = new Empresa();
        RestriccionEmpresaModelo restriccion = new RestriccionEmpresaModelo();

        // Agregar la restricción
        empresa.agregarRestriccionParaModelo(restriccion);

        // Validar que la empresa esté asociada con la restricción
        assertThat(restriccion.getEmpresa()).isEqualTo(empresa);
        assertThat(empresa.getRestriccionesModelos()).contains(restriccion);
    }

    @Test
    void testSetAttributes() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Empresa Test");
        empresa.setRuc("12345678901");
        empresa.setFechaAfiliacion(LocalDate.now());
        empresa.setEstadoActivo(true);

        // Validar los atributos establecidos
        assertThat(empresa.getId()).isEqualTo(1L);
        assertThat(empresa.getNombre()).isEqualTo("Empresa Test");
        assertThat(empresa.getRuc()).isEqualTo("12345678901");
        assertThat(empresa.getFechaAfiliacion()).isEqualTo(LocalDate.now());
        assertThat(empresa.getEstadoActivo()).isTrue();
    }
}