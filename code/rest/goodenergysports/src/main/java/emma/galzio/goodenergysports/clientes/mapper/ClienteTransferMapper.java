package emma.galzio.goodenergysports.clientes.mapper;

import emma.galzio.goodenergysports.clientes.common.api.ClienteRestController;
import emma.galzio.goodenergysports.clientes.domain.*;
import emma.galzio.goodenergysports.clientes.domain.documentoValidator.IValidadorDocumento;
import emma.galzio.goodenergysports.clientes.persistence.entity.LocalidadEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.TipoDocumentoEntity;
import emma.galzio.goodenergysports.clientes.persistence.repository.LocalidadEntityRepository;
import emma.galzio.goodenergysports.clientes.persistence.repository.TipoDocumentoRepository;
import emma.galzio.goodenergysports.clientes.transferObject.ClienteDto;
import emma.galzio.goodenergysports.clientes.transferObject.DomicilioDto;
import emma.galzio.goodenergysports.clientes.transferObject.UsuarioDto;
import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ClienteTransferMapper implements TransferMapper<ClienteDto, Cliente> {

    @Autowired
    private LocalidadEntityRepository localidadRepository;
    @Autowired
    private Map<String,IValidadorDocumento> validadorDocumentoStrategies;
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    public ClienteDto mapToDto(Cliente business) {

        if(business == null) throw new NullPointerException("El cliente no puede ser nulo");
        business.validar();
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(ClienteDto.class , Cliente.class )
                .exclude("usuario")
                .exclude("tipoDocumento").byDefault().register();
        ClienteDto clienteDto = mapperFactory.getMapperFacade().map(business, ClienteDto.class);
        clienteDto.setUsuario(this.mapUsuarioToDto(business.getUsuario(),mapperFactory));
        clienteDto.setTipoDocumento(business.getTipoDocumento().getTipo());
        this.setLinksCliente(clienteDto);

        return clienteDto;
    }

    private UsuarioDto mapUsuarioToDto(Usuario usuario, MapperFactory mapperFactory) {
        mapperFactory.classMap(Usuario.class,UsuarioDto.class).exclude("rol").byDefault().register();
        return mapperFactory.getMapperFacade().map(usuario, UsuarioDto.class);
    }
    private void setLinksCliente(ClienteDto dto) {

        dto.add(linkTo(methodOn(ClienteRestController.class).buscarCliente(dto.getCodigo())).withSelfRel());
        dto.add(linkTo(methodOn(ClienteRestController.class)
                .listarClientes(null,null,false)).withRel("clientes"));

        //Podría agregar los links de las localidades
    }

    @Override
    public List<ClienteDto> mapAllToDto(List<Cliente> businessList) {
        if(businessList == null || businessList.isEmpty()) throw new NullPointerException("La lista de clientes no puede estar vacía");
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Cliente mapToBusiness(ClienteDto dto) {
        if(dto == null) throw new NullPointerException("El cliente no puede ser nulo");
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(ClienteDto.class , Cliente.class )
                                .exclude("domicilio").exclude("usuario")
                                .exclude("tipoDocumento").byDefault().register();
        Cliente cliente = mapperFactory.getMapperFacade().map(dto, Cliente.class);
        cliente.setUsuario(this.mapUsuarioToBusiness(dto.getUsuario(), mapperFactory));
        cliente.setDomicilio(this.mapDomicilioToBusiness(dto.getDomicilio(), mapperFactory));
        cliente.setTipoDocumento(this.mapTipoDocumentoToBusiness(dto.getTipoDocumento()));
        //cliente.validar();
        return cliente;
    }
    private TipoDocumento mapTipoDocumentoToBusiness(String sTipoDocumento){

        TipoDocumentoEntity tipoDocumentoEntity = tipoDocumentoRepository.getById(sTipoDocumento);
        try{
             return new TipoDocumento(tipoDocumentoEntity.getTipo(),
                    tipoDocumentoEntity.getLongitud(),
                    validadorDocumentoStrategies.get(tipoDocumentoEntity.getTipo()));
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("El tipo de documento ingresado es incorrecto");
            domainException.addCause("TIPO_DOCUMENTO", "El tipo de documento ingresado es incorrecto");
            throw domainException;
        }
    }

    private Usuario mapUsuarioToBusiness(UsuarioDto dto, MapperFactory mapperFactory){
        mapperFactory.classMap(UsuarioDto.class,Usuario.class).byDefault().register();
        MapperFacade facade = mapperFactory.getMapperFacade();
        Usuario usuario = facade.map(dto, Usuario.class);
        RolUsuario rolUsuario = new RolUsuario("USUARIO",null);
        usuario.setRol(rolUsuario);
        return usuario;
    }
    private Domicilio mapDomicilioToBusiness(DomicilioDto domicilioDto, MapperFactory mapperFactory){
        mapperFactory.classMap(DomicilioDto.class,Domicilio.class).exclude("localidad")
                                                        .byDefault().register();
        Domicilio domicilio = mapperFactory.getMapperFacade().map(domicilioDto,Domicilio.class);
        LocalidadEntity localidadEntity = localidadRepository.getById(domicilioDto.getLocalidad().getId());
        mapperFactory.classMap(LocalidadEntity.class, Localidad.class).byDefault().register();
        Localidad localidad = mapperFactory.getMapperFacade().map(localidadEntity, Localidad.class);
        domicilio.setLocalidad(localidad);
        return domicilio;
    }

    @Override
    public List<Cliente> mapAllToBusiness(List<ClienteDto> dtoList) {
        if(dtoList == null || dtoList.isEmpty()) throw new NullPointerException("La lista de clientes no puede estar vacía");

        return dtoList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
