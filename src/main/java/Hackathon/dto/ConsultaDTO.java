package Hackathon.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ConsultaDTO {
    private String texto; // Consulta en texto enviada al modelo de IA
    private String modelo; // Nombre del modelo utilizado (e.g., "gpt-4")
    private Long usuarioId; // Identificador del usuario haciendo la consulta
    private Long empresaId; // Identificador de la empresa requerida para rastrear
}
