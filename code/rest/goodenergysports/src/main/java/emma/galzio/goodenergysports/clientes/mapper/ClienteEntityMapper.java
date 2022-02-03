package emma.galzio.goodenergysports.clientes.mapper;

import emma.galzio.goodenergysports.clientes.domain.Cliente;
import emma.galzio.goodenergysports.clientes.domain.TipoDocumento;
import emma.galzio.goodenergysports.clientes.domain.Usuario;
import emma.galzio.goodenergysports.clientes.domain.documentoValidator.IValidadorDocumento;
import emma.galzio.goodenergysports.clientes.persistence.entity.ClienteEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.TipoDocumentoEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.UsuarioEntity;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteEntityMapper implements EntityMapper<ClienteEntity, Cliente> {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;
    @Autowired
    private Map<String, IValidadorDocumento> validadorDocumentoStrategies;

    @Override
    public ClienteEntity mapToEntity(Cliente business) {
        //Este método asumen que el cliente ya se encuentra validado.

        if(business == null) throw new NullPointerException("El cliente no puede ser nulo");

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        //En teoria no debería hacer falta el exclude del salt
        mapperFactory.classMap(ClienteEntity.class,Cliente.class)
                                                .exclude("usuario").byDefault().register();
        ClienteEntity clienteEntity = mapperFactory.getMapperFacade().map(business, ClienteEntity.class);
        clienteEntity.setUsuario(this.mapUsuarioToEntity(business.getUsuario(),mapperFactory));
        return clienteEntity;
    }
    private UsuarioEntity mapUsuarioToEntity(Usuario usuario, MapperFactory mapperFactory){

        mapperFactory.classMap(UsuarioEntity.class,Usuario.class).exclude("rol").byDefault().register();
        UsuarioEntity usuarioEntity = mapperFactory.getMapperFacade().map(usuario,UsuarioEntity.class);
        usuarioEntity.setRol(rolUsuarioRepository.findByRol(usuario.getRol().getRol()));
        return usuarioEntity;
    }

    @Override
    public List<ClienteEntity> mapAllToEntity(List<Cliente> businessList) {

        if(businessList == null || businessList.isEmpty())
                                            throw new NullPointerException("La lista de clientes no puede estar vacía");

        return businessList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public Cliente mapToBusiness(ClienteEntity entity) {

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(ClienteEntity.class,Cliente.class).exclude("tipoDocuento").byDefault().register();
        Cliente cliente = mapperFactory.getMapperFacade().map(entity,Cliente.class);
        cliente.setTipoDocumento(this.mapTipoDocumentoToBusiness(entity.getTipoDocumento()));
        return cliente;

    }

    private TipoDocumento mapTipoDocumentoToBusiness(TipoDocumentoEntity tipoDocumentoEntity) {
        return new TipoDocumento(tipoDocumentoEntity.getTipo(),
                tipoDocumentoEntity.getLongitud(),
                validadorDocumentoStrategies.get(tipoDocumentoEntity.getTipo()));
    }

    @Override
    public List<Cliente> mapAllToBusiness(List<ClienteEntity> entityList) {
        if(entityList == null || entityList.isEmpty())
                                        throw new NullPointerException("La lista de clientes no puede estar vacía");

        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
