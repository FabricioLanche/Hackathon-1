package Hackathon.repository;

import Hackathon.domain.Empresa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Test
    void testSaveAndFindById() {
        // Crear una nueva empresa
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa Test");
        empresa.setRuc("12345678901");
        empresa.setEstadoActivo(true);
        empresa = empresaRepository.save(empresa);

        // Buscar por ID
        var resultado = empresaRepository.findById(empresa.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Empresa Test");
    }

    @Test
    void testExistsByRuc() {
        // Crear una nueva empresa
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa Test");
        empresa.setRuc("12345678901");
        empresa.setEstadoActivo(true);
        empresaRepository.save(empresa);

        // Verificar existencia por RUC
        boolean exists = empresaRepository.existsByRuc("12345678901");

        assertThat(exists).isTrue();
    }
}