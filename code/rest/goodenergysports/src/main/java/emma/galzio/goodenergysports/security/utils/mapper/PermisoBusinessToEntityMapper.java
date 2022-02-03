package emma.galzio.goodenergysports.security.utils.mapper;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntityId;
import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import emma.galzio.goodenergysports.utils.mapper.BusinessToEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermisoBusinessToEntityMapper  implements BusinessToEntityMapper<PermisoEntity, Permiso> {


    @Override
    public PermisoEntity mapToEntity(Permiso business) {

        if(business == null) throw new NullPointerException("El permiso no puede ser nulo");
        PermisoEntityId permisoEntityId = new PermisoEntityId(business.getUrl(), business.getMetodo());
        PermisoEntity permisoEntity = new PermisoEntity();
        permisoEntity.setId(permisoEntityId);
        if(business.getRoles() != null && !business.getRoles().isEmpty()){
            permisoEntity.setRoles(business.getRoles().stream()
                    .map(rol -> new RolUsuarioEntity(null,rol.getRol())).collect(Collectors.toList()));
        }
        return permisoEntity;
    }

    @Override
    public List<PermisoEntity> mapAllToEntity(List<Permiso> businessList) {
        if(businessList == null || businessList.isEmpty()) throw new NullPointerException("La lista de permisos no puede estar vacía");
        return businessList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
