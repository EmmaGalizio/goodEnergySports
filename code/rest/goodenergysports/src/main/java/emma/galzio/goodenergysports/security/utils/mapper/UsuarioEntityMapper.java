package emma.galzio.goodenergysports.security.utils.mapper;

import emma.galzio.goodenergysports.clientes.domain.Usuario;
import emma.galzio.goodenergysports.clientes.persistence.entity.UsuarioEntity;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioEntityMapper implements EntityToBusinessMapper<UsuarioEntity, Usuario> {
    @Override
    public Usuario mapToBusiness(UsuarioEntity entity) {

        if(entity == null) throw new NullPointerException("El usuario no puede ser nulo");
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        try{
            mapperFactory.classMap(UsuarioEntity.class,Usuario.class);
            return mapperFactory.getMapperFacade().map(entity,Usuario.class);

        }catch(EntityNotFoundException e){
            return null;
        }
    }

    @Override
    public List<Usuario> mapAllToBusiness(List<UsuarioEntity> entityList) {
        if(entityList == null || entityList.isEmpty()) throw new NullPointerException("La lista de usuarios no puede estar vacia");
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
