package emma.galzio.goodenergysports.productos.client.transferObject;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class StockDto extends RepresentationModel<StockDto> {

    private Integer stockDisponible;
    private TalleDto talle;

}
