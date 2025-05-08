package Hackathon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @Column(unique = true, length = 11)
    private String ruc;

    @NotNull
    private LocalDate fechaAfiliacion;

    @NotNull
    private Boolean estadoActivo;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "administrador_id", unique = true)
    private Usuario administrador;

    // Modelos disponibles y sus restricciones (muchos-a-muchos con datos adicionales)
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestriccionEmpresaModelo> restriccionesModelos = new ArrayList<>();

    public void agregarRestriccionParaModelo(RestriccionEmpresaModelo restriccion) {
        restriccion.setEmpresa(this);
        restriccionesModelos.add(restriccion);
    }
}
