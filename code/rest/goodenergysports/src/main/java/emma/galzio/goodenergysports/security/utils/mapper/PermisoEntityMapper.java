package emma.galzio.goodenergysports.security.utils.mapper;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PermisoEntityMapper implements EntityToBusinessMapper<PermisoEntity, Permiso> {

    @Override
    public Permiso mapToBusiness(PermisoEntity entity) {

        if(entity == null) throw new NullPointerException("Se debe proporcionar un permiso existente");
        try{
            return new Permiso(entity.getId().getUrl(),entity.getId().getMetodo(),
                    entity.getRoles() != null ? entity.getRoles().stream()
                            .map((rolE) ->new RolUsuario(rolE.getRol(), rolE.getDescripcion()))
                            .collect(Collectors.toList()) : null);

        }catch(EntityNotFoundException e){
            return null;
        }
    }

    @Override
    public List<Permiso> mapAllToBusiness(List<PermisoEntity> entityList) {
        if(entityList == null || entityList.isEmpty()) throw new NullPointerException("La lista de permisos no puede estar vacía");

        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
