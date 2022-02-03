package emma.galzio.goodenergysports.productos.admin.api;

import emma.galzio.goodenergysports.productos.admin.controller.ICategoriaAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categoria")
public class CategoriaAdminRestController {

    @Autowired
    @Qualifier("categoriaAdminService")
    private ICategoriaAdminService categoriaAdminService;


    //Se puede hacer con un assembler para que el método quede mucho más chico.
    //Se delegaría la creacion de todos los links a una clase que se encargue exclusivamente de eso
    @GetMapping
    public PagedModel<CategoriaAdminDto> listAllCategories(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "active", required = false, defaultValue = "false")boolean active) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 10;


        List<CategoriaAdminDto> categoriaAdminDtos = categoriaAdminService.listAllCategories(page, size,active);
         PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, categoriaAdminService.countAllCategories(active));
        PagedModel<CategoriaAdminDto> pagedModel = PagedModel.of(categoriaAdminDtos, pageMetadata);
        return pagedModel;
    }

    @GetMapping("/{id}")
    public RepresentationModel<CategoriaAdminDto> getById(@PathVariable("id") Integer id){
        return categoriaAdminService.getCategoriaById(id);
    }

    /*
    * ResponseEntity da un control mucho mayor sobre la respuesta que RepresentationModel y sus derivadas
    * Permite setear headers y el status code
    * Retornar CategoriaAdminDto o RepresentationModel<CategoriaAdminDto> en principio deberia ser lo mismo
    * */


    @GetMapping("/count")
    public @ResponseBody Integer getCount(@RequestParam(name = "active", required = false, defaultValue = "true") boolean active){
        return categoriaAdminService.countAllCategories(active);
    }

    @PostMapping
    public ResponseEntity<RepresentationModel<CategoriaAdminDto>> newCategoria(@RequestBody CategoriaAdminDto categoria){

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaAdminService.postNewCategoria(categoria));

    }

    @PutMapping
    public ResponseEntity<RepresentationModel<CategoriaAdminDto>> modifyCategoria(@RequestBody CategoriaAdminDto categoriaAdminDto){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAdminService.updateCategoria(categoriaAdminDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RepresentationModel<CategoriaAdminDto>> deactivateCategoria(@PathVariable(name = "id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaAdminService.disableCategoria(id));
    }


}
