package emma.galzio.goodenergysports.security.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permiso")
public class PermisoEntity {
    @EmbeddedId
    private PermisoEntityId id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "permiso_x_rol",
                joinColumns = {@JoinColumn(name = "url",referencedColumnName = "url"),
                        @JoinColumn(name = "metodo",referencedColumnName = "metodo")},
                inverseJoinColumns = @JoinColumn(name = "id_rol_usuario"))
    private List<RolUsuarioEntity> roles;

    public PermisoEntityId getId() {
        return id;
    }

    public void setId(PermisoEntityId id) {
        this.id = id;
    }

    public List<RolUsuarioEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RolUsuarioEntity> roles) {
        this.roles = roles;
    }

}