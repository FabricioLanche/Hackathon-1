package Hackathon.dto;

import lombok.Data;
import Hackathon.domain.Rol;

@Data
public class UsuarioDTO {
    private Rol rol;
    private String password;
    private String nombre;
    private String email;
    private Long empresaId;
}
