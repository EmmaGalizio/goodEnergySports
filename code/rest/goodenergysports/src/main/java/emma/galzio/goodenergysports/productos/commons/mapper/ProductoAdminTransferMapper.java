package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.admin.api.ImagenProductoRestController;
import emma.galzio.goodenergysports.productos.admin.api.ProductoAdminRestController;
import emma.galzio.goodenergysports.productos.admin.api.StockProductoAdminRestController;
import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.servlet.ServletContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service("productoAdminTransferMapper")
public class ProductoAdminTransferMapper implements TransferMapper<ProductoAdminDto, Producto> {

    @Autowired
    private ServletContext servletContext;
    @Value("${goodEnergy.WebResourceDirectory}")
    private String resourcesPath;

    @Autowired
    private CategoriaAdminTransferMaper categoriaAdminTransferMaper;
    @Autowired
    private StockAdminTransferMapper stockAdminTransferMapper;
    @Autowired
    @Qualifier("categoriaEntityMapper")
    private EntityMapper<CategoriaEntity, Categoria> categoriaEntityMapper;
    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;

    @Override
    public ProductoAdminDto mapToDto(Producto business){

        if(business == null) throw  new NullPointerException("El producto no puede ser nulo");
        ProductoAdminDto productoAdminDto = new ProductoAdminDto();
        productoAdminDto.setCodigoProducto(business.getCodigo());
        productoAdminDto.setNombre(business.getNombre());
        productoAdminDto.setDescripcion(business.getDescripcion());
        productoAdminDto.setPrecio(business.getPrecio());
        productoAdminDto.setFechaAlta(business.getFechaAlta());
        productoAdminDto.setFechaBaja(business.getFechaBaja());
        //productoAdminDto.setCategoria(categoriaAdminTransferMaper.mapToDto(business.getCategoria()));
        productoAdminDto.setIdCategoria(business.getCategoria().getIdCategoria());
        productoAdminDto.setNombreCategoria(business.getCategoria().getNombre());
        this.setDtoImages(business, productoAdminDto);
        productoAdminDto.add(linkTo(ProductoAdminRestController.class)
                .slash(productoAdminDto.getCodigoProducto()).withSelfRel());
        this.setImageLinks(business, productoAdminDto);
        this.setStockLinks(business, productoAdminDto);
        this.setAllproductosLink(business,productoAdminDto);
        productoAdminDto.setStock(stockAdminTransferMapper.mapAllToDto(business.getStock()));
        return productoAdminDto;
    }

    private void setAllproductosLink(Producto business, ProductoAdminDto productoAdminDto){
        /*Link allProductos = linkTo(ProductoAdminRestController.class).withRel("productos");
        allProductos = Affordances.of(allProductos).afford(HttpMethod.GET).withOutput(List.class)
                .withName("productos")
                .addParameters(QueryParameter.optional("page"),
                        QueryParameter.optional("size"),
                        QueryParameter.optional("active"),
                        QueryParameter.optional("categoria").withValue(business.getCategoria().getIdCategoria().toString()),
                        QueryParameter.optional("sort")).toLink();
        productoAdminDto.add(allProductos);*/

        Link allProductos = linkTo(methodOn(ProductoAdminRestController.class).
                listAllProductos(null,null,null,false, productoAdminDto.getIdCategoria()))
                .withRel("productos");
        productoAdminDto.add(allProductos);
    }

    private void setDtoImages(Producto business, ProductoAdminDto productoAdminDto){

        String rootPath = servletContext.getContextPath().concat("/resources/images/producto");

        List<ImagenProductoDto> imagesList = new LinkedList<>();
        business.getImagenes().sort(ImagenProducto.getOrdenComparator());
        for(ImagenProducto imagenProducto: business.getImagenes()){
            ImagenProductoDto imagenProductoDto = new ImagenProductoDto();
            String imageName = FilenameUtils.getName(imagenProducto.getRutaArchivo());
            imagenProductoDto.setUri(rootPath.concat("/").concat(imageName));
            imagenProductoDto.setOrden(imagenProducto.getOrden());
            imagesList.add(imagenProductoDto);
        }
        productoAdminDto.setImagenes(imagesList);

    }

    private void setImageLinks(Producto producto, ProductoAdminDto productoAdminDto){

        Link allProductImagesLink = linkTo(ImagenProductoRestController.class
                                            , producto.getCodigo()).withRel("todas_imagenes_links");
        productoAdminDto.add(allProductImagesLink);

        List<Link> imagesLinks = new LinkedList<>();
        for(ImagenProducto imagen : producto.getImagenes()){
            Link link = linkTo(methodOn(ImagenProductoRestController.class)
                            .getProductImage(producto.getCodigo(), imagen.getOrden()))
                            .withRel("imagenes_links");
            imagesLinks.add(link);
        }
        productoAdminDto.add(imagesLinks);

    }

    private void setStockLinks(Producto producto, ProductoAdminDto productoAdminDto){

        Link allStokItems = linkTo(methodOn(StockProductoAdminRestController.class).listAllStockForProduct(producto.getCodigo(),false)).withRel("stock");
        productoAdminDto.add(allStokItems);
        List<Link> stockLinksList = new LinkedList<>();
        for(Stock stock: producto.getStock()){
            Link stockTalleLink = linkTo(methodOn(StockProductoAdminRestController.class)
                                        .getTalleStockForProduct(
                                                    producto.getCodigo(),stock.getTalle().getTalle()))
                                .withRel("stock_item");
            stockLinksList.add(stockTalleLink);
        }
        productoAdminDto.add(stockLinksList);
    }

    @Override
    public Producto mapToBusiness(ProductoAdminDto dto){
        if(dto == null) throw new NullPointerException();
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigoProducto());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setFechaAlta(dto.getFechaAlta());
        producto.setFechaBaja(dto.getFechaBaja());
        CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(dto.getIdCategoria());
        Categoria categoria;
        try{
            categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
        }catch(Exception e){ //La unica excepcion que debería saltar es EntityNotFoundException
            DomainException domainException = new DomainException("La categoría indicada no corresponde a una categoría existente");
            domainException.addCause("CATEGORIA","La categoría indicada no corresponde a una categoría existente");
            throw domainException;
        }
        producto.setCategoria(categoria);
        producto.setStock(stockAdminTransferMapper.mapAllToBusiness(dto.getStock()));
        producto.getStock().forEach(stock -> stock.getTalle().setIdCategoriaTalle(producto.getCategoria().getIdCategoria()));
        this.setBusinessImages(producto, dto);

        return producto;
    }

    private void setBusinessImages(Producto business, ProductoAdminDto dto){

        List<ImagenProducto> imagenes = new ArrayList<>();
        for(ImagenProductoDto imagenProductoDto: dto.getImagenes()){
            ImagenProducto imagenProducto = new ImagenProducto();
            imagenProducto.setOrden(imagenProductoDto.getOrden());
            imagenProducto.setUri(imagenProductoDto.getUri());
            imagenProducto.setNombreOriginal(imagenProductoDto.getOriginalName());
            imagenProducto.setBase64(imagenProductoDto.getBase64Image());

            imagenes.add(imagenProducto);
        }
        business.setImagenes(imagenes);
    }

    @Override
    public List<ProductoAdminDto> mapAllToDto(List<Producto> businessList){
        if(businessList == null || businessList.isEmpty()) throw new NullPointerException("La lista de productos no puede estar vaía");
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<Producto> mapAllToBusiness(List<ProductoAdminDto> dtoList){
        if(dtoList == null || dtoList.isEmpty()) throw new NullPointerException("La lista de productos no puede ser nula");

        return dtoList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
