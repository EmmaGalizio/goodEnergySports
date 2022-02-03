package emma.galzio.goodenergysports.clientes.persistence.entity;

import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {
    @Id
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "password", nullable = false, length = 300)
    private String password;

    @Column(name = "salt", length = 50)
    private String salt;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol", nullable = false)
    private RolUsuarioEntity rol;

    public RolUsuarioEntity getRol() {
        return rol;
    }

    public void setRol(RolUsuarioEntity rol) {
        this.rol = rol;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String id) {
        this.email = id;
    }
}