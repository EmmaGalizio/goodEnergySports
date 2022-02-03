package emma.galzio.goodenergysports.controller.administrator;

import emma.galzio.goodenergysports.model.TransferModel.ProductoDto;
import emma.galzio.goodenergysports.model.businessModel.Producto;
import emma.galzio.goodenergysports.model.entityModel.ProductoEntity;
import emma.galzio.goodenergysports.model.mappers.Mapper;
import emma.galzio.goodenergysports.persistence.jpaRepository.ProductoRepository;
import emma.galzio.goodenergysports.utils.PaginatorWrapper;
import emma.galzio.goodenergysports.webController.administratorController.ProductoWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoAdminServiceImpl {

    private static final String BASE_URL = "/admin/producto";
    private Logger logger = LoggerFactory.getLogger(ProductoAdminServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    @Qualifier("productoMapper")
    private Mapper<Producto, ProductoEntity, ProductoDto> mapper;

    private ProductoWebController productoWebController;
    private List<Producto> productos;

    public void innitProductoAdminPage(Integer page, Integer size, Integer id, boolean newProducto)
                                                throws IllegalArgumentException, NullPointerException{

        this.innitWebControllerAttributes();
        productos = this.getAllProductos(page, size);

        productoWebController.setProductos(mapper.mapAllToDto(productos));
        int numberOfPages = this.calculateNumberOfPages(size);
        productoWebController.setPageCount(numberOfPages);
        productoWebController.setPaginatorWrapper(this.setUpPaginatorWrapper(numberOfPages, page, size));

        if(id != null && !newProducto){
            Producto productoMod = this.findProductoToModify(id);
            productoWebController.setSaveOrUpdateProducto(mapper.mapToDto(productoMod));
        }
        if(id == null && newProducto){
            //This new ProductoDto object is created to be passed via model attribute in the view in case
            //that the new producto button was pressed;
            productoWebController.setSaveOrUpdateProducto(new ProductoDto());
        }

        /*
        * Falta toda la lógica para manejar los new, update y delete.
        * */

    }

    private PaginatorWrapper setUpPaginatorWrapper(Integer numberOfPages, Integer currentPage, Integer size){

        return new PaginatorWrapper(numberOfPages, currentPage, BASE_URL, size);

    }

    private int calculateNumberOfPages(Integer size){
        long count = productoRepository.count();
        Long lCount = new Long(count);
        int iCount = lCount.intValue();


        int numberOfPages = (iCount % size == 0) ? iCount/size : (iCount/size)+1;

        return numberOfPages;
    }

    private Producto findProductoToModify(Integer id) throws IllegalArgumentException {

        ProductoEntity productoEntity = productoRepository.getOne(id);
        if(productoEntity == null) throw new IllegalArgumentException("El codigo proporcionado no corresponde con un producto registrado");
        return mapper.mapFromEntity(productoEntity);
    }

    private List<Producto> getAllProductos(Integer page, Integer size) throws IllegalArgumentException, NullPointerException{

        Pageable pageable = PageRequest.of(page-1, size);
        List<ProductoEntity> productosEntity = productoRepository
                                            .findByOrderByFechaBajaAscCodigoProductoAsc(pageable)
                                            .getContent();
        return mapper.mapAllFromEntity(productosEntity);

    }

    private void innitWebControllerAttributes(){
        productoWebController.setSaveOrUpdateProducto(null);

    }


    /*================================
    * From here just getters and setters
    * For code folding select the lines to fold and press ctrl + alt + t
    * ==================================
    * */
    //<editor-fold desc="Getters and Setters">
    public ProductoRepository getProductoRepository() {
        return productoRepository;
    }

    public void setProductoRepository(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoWebController getProductoWebController() {
        return productoWebController;
    }

    public void setProductoWebController(ProductoWebController productoWebController) {
        this.productoWebController = productoWebController;
    }

    //</editor-fold>


}
