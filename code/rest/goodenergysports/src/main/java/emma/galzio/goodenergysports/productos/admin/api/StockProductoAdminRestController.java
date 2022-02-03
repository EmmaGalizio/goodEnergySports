package emma.galzio.goodenergysports.productos.admin.api;

import emma.galzio.goodenergysports.productos.admin.controller.StockAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.StockAdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/producto/{codigo}/stock")
public class StockProductoAdminRestController {

    @Autowired
    private StockAdminService stockAdminService;

    //Ver documentación con OPEN API 3.0 integrado con spring boot, en baeldung

    @GetMapping
    public PagedModel<StockAdminDto> listAllStockForProduct(
                                        @PathVariable("codigo") Integer codigo,
                            @RequestParam(name = "active", required = false,defaultValue = "false")Boolean active){
        List<StockAdminDto> stock = stockAdminService.listarStockProducto(codigo,active);
        //Si algún dia se decide paginar hay ya está programado el contador, solo hay que agregar los parametros
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(stock.size(),1,stock.size(),1);
        return PagedModel.of(stock, pageMetadata);
    }

    @GetMapping("/{talle}")
    public ResponseEntity<RepresentationModel<StockAdminDto>> getTalleStockForProduct(
                                        @PathVariable("codigo") Integer codigo,
                                        @PathVariable("talle") String talle){
        StockAdminDto stockAdminDto = stockAdminService.buscarStock(codigo, talle);
        return ResponseEntity.ok(stockAdminDto);
    }

    @PostMapping
    public ResponseEntity<StockAdminDto> crearNuevoStock(
                                                    @PathVariable("codigo") Integer codigo,
                                                    @RequestBody StockAdminDto stockAdminDto){
        StockAdminDto stock = stockAdminService.crearNuevoStockItem(codigo,stockAdminDto);
        return ResponseEntity.ok(stock);

    }
    @DeleteMapping("/{talle}")
    public ResponseEntity<RepresentationModel<StockAdminDto>> desactivarStock(
                                                    @PathVariable("codigo") Integer codigo,
                                                    @PathVariable("talle") String talle){
        StockAdminDto stock = stockAdminService.registrarBajaStock(codigo,talle);
        return ResponseEntity.ok(stock);
    }

    @PutMapping("/{talle}")
    public ResponseEntity<RepresentationModel<StockAdminDto>> actualizarStock(
                                                @PathVariable("codigo") Integer codigo,
                                                @PathVariable("talle") String talle,
                                                @RequestBody StockAdminDto stockAdminDto){
        StockAdminDto stock = stockAdminService.actualizarStock(codigo,talle,stockAdminDto);
        return ResponseEntity.ok(stock);

    }




}
