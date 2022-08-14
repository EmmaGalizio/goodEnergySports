package emma.galzio.goodenergysports.productos.client.mapper.transferMapper;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import emma.galzio.goodenergysports.productos.client.api.CategoriaRestController;
import emma.galzio.goodenergysports.productos.client.api.ImagenRestController;
import emma.galzio.goodenergysports.productos.client.api.ProductoRestController;
import emma.galzio.goodenergysports.productos.client.transferObject.ProductoDto;
import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoBusinessToTransferMapper implements BusinessToDtoMapper<ProductoDto, Producto> {

    @Autowired
    private ServletContext servletContext;

    @Override
    public ProductoDto mapToDto(Producto business) {
        if(business == null) return null;
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(ProductoDto.class,Producto.class).exclude("imagenes").byDefault().register();
        ProductoDto productoDto = mapperFactory.getMapperFacade().map(business,ProductoDto.class);
        productoDto.setIdCategoria(business.getCategoria().getIdCategoria());
        productoDto.setNombreCategoria(business.getCategoria().getNombre());
        this.setDtoImages(business,productoDto);
        this.setLinks(productoDto);
        return productoDto;
    }

    private void setDtoImages(Producto business, ProductoDto productoDto){
//está repetido porque el Dto de la imagen es el mismo, pero es mejor tenerlos separados
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
        productoDto.setImagenes(imagesList);
    }

    private void setLinks(ProductoDto productoDto) {
        productoDto.add(linkTo(methodOn(ProductoRestController.class)
                                                    .buscarProducto(productoDto.getCodigo())).withSelfRel());
        productoDto.add(linkTo(methodOn(ProductoRestController.class).
                            listarProductos(null,null,null)).withRel("productos"));
        ProductoFilter productoFilter = new ProductoFilter();
        productoFilter.setCategoria(productoDto.getIdCategoria());

        productoDto.add(linkTo(methodOn(ImagenRestController.class)
                            .listarImagenesProducto(productoDto.getCodigo())).withRel("imagenes_link"));
        List<Link> imagenesLinks = productoDto.getImagenes().stream()
                .map((imagen)-> linkTo(methodOn(ImagenRestController.class)
                        .buscarImagenProducto(productoDto.getCodigo(),imagen.getOrden()))
                        .withRel("imagen_link")).collect(Collectors.toList());
        productoDto.add(imagenesLinks);

        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(ProductoRestController.class).listarProductos(null,null,productoFilter));
        UriComponentsBuilder uriBuilder = linkBuilder.toUriComponentsBuilder();
        uriBuilder.replaceQueryParam("categoria", productoDto.getIdCategoria());
        Link productosCategoriaLink = Link.of(UriTemplate.of(uriBuilder.build().toString()),"productos_categoria");
        productoDto.add(productosCategoriaLink);

        productoDto.add(linkTo(methodOn(CategoriaRestController.class)
                .buscarCategoria(productoDto.getIdCategoria())).withRel("categoria"));

        //Faltan linsk stock
    }

    @Override
    public List<ProductoDto> mapAllToDto(List<Producto> businessList) {
        if(businessList == null || businessList.isEmpty()) return null;
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

}
