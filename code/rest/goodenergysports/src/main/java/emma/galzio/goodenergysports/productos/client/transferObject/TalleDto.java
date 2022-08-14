package emma.galzio.goodenergysports.productos.client.transferObject;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TalleDto extends RepresentationModel<TalleDto> {

    private String talle;
    private String equivalencia;
}
