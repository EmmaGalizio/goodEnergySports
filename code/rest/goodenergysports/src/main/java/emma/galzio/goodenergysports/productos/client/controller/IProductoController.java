package emma.galzio.goodenergysports.productos.client.controller;

import emma.galzio.goodenergysports.productos.client.transferObject.ProductoDto;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;

import java.util.List;

public interface IProductoController {

    List<ProductoDto> listarProductos(Integer page, Integer size, ProductoFilter filtros);

    ProductoDto buscarProducto(Integer codigo);

}
