package Hackathon.dto;

import lombok.Data;

@Data
public class RestriccionDTO {
    private Long empresaId; // ID de la empresa asociada
    private Long modeloId; // ID del modelo asociado
    private Integer limiteSolicitudes; // Opcional
    private Integer limiteTokensGlobal; // Opcional
    private String ventanaTiempo; // Opcional
}
