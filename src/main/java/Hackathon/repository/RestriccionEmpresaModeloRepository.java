package Hackathon.repository;

import Hackathon.domain.Empresa;
import Hackathon.domain.Modelo;
import Hackathon.domain.RestriccionEmpresaModelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestriccionEmpresaModeloRepository extends JpaRepository<RestriccionEmpresaModelo, Long> {
    Optional<RestriccionEmpresaModelo> findByEmpresaAndModelo(Empresa empresa, Modelo modelo);
    List<RestriccionEmpresaModelo> findByEmpresa(Empresa empresa);
}
