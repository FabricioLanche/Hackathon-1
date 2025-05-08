package Hackathon;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackathonApplication {

    public static void main(String[] args) {
        // Cargar variables desde .env
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // Directorio raíz del proyecto
                .ignoreIfMissing() // Ignorar si el archivo .env no existe
                .load();

        // Pasar las variables de .env al sistema
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(HackathonApplication.class, args);
    }
}
