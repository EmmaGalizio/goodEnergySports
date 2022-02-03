package emma.galzio.goodenergysports.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permiso {

    @EqualsAndHashCode.Include
    private String url;
    @EqualsAndHashCode.Include
    private String metodo;
    private List<RolUsuario> roles;

    public boolean permiteRol(String rol){
        if(rol == null || rol.trim().isEmpty()) return false;
        for(RolUsuario rolUsuario:roles){
            if(rolUsuario.esRol(rol.trim())) return true;
        }
        return false;
    }

    public void agregarRol(RolUsuario rol){
        if(!this.permiteRol(rol.getRol())){
            if(roles == null) roles = new LinkedList<>();
            roles.add(rol);
        }
    }

}
