package emma.galzio.goodenergysports.productos.admin.initializationController;

import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductoAdminDtoWrapper {

    private List<ProductoAdminDto> productos;
}
