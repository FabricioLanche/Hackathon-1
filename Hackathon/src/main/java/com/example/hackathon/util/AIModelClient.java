package com.sparkyconsulting.aihub.util;

import org.springframework.stereotype.Component;

@Component
public class AIModelClient {

    public String enviarConsultaTexto(String modelo, String input) {
        // Simulación de una integración con el SDK de GitHub Models
        return "Respuesta del modelo " + modelo + " para input: " + input;
    }

    public String enviarConsultaMultimodal(String modelo, String texto, String imagen) {
        // Simulación básica de respuesta multimodal
        return "Respuesta multimodal de " + modelo + " con imagen " + imagen;
    }

    public int contarTokens(String input) {
        // Simulación de conteo de tokens. Esto sería provisto por el SDK real.
        return input.length() / 4; // Ejemplo básico
    }
}
