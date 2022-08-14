package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.admin.api.CategoriaAdminRestController;
import emma.galzio.goodenergysports.productos.admin.api.TalleAdminRestController;
import emma.galzio.goodenergysports.productos.admin.api.ProductoAdminRestController;
import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.TalleAdminDto;
import emma.galzio.goodenergysports.productos.client.api.ProductoRestController;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalleCategoriaTransferMapper implements TransferMapper<TalleAdminDto, Talle> {

    @Override
    public TalleAdminDto mapToDto(Talle business) {
        if(business == null) throw new DomainException("El talle no puede ser nulo");

        TalleAdminDto talleAdminDto = new TalleAdminDto();
        talleAdminDto.setTalle(business.getTalle());
        talleAdminDto.setEquivalencia(business.getEquivalencia());
        talleAdminDto.setFechaBaja(business.getFechaBaja());
        this.addDtoLinks(business, talleAdminDto);
        return talleAdminDto;
    }

    private void addDtoLinks(Talle business, TalleAdminDto dto){
        dto.add(linkTo(methodOn(TalleAdminRestController.class, business.getIdCategoriaTalle(), business.getTalle()).findTalle(business.getIdCategoriaTalle(), business.getTalle())).withSelfRel());
        dto.add(linkTo(CategoriaAdminRestController.class).slash(business.getIdCategoriaTalle()).withRel("categoria"));
        ProductoFilter productoFilter = new ProductoFilter();
        productoFilter.setCategoria(business.getIdCategoriaTalle());
        //Agregar link a metodo con un POJO como query parameter
        WebMvcLinkBuilder linkBuilder = linkTo(methodOn(ProductoAdminRestController.class).listAllProductos(null,null,false,productoFilter));
        UriComponentsBuilder uriBuilder = linkBuilder.toUriComponentsBuilder();
        uriBuilder.replaceQueryParam("categoria", business.getIdCategoriaTalle());
        Link productosCategoriaLink = Link.of(UriTemplate.of(uriBuilder.build().toString()),"productos_categoria");
        dto.add(productosCategoriaLink);
    }


    @Override
    public Talle mapToBusiness(TalleAdminDto dto) {
        if(dto == null) throw new DomainException("El talle no puede ser nulo");
        Talle talle = new Talle();
        talle.setTalle(dto.getTalle() != null ? dto.getTalle().trim().toUpperCase(): null);
        talle.setEquivalencia(dto.getEquivalencia());
        talle.setFechaBaja(dto.getFechaBaja());
        return talle;
    }

    @Override
    public List<TalleAdminDto> mapAllToDto(List<Talle> businessList) {
        if(businessList == null || businessList.isEmpty()) throw new DomainException("La lista con talles no puede estar vacía");
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<Talle> mapAllToBusiness(List<TalleAdminDto> dtoList) {
        if(dtoList == null || dtoList.isEmpty()) throw new DomainException("La lista con talles no puede estar vacía");
        return dtoList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
