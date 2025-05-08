package Hackathon.dto;

import lombok.Data;

@Data
public class ModeloDTO {
    private Long id;
    private String tipoModelo;
    private Integer limiteSolicitudes;
    private Integer limiteTokens;
}
