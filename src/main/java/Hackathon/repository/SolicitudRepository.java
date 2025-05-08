package Hackathon.repository;

import Hackathon.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    /**
     * Sumar todos los tokens consumidos por un usuario específico.
     * @param usuarioId ID del usuario.
     * @return La suma total de tokens consumidos.
     */
    @Query("SELECT SUM(s.tokensConsumidos) FROM Solicitud s WHERE s.usuario.id = :usuarioId")
    Integer sumTokensByUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Solicitud> findByUsuarioId(Long usuarioId);
}
