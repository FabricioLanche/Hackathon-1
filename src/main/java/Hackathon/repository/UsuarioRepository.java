package Hackathon.repository;

import Hackathon.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    List<Usuario> findByEmpresaId(Long empresaId);
    boolean existsByEmail(String email);
}
