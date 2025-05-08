package Hackathon.application;

import Hackathon.domain.RestriccionEmpresaModelo;
import Hackathon.domain.RestriccionService;
import Hackathon.domain.Usuario;
import Hackathon.dto.RestriccionDTO;
import Hackathon.exceptions.NotFoundException;
import Hackathon.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
public class CompanyRestrictionsController {

    private final RestriccionService restriccionService;
    private final UsuarioRepository usuarioRepository;

    public CompanyRestrictionsController(RestriccionService restriccionService, UsuarioRepository usuarioRepository) {
        this.restriccionService = restriccionService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> createRestriction(@RequestBody RestriccionDTO dto) {
        try {
            RestriccionEmpresaModelo restriccion = restriccionService.createRestriction(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(restriccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> listRestrictions() {
        try {
            Long empresaId = getAuthenticatedCompanyId();
            List<RestriccionEmpresaModelo> restricciones = restriccionService.listRestrictions(empresaId);
            return ResponseEntity.ok(restricciones); // Respuesta con la lista de restricciones
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> updateRestriction(@PathVariable Long id, @RequestBody RestriccionDTO dto) {
        try {
            RestriccionEmpresaModelo restriccionActualizada = restriccionService.updateRestriction(id, dto);
            return ResponseEntity.ok(restriccionActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> deleteRestriction(@PathVariable Long id) {
        try {
            restriccionService.deleteRestriction(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private Long getAuthenticatedCompanyId() {
        // Obtener el objeto principal desde el contexto de seguridad
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificar que sea del tipo org.springframework.security.core.userdetails.User
        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            // Extraer el email (username) del usuario autenticado
            String email = springUser.getUsername();

            // Buscar al usuario personalizado en la base de datos utilizando el email
            Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el email: " + email));

            // Verificar que el usuario tenga una empresa asociada
            if (usuarioAutenticado.getEmpresa() == null) {
                throw new IllegalArgumentException("El usuario autenticado no tiene una empresa asociada.");
            }

            // Retornar el ID de la empresa asociada
            return usuarioAutenticado.getEmpresa().getId();
        }

        // Si el principal no es del tipo esperado, lanzar una excepción
        throw new IllegalArgumentException("Error al recuperar el usuario autenticado.");
    }
}
