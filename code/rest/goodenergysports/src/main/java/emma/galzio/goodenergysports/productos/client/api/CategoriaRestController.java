package emma.galzio.goodenergysports.productos.client.api;

import emma.galzio.goodenergysports.productos.client.controller.ICategoriaController;
import emma.galzio.goodenergysports.productos.client.transferObject.CategoriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController {

    @Autowired
    private ICategoriaController categoriaController;

    @GetMapping
    public PagedModel<CategoriaDto> listarCategorias(
            @RequestParam(value = "sort", required = false,defaultValue = "DEFAULT") ICategoriaController.SORT sort){
        List<CategoriaDto> categorias = categoriaController.listarCategorias(sort);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(categorias.size(),1,categorias.size(),1);
        PagedModel<CategoriaDto> categoriaDtoPagedModel = PagedModel.of(categorias,pageMetadata);
        return categoriaDtoPagedModel;
    }

    @GetMapping("/{id}")
    public CategoriaDto buscarCategoria(@PathVariable("id") Integer id){
        return categoriaController.buscarCategoria(id);
    }

    @GetMapping("/nombre/{nombre}")
    public CategoriaDto buscarCategoria(@PathVariable("nombre")String nombre){
        return categoriaController.buscarCategoria(nombre);
    }




}
