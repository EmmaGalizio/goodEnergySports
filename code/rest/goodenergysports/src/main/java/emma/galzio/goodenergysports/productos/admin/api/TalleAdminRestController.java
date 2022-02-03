package emma.galzio.goodenergysports.productos.admin.api;

import emma.galzio.goodenergysports.productos.admin.controller.TalleAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.TalleAdminDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categoria/{idCategoria}/talles")
public class TalleAdminRestController {

    @Autowired
    private TalleAdminService talleAdminService;

    @GetMapping
    public PagedModel<TalleAdminDto> listAllTalles(@PathVariable("idCategoria")Integer idCategoria,
              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
              @RequestParam(value = "active",required = false, defaultValue = "true") boolean active){

        List<TalleAdminDto> talles = talleAdminService.listAllTalleByCategoria(idCategoria, page, size, active);
        Integer tallesCount = talleAdminService.countTalles(idCategoria, active);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page, size, tallesCount);
        return PagedModel.of(talles, pageMetadata);
    }



    @GetMapping("/{talle}")
    public ResponseEntity<TalleAdminDto> findTalle(@PathVariable("idCategoria") Integer idCategoria,
                                                   @PathVariable("talle")String talle){

        TalleAdminDto talleAdminDto = talleAdminService.findTalle(idCategoria, talle);
        return ResponseEntity.ok(talleAdminDto);
    }

    @PostMapping
    public ResponseEntity<TalleAdminDto> saveNewTalle(@PathVariable("idCategoria") Integer idCategoria,
                                                        @RequestBody TalleAdminDto talle){
        TalleAdminDto talleAdminDto = talleAdminService.crearNuevoTalle(talle,idCategoria);
        return ResponseEntity.ok(talleAdminDto);
    }

    @DeleteMapping("/{talle}")
    public ResponseEntity<TalleAdminDto> withDrawTalle(@PathVariable("idCategoria") Integer idCategoria,
                                                       @PathVariable("talle") String talle){
        TalleAdminDto talleAdminDto = talleAdminService.registrarBaja(idCategoria,talle);
        return ResponseEntity.ok(talleAdminDto);
    }

    @PutMapping("{sTalle}")
    public ResponseEntity<TalleAdminDto> updateTalle(@PathVariable("idCategoria") Integer idCategoria,
                                                     @PathVariable("sTalle")String sTalle,
                                                     @RequestBody TalleAdminDto talle){
        TalleAdminDto talleActualizado = talleAdminService.actualizarTalle(talle, idCategoria,sTalle);
        return ResponseEntity.ok(talleActualizado);
    }



}
