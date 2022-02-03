package emma.galzio.goodenergysports;

import emma.galzio.goodenergysports.clientes.common.controller.IClientesService;
import emma.galzio.goodenergysports.clientes.domain.Cliente;
import emma.galzio.goodenergysports.clientes.persistence.entity.ClienteEntity;
import emma.galzio.goodenergysports.clientes.transferObject.*;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

@SpringBootTest
public class RegistroClienteTest {

    @Autowired
    private TransferMapper<ClienteDto, Cliente> clienteTransferMapper;
    @Autowired
    private EntityMapper<ClienteEntity,Cliente> clienteEntityMapper;
    @Autowired
    private IClientesService clientesService;


    @Test
    //@Transactional
    public void testClienteDtoMapper(){


        ClienteDto clienteDto = this.getTestClienteDto();
        Cliente cliente = clienteTransferMapper.mapToBusiness(clienteDto);
        ClienteEntity clienteEntity = clienteEntityMapper.mapToEntity(cliente);
        System.out.println(cliente);

    }

    private ClienteDto getTestClienteDto(){
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setUsuario("emmaGalzio");
        usuarioDto.setEmail("emmagalzio@gmail.com");
        usuarioDto.setPassword("Gali.38748");
        usuarioDto.setFechaAlta(LocalDate.now());

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setUsuario(usuarioDto);
        clienteDto.setNombres("Matias");
        clienteDto.setApellidos("Galizio");
        clienteDto.setTelefono("3543534464");
        clienteDto.setNroDocumento("38748975");
        clienteDto.setTipoDocumento("DNI");

        LocalidadDto localidadDto = new LocalidadDto();
        localidadDto.setId(1505);
        DomicilioDto domicilioDto = new DomicilioDto();
        domicilioDto.setCalle("URIBURU");
        domicilioDto.setNumero(31);
        domicilioDto.setLocalidad(localidadDto);
        clienteDto.setDomicilio(domicilioDto);
        return clienteDto;
    }

    @Test
    @Rollback(value = false)
    public void testInsertCliente(){

        //https://www.baeldung.com/spring-5-webclient

        ClienteDto clienteDto = this.getTestClienteDto();
        //Crea un WebClient para la base URI del proyecto actual
        /*WebClient webClient = WebClient.create("http://127.0.0.1:8080/api");
        //No se debe reutilizar la uriSpec porque pueden mantener datos que modifiquen solicitudes futuras
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.post();

        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/cliente");
        //Mono es un publisher, no sé para qué lo utilizará en vez de usar directamente el objeto
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(Mono.just(clienteDto),ClienteDto.class);
        //agrega los headers necesarios al builder
        headersSpec.accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .acceptCharset(StandardCharsets.UTF_8);
        Mono<ClienteDto> response = headersSpec.exchangeToMono(monoResponse ->{
           if(monoResponse.statusCode().is2xxSuccessful()){
               return monoResponse.bodyToMono(ClienteDto.class);
           } else{
               return monoResponse.createException().flatMap(Mono::error);
           }
        });

         */
        ClienteDto clienteInsertado = clientesService.registrarCliente(clienteDto);
        System.out.println("Cliente registrado:");
        System.out.println(clienteInsertado);
        //System.out.println(response.block());
    }

}
