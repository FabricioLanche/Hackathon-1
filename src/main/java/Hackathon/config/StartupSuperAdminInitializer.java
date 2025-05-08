package Hackathon.config;

import Hackathon.domain.Usuario;
import Hackathon.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static Hackathon.domain.Rol.ROLE_SPARKY_ADMIN;

@Component
public class StartupSuperAdminInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initSuperAdmin() {
        // Verificar si ya existe algún usuario en la base de datos
        if (usuarioRepository.count() == 0) {
            // Crear el superadministrador inicial
            Usuario superAdmin = new Usuario();
            superAdmin.setNombre("Super Admin");
            superAdmin.setEmail("superadmin@sparky.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123")); // Contraseña encriptada
            superAdmin.setRol(ROLE_SPARKY_ADMIN);

            // Guardar en la base de datos
            usuarioRepository.save(superAdmin);
        }
    }

}
