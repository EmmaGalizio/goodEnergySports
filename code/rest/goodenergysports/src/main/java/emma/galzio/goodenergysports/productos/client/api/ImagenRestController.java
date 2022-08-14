package emma.galzio.goodenergysports.productos.client.api;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos/{codigo}/imagenes")
public class ImagenRestController{

    @GetMapping
    public List<ImagenProductoDto> listarImagenesProducto(@PathVariable("codigo")Integer codigoProducto){
        return null;
    }

    @GetMapping("/{orden}")
    public ImagenProductoDto buscarImagenProducto(@PathVariable("codigo") Integer codigo,
                                                  @PathVariable("orden")Integer orden){
        return null;
    }
}
