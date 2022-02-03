package emma.galzio.goodenergysports.security.persistence.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rol_usuario")
public class RolUsuarioEntity {

    public RolUsuarioEntity(){

    }

    public RolUsuarioEntity(Integer idRol, String rol) {
        this.idRol = idRol;
        this.rol = rol;
    }

    @Id
    @Column(name = "id_rol_usuario", nullable = false)
    private Integer idRol;

    @Column(name = "rol", nullable = false, length = 45, unique = true)
    private String rol;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    //@JoinTable(name="permiso_x_rol")
    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "roles")
    private List<PermisoEntity> permisos = new ArrayList<>();

    public List<PermisoEntity> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<PermisoEntity> permisoEntities) {
        this.permisos = permisoEntities;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer id) {
        this.idRol = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolUsuarioEntity that = (RolUsuarioEntity) o;
        return Objects.equals(idRol, that.idRol) && rol.equals(that.rol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRol, rol);
    }
}