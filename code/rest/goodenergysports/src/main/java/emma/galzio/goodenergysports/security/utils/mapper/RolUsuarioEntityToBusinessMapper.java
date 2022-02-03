package emma.galzio.goodenergysports.security.utils.mapper;

import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolUsuarioEntityToBusinessMapper implements EntityToBusinessMapper<RolUsuarioEntity, RolUsuario> {
    @Override
    public RolUsuario mapToBusiness(RolUsuarioEntity entity) {
        return new RolUsuario(entity.getRol(),entity.getDescripcion());

    }

    @Override
    public List<RolUsuario> mapAllToBusiness(List<RolUsuarioEntity> entityList) {
        if(entityList == null || entityList.isEmpty()) throw new NullPointerException("La lista de roles no puede estar vacía");
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
