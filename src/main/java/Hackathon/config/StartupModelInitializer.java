package Hackathon.config;

import Hackathon.domain.Modelo;
import Hackathon.repository.ModeloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class StartupModelInitializer {

    @Bean
    CommandLineRunner initModelos(ModeloRepository modeloRepository) {
        return args -> {
            if (!modeloRepository.existsByTipoModelo("GPT")) {
                Modelo gpt = new Modelo();
                gpt.setTipoModelo("GPT");
                gpt.setLimiteSolicitudes(1000);
                gpt.setLimiteTokensGlobal(10000);

                Modelo DeepSeek = new Modelo();
                DeepSeek.setTipoModelo("DEEPSEEK-R1"); // Cambiamos el nombre para el nuevo modelo
                DeepSeek.setLimiteSolicitudes(500);
                DeepSeek.setLimiteTokensGlobal(5000);

                Modelo meta = new Modelo();
                meta.setTipoModelo("META"); // Modelo "Meta"
                meta.setLimiteSolicitudes(800);
                meta.setLimiteTokensGlobal(8000);

                Modelo maverick = new Modelo();
                maverick.setTipoModelo("MAVERICK"); // Modelo "Meta"
                maverick.setLimiteSolicitudes(1000);
                maverick.setLimiteTokensGlobal(10000);

                modeloRepository.saveAll(Arrays.asList(gpt, DeepSeek, meta, maverick));
                System.out.println("Modelos iniciales creados exitosamente.");
            } else {
                System.out.println("Modelos ya existentes en la base de datos.");
            }
        };
    }
}
