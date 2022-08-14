package emma.galzio.goodenergysports.productos.client.transferObject;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data

public class ProductoDto extends RepresentationModel<ProductoDto> {

    private Integer codigo;
    private String nombre;
    private String descripcion;
    private Float precio;
    private Integer idCategoria;
    private String nombreCategoria;
    private List<StockDto> stock;
    private List<ImagenProductoDto> imagenes;
}
