package emma.galzio.goodenergysports.clientes.transferObject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class ProvinciaDto extends RepresentationModel<ProvinciaDto> {
    private Integer id;
    private String nombre;
}
