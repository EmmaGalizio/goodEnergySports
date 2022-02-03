package emma.galzio.goodenergysports.clientes.transferObject;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.RepresentationModel;


@Data
@NoArgsConstructor
public class LocalidadDto extends RepresentationModel<LocalidadDto> {

    private Integer id;
    private String nombre;
    private Integer codigoPostal;
    private ProvinciaDto provincia;
}
