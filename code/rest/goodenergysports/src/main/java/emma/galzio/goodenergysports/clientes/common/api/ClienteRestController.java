package emma.galzio.goodenergysports.clientes.common.api;


import emma.galzio.goodenergysports.clientes.common.controller.IClientesService;
import emma.galzio.goodenergysports.clientes.transferObject.ClienteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteRestController {

    @Autowired
    private IClientesService clientesService;

    @GetMapping
    public PagedModel<ClienteDto> listarClientes(
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "10")Integer size,
                            @RequestParam(value = "active", required = false, defaultValue = "false")boolean active){
        return null;
    }

    @GetMapping("/{codigo}")
    public RepresentationModel<ClienteDto> buscarCliente(@PathVariable("codigo")Integer codigo){
        return null;
    }

    @PostMapping
    public ResponseEntity<RepresentationModel<ClienteDto>> registrarCliente(@RequestBody ClienteDto clienteDto){

        ClienteDto cliente = clientesService.registrarCliente(clienteDto);

        //No importa el warning, siempre va a tener el link self por el mapper
        return ResponseEntity.created(cliente.getLink("self").get().toUri()).body(cliente);
    }


}