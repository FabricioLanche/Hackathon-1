package Hackathon.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDTO {
    private Long id;
    private String consulta;
    private String respuesta;
    private Integer tokensConsumidos;
    private LocalDateTime fechaHora;
    private String modeloUtilizado;
    private Long usuarioId;
    private Long empresaId;
    private String nombreArchivoMultimodal;
}
