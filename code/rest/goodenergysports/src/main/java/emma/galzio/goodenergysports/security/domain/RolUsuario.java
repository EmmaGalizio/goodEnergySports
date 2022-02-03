package emma.galzio.goodenergysports.security.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RolUsuario {

    @NonNull
    @EqualsAndHashCode.Include
    private String rol;
    private String descripcion;

    public boolean esRol(String rol){
        if(rol == null || rol.trim().isEmpty()) return false;
        return this.rol.equals(rol);
    }

}
