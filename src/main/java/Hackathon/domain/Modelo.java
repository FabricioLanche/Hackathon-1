package Hackathon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
@Data
@Table(name = "modelos")
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String tipoModelo;

    @NotNull
    private Integer limiteSolicitudes; // Límite máximo de solicitudes globales para este modelo

    @NotNull
    private Integer limiteTokensGlobal; // Límite máximo de tokens globales para este modelo
}



