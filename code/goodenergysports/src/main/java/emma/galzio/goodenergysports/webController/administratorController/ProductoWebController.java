package emma.galzio.goodenergysports.webController.administratorController;

import emma.galzio.goodenergysports.controller.administrator.ProductoAdminServiceImpl;
import emma.galzio.goodenergysports.model.TransferModel.ProductoDto;
import emma.galzio.goodenergysports.persistence.jpaRepository.ProductoRepository;
import emma.galzio.goodenergysports.utils.PaginatorWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/producto")
public class ProductoWebController {

    @Autowired
    private ProductoAdminServiceImpl productoAdminService;

    private int pageCount;
    private List<ProductoDto> productos;
    private ProductoDto saveOrUpdateProducto;
    private PaginatorWrapper paginatorWrapper;

    @GetMapping
    public ModelAndView getProductoHome(@RequestParam(required = false) Integer id,
                                        @RequestParam(required = false, defaultValue = "false") boolean newProducto,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "20") Integer size,
                                        HttpServletRequest request){

        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Numero de página solicitada: " + page);
        logger.info("Tamaño de página solicitada: " + size);

        productoAdminService.setProductoWebController(this);
        productoAdminService.innitProductoAdminPage(page, size, id, newProducto);
        ModelAndView modelAndView = new ModelAndView("admin/productos");
        modelAndView.addObject("productos", productos);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("page", page);
        modelAndView.addObject("paginatorWrapper", paginatorWrapper);

        logger.info("Cantidad de páginas en el wrapper: " + paginatorWrapper.getNumberOfPages());
        logger.info("Tamaño de la lista de páginas del wrapper: " + paginatorWrapper.getPageItems().size());
        logger.info("Pagina actual: " + paginatorWrapper.getCurrentPage());
        /*All logic related to create, update and delete isnt implemented yet
        * */


        return modelAndView;
    }

    /*================================
     * From here just getters and setters
     * For code folding select the lines to fold and press ctrl + alt + t
     * ==================================
     * */
    //<editor-fold desc="Getters and Setters">

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<ProductoDto> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDto> productos) {
        this.productos = productos;
    }

    public ProductoDto getSaveOrUpdateProducto() {
        return saveOrUpdateProducto;
    }

    public void setSaveOrUpdateProducto(ProductoDto saveOrUpdateProducto) {
        this.saveOrUpdateProducto = saveOrUpdateProducto;
    }

    public PaginatorWrapper getPaginatorWrapper() {
        return paginatorWrapper;
    }

    public void setPaginatorWrapper(PaginatorWrapper paginatorWrapper) {
        this.paginatorWrapper = paginatorWrapper;
    }
    //</editor-fold>


}
