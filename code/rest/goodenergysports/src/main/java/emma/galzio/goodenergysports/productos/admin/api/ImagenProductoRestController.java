package emma.galzio.goodenergysports.productos.admin.api;

import emma.galzio.goodenergysports.productos.admin.controller.IProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/producto/{codigo}/imagen")
public class ImagenProductoRestController {

    @Autowired
    private IProductoAdminService productoAdminService;

    @GetMapping
    public ResponseEntity<List<ImagenProductoDto>> listAllProductImages(@PathVariable("codigo")
                                                                                    Integer codigo){
        return ResponseEntity.ok(productoAdminService.listAllImages(codigo));
    }

    @GetMapping("/{orden}")
    public ResponseEntity<ImagenProductoDto> getProductImage(@PathVariable("codigo") Integer codigo,
                                                             @PathVariable("orden")Integer orden){
        return ResponseEntity.ok(productoAdminService.findProductImage(codigo, orden));
    }

    @DeleteMapping("/{orden}")
    public CollectionModel<ImagenProductoDto> deleteProductImage(@PathVariable("codigo") Integer codigo,
                                              @PathVariable("orden")Integer orden){

        List<ImagenProductoDto> result = productoAdminService.deleteImage(codigo,orden);
        return CollectionModel.of(result);
    }

}
