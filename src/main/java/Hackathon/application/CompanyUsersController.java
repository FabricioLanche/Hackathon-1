package Hackathon.application;

import Hackathon.domain.UsuarioService;
import Hackathon.dto.RestriccionDTO;
import Hackathon.dto.UsuarioDTO;
import Hackathon.domain.Usuario;
import Hackathon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users")
public class CompanyUsersController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UsuarioDTO dto) {
        try {
            Usuario nuevoUsuario = usuarioService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> listUsers() {
        try {
            // Obtener el ID de la empresa del usuario autenticado
            Long empresaId = getAuthenticatedCompanyId();

            // Llamar al servicio para obtener los usuarios de la empresa
            List<Usuario> usuarios = usuarioService.listUsers(empresaId);

            return ResponseEntity.ok(usuarios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            Long companyId = getAuthenticatedCompanyId();
            Usuario usuario = usuarioService.getUserByIdAndCompany(id, companyId);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        try {
            Long authenticatedCompanyId = getAuthenticatedCompanyId();
            Usuario usuarioActualizado = usuarioService.updateUser(id, dto, authenticatedCompanyId);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            // Enviar respuesta en caso de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/{id}/limits")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> assignLimits(@PathVariable Long id, @RequestBody RestriccionDTO dto) {
        try {
            Long authenticatedCompanyId = getAuthenticatedCompanyId();
            Usuario usuarioActualizado = usuarioService.assignLimits(id, dto, authenticatedCompanyId);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/consumption")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> getUserConsumption(@PathVariable Long id) {
        try {
            Long authenticatedCompanyId = getAuthenticatedCompanyId();
            Integer totalTokens = usuarioService.getTotalTokensConsumedByUser(id, authenticatedCompanyId);
            return ResponseEntity.ok(totalTokens);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
