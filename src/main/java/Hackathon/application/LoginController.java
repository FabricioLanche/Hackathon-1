package Hackathon.application;

import Hackathon.domain.Usuario;
import Hackathon.dto.LoginRequestDTO;
import Hackathon.dto.LoginResponseDTO;
import Hackathon.domain.UsuarioService;
import Hackathon.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // Buscar al usuario por email
        Usuario usuario = usuarioService.buscarPorEmail(loginRequest.getEmail())
                .orElse(null);

        // Validar si el usuario existe y si la contraseña es válida
        if (usuario == null || !passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas. Verifique su email o contraseña.");
        }

        // Generar el token JWT
        String token = jwtService.generateToken((UserDetails) usuario);

        // Crear la respuesta del login
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol().name());

        // Retornar el token y detalles del usuario
        return ResponseEntity.ok(response);
    }
}
