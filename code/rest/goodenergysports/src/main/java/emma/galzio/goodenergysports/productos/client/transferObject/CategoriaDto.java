package emma.galzio.goodenergysports.productos.client.transferObject;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class CategoriaDto extends RepresentationModel<CategoriaDto> {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private CategoriaDto categoriaSuperior;
    private List<CategoriaDto> subCategorias;
}
