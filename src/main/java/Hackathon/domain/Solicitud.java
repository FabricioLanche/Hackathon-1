package Hackathon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(nullable = false, length = 1000)
    private String prompt;

    @Column(nullable = false, length = 2000)
    private String respuesta;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    private int tokensConsumidos;
}
