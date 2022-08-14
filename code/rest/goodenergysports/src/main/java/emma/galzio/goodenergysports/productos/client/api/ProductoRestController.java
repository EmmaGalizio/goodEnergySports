package emma.galzio.goodenergysports.productos.client.api;

import emma.galzio.goodenergysports.productos.client.controller.IProductoController;
import emma.galzio.goodenergysports.productos.client.transferObject.ProductoDto;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {

    @Autowired
    private IProductoController productoController;

    @GetMapping
    public List<ProductoDto> listarProductos(
            @RequestParam(value = "page",required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10")Integer size,
            ProductoFilter productoFilter){
        return productoController.listarProductos(page, size, productoFilter);
    }

    @GetMapping("/{codigo}")
    public ProductoDto buscarProducto(@PathVariable("codigo") Integer codigo){

        return productoController.buscarProducto(codigo);
    }

}
