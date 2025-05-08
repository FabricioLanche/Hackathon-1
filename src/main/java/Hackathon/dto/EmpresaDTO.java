package Hackathon.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class EmpresaDTO {

    private Long id;
    private String nombre;
    private String ruc;
    private LocalDate fechaAfiliacion;
    private Boolean estadoActivo;

    private AdministradorDTO administrador;

    @Data
    public static class AdministradorDTO {
        private Long id;
        private String nombre;
        private String email;
        private String password;
    }
}
