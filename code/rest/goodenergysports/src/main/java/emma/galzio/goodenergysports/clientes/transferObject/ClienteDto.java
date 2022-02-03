package emma.galzio.goodenergysports.clientes.transferObject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class ClienteDto extends RepresentationModel<ClienteDto> {

    private Integer codigo;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String nroDocumento;
    private String tipoDocumento;
    private UsuarioDto usuario;
    private DomicilioDto domicilio;

}
