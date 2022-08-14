package emma.galzio.goodenergysports.productos.client.mapper.transferMapper;

import emma.galzio.goodenergysports.productos.client.api.CategoriaRestController;
import emma.galzio.goodenergysports.productos.client.api.ProductoRestController;
import emma.galzio.goodenergysports.productos.client.transferObject.CategoriaDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.hateoas.UriTemplate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaBusinessToTransferMapper implements BusinessToDtoMapper<CategoriaDto, Categoria> {
    @Override
    public CategoriaDto mapToDto(Categoria business) {

        if(business == null) return null;
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(CategoriaDto.class,Categoria.class).byDefault().register();
        CategoriaDto categoriaDto = mapperFactory.getMapperFacade().map(business, CategoriaDto.class);
        this.setLinks(categoriaDto);

        return categoriaDto;
    }

    private void setLinks(CategoriaDto dto){

        dto.add(linkTo(methodOn(CategoriaRestController.class).buscarCategoria(dto.getIdCategoria())).withSelfRel());
        ProductoFilter productoFilter = new ProductoFilter();
        productoFilter.setCategoria(dto.getIdCategoria());
        //Agregar link a metodo con un POJO como query parameter
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(ProductoRestController.class).listarProductos(null,null,productoFilter));
        UriComponentsBuilder uriBuilder = linkBuilder.toUriComponentsBuilder();
        uriBuilder.replaceQueryParam("categoria", dto.getIdCategoria());
        Link productosCategoriaLink = Link.of(UriTemplate.of(uriBuilder.build().toString()),"productos_categoria");
        dto.add(productosCategoriaLink);


    }

    @Override
    public List<CategoriaDto> mapAllToDto(List<Categoria> businessList) {
        if(businessList == null || businessList.isEmpty()) return null;
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
