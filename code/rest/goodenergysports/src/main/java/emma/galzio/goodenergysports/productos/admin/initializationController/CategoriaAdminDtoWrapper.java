package emma.galzio.goodenergysports.productos.admin.initializationController;

import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoriaAdminDtoWrapper {

    private List<CategoriaAdminDto> categorias;
}
