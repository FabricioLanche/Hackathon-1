package Hackathon.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @NotNull
    private String nombre;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    private Integer limiteSolicitudes;

    private Integer limiteTokensGlobal;

    private String ventanaTiempo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Solicitud> solicitudes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @PrePersist
    public void aplicarRestricciones() {
        if (empresa != null && modelo != null) {
            for (RestriccionEmpresaModelo restriccion : empresa.getRestriccionesModelos()) {
                if (restriccion.getModelo().equals(modelo)) {
                    // Aplicar restricciones si existen
                    if (restriccion.getLimiteSolicitudes() != null) {
                        this.limiteSolicitudes = restriccion.getLimiteSolicitudes();
                    }
                    if (restriccion.getLimiteTokensGlobal() != null) {
                        this.limiteTokensGlobal = restriccion.getLimiteTokensGlobal();
                    }
                    if (restriccion.getVentanaTiempo() != null) {
                        this.ventanaTiempo = restriccion.getVentanaTiempo();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
