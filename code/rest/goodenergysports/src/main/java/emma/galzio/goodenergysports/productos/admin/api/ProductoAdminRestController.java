package emma.galzio.goodenergysports.productos.admin.api;
import emma.galzio.goodenergysports.productos.admin.controller.IProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/producto")
public class ProductoAdminRestController {

    @Autowired
    @Qualifier("productoAdminService")
    private IProductoAdminService IProductoAdminService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RepresentationModel<ProductoAdminDto>> createNewProducto(
                                                                   @RequestBody final ProductoAdminDto productoAdminDto){

        ProductoAdminDto newProductoAdminDto = IProductoAdminService.createNewProducto(productoAdminDto);
        String selfLink = newProductoAdminDto.getRequiredLink("self").getHref();
        return ResponseEntity.created(URI.create(selfLink))
                .body(newProductoAdminDto);

    }

    @GetMapping
    public PagedModel<ProductoAdminDto> listAllProductos(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10")Integer size,
            @RequestParam(name = "sort", required = false) ProductoAdminService.ORDER order,
            @RequestParam(name = "active", required = false, defaultValue = "false")boolean active,
            @RequestParam(name = "categoria", required = false)Integer categoria){

        if(order == null) order = ProductoAdminService.ORDER.DEFAULT;

        List<ProductoAdminDto> productos = IProductoAdminService.listAllProductos(active,page,size,order, categoria);
        Integer productCount = IProductoAdminService.countProducts(active, categoria);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, productCount);
        PagedModel<ProductoAdminDto> pagedModel = PagedModel.of(productos, pageMetadata);
        pagedModel.add(linkTo(this.getClass()).slash("sortOption").withRel("ordenamiento"));
        return pagedModel;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<RepresentationModel<ProductoAdminDto>> getProducto(
                                        @PathVariable("codigo") Integer codigo){

        ProductoAdminDto productoAdminDto = IProductoAdminService.findProducto(codigo);
        return ResponseEntity.ok(productoAdminDto);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<RepresentationModel<ProductoAdminDto>> deleteProducto(
                                                @PathVariable("codigo") Integer codigo){

        ProductoAdminDto productoAdminDto = IProductoAdminService.productoWithdraw(codigo);
        return ResponseEntity.ok(productoAdminDto);

    }

    @GetMapping(value = {"/sortoption", "/sortOption"})
    public ResponseEntity<Map<String,String>> getSortOrder(){

        Map<String,String> sortOptions = Arrays.stream(ProductoAdminService.ORDER.values())
                .collect(Collectors.toMap(ProductoAdminService.ORDER::toString, ProductoAdminService.ORDER::getValue));

        return ResponseEntity.ok(sortOptions);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<RepresentationModel<ProductoAdminDto>> updateProducto(
                                                        @RequestBody final ProductoAdminDto productoAdminDto,
                                                        @PathVariable("codigo") Integer codigo){

        return ResponseEntity.ok(IProductoAdminService.updateProducto(productoAdminDto,codigo));
    }



}
