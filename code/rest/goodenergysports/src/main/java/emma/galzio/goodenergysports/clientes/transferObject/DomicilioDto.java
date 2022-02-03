package emma.galzio.goodenergysports.clientes.transferObject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class DomicilioDto extends RepresentationModel<DomicilioDto> {

    private String calle;
    private Integer numero;
    private Integer piso;
    private String dpto;
    private String referencia;
    private LocalidadDto localidad;
}
