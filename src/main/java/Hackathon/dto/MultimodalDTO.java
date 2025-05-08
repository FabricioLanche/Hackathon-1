package Hackathon.dto;

import lombok.Data;

@Data
public class MultimodalDTO {
    private String texto; // Texto asociado a la consulta
    private String archivoNombre; // Nombre del archivo multimodal (imagen, audio, etc.)
    private byte[] archivoContenido; // Contenido del archivo en formato binario
    private String modelo; // Modelo que manejará la interacción
    private Long usuarioId; // Usuario utilizando el servicio
}
