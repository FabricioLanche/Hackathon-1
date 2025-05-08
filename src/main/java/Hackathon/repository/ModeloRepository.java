package Hackathon.repository;

import Hackathon.domain.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Optional<Modelo> findByTipoModelo(String tipoModelo);

    boolean existsByTipoModelo(String gpt);
}
