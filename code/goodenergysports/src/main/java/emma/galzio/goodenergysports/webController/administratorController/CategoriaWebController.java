package emma.galzio.goodenergysports.webController.administratorController;

import ch.qos.logback.core.encoder.EchoEncoder;
import emma.galzio.goodenergysports.controller.administrator.CategoriaAdminService;
import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaWebController {

    @Autowired
    private CategoriaAdminService categoriaAdminService;

    private List<CategoriaDto> categorias;


    @GetMapping()
    public ModelAndView getCategoriaHome(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false, defaultValue = "false") boolean newCategoria,
                                         HttpServletRequest request){

        categoriaAdminService.setWebController(this);
        categoriaAdminService.displayAllCategories();

        ModelAndView modelAndView = new ModelAndView("categorias");
        modelAndView.addObject("categorias", this.categorias);

        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String error = null;
        String success = null;

        if(inputFlashMap != null){
            error = (String) inputFlashMap.get("error");
            success = (String) inputFlashMap.get("success");
        }

       if (error != null && !error.trim().isEmpty()) modelAndView.addObject("error", error);
       if (success != null && !success.trim().isEmpty()) modelAndView.addObject("success", success);

        if(id != null){
            try{
                modelAndView.addObject("updateOrSavecategoria",categoriaAdminService.searchCategoriaById(id));

            } catch(Exception e){
                modelAndView.addObject("error", e.getMessage());
            }
        } else{
            try{
                modelAndView.addObject("updateOrSavecategoria",new CategoriaDto());
            } catch (Exception e){
                modelAndView.addObject("error", e.getMessage());
            }
        }
        modelAndView.addObject("newCategoria",newCategoria);
        modelAndView.addObject("categoria", new CategoriaDto());

        return modelAndView;
    }

    @PostMapping()
    @Transactional
    public RedirectView saveOrUpdateCategoria(@ModelAttribute CategoriaDto categoria,
                                              RedirectAttributes redirectAttributes){

        try{
            redirectAttributes.addFlashAttribute("success",
                                            categoriaAdminService.saveOrUpdateCategoria(categoria));

        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return new RedirectView("/admin/categoria", true);
    }

    @RequestMapping(value = "/baja", method = RequestMethod.DELETE)
    @Transactional
    public RedirectView deleteCategoria(@RequestParam(required = false) Integer id,
                                        RedirectAttributes redirectAttributes){

        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Llego al metodo deleteCategoria");
        CategoriaDto categoria = new CategoriaDto();
        categoria.setIdCategoria(id);


        categoriaAdminService.setWebController(this);
        try{
            categoriaAdminService.deleteCategoria(categoria);
            redirectAttributes.addFlashAttribute("success", "Se registró con éxito la baja de la categoría");

        } catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/admin/categoria");

    }


    /*
    * From here just Getters and Setters
    * */
    public List<CategoriaDto> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDto> categorias) {
        this.categorias = categorias;
    }
}
