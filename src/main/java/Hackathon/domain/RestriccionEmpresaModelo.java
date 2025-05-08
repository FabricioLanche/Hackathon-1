package Hackathon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Data
@Table(name = "restricciones_empresa_modelo")
public class RestriccionEmpresaModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    // Restricciones principales
    private Integer limiteSolicitudes; // Nulo por defecto (sin restricción)

    private Integer limiteTokensGlobal; // Nulo por defecto (sin restricción)

    private String ventanaTiempo; // Nulo por defecto (sin restricción)

    public RestriccionEmpresaModelo() {
        this.limiteSolicitudes = null;
        this.limiteTokensGlobal = null;
        this.ventanaTiempo = null;
    }

    public RestriccionEmpresaModelo(Empresa empresa, Modelo modelo, Integer limiteSolicitudes, Integer limiteTokensGlobal, String ventanaTiempo) {
        this.empresa = empresa;
        this.modelo = modelo;
        this.limiteSolicitudes = limiteSolicitudes;
        this.limiteTokensGlobal = limiteTokensGlobal;
        this.ventanaTiempo = ventanaTiempo;
    }
}
