package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.admin.api.CategoriaAdminRestController;
import emma.galzio.goodenergysports.productos.admin.api.TalleAdminRestController;
import emma.galzio.goodenergysports.productos.admin.api.ProductoAdminRestController;
import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.*;
import java.util.stream.Collectors;

@Service("categoriaAdminTransferMaper")
public class CategoriaAdminTransferMaper implements TransferMapper<CategoriaAdminDto, Categoria> {

    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;
    @Autowired
    private CategoriaEntityMapper categoriaEntityMapper;

    @Override
    public CategoriaAdminDto mapToDto(Categoria business){
        if(business == null) throw new NullPointerException("Categoria must not be null");

        CategoriaAdminDto categoriaAdminDto = new CategoriaAdminDto();
        categoriaAdminDto.setIdCategoria(business.getIdCategoria());
        categoriaAdminDto.setNombre(business.getNombre());
        categoriaAdminDto.setDescripcion(business.getDescripcion());
        categoriaAdminDto.setFechaBaja(business.getFechaBaja());
        if(business.esCategoriaHija()){
            Categoria categoriaSuperior;
            if(business.getCategoriaSuperior() != null) {
                categoriaSuperior = business.getCategoriaSuperior();
            }else{
                categoriaSuperior = categoriaEntityMapper.
                        mapToBusiness(categoriaEntityRepository.findCategoriaSuperior(business.getIdCategoria()));

            }
            CategoriaAdminDto catSuperiorDto = new CategoriaAdminDto();
            catSuperiorDto.setIdCategoria(categoriaSuperior.getIdCategoria());
            catSuperiorDto.setNombre(categoriaSuperior.getNombre());
            categoriaAdminDto.setCategoriaSuperior(catSuperiorDto);
        }
        this.setLinks(categoriaAdminDto);

        return categoriaAdminDto;
    }

    private void setLinks(CategoriaAdminDto categoriaAdminDto){
        categoriaAdminDto.add(linkTo(CategoriaAdminRestController.class)
                                .slash(categoriaAdminDto.getIdCategoria()).withSelfRel());

        if(categoriaAdminDto.getCategoriaSuperior() != null){
            categoriaAdminDto.add(linkTo(CategoriaAdminRestController.class)
                                .slash(categoriaAdminDto.getCategoriaSuperior().getIdCategoria())
                                .withRel("categoria_superior"));
        }
        List<Integer> subCategoriasId = categoriaEntityRepository.findSubCategoriesId(categoriaAdminDto.getIdCategoria());
        List<Link> subCategoriasLinks = subCategoriasId.stream().map(id ->
                                        linkTo(CategoriaAdminRestController.class).slash(id).withRel("sub_categorias"))
                                        .collect(Collectors.toList());
        categoriaAdminDto.add(subCategoriasLinks);

        //categoriaAdminDto.add(linkTo(methodOn(ProductoAdminRestController.class)
        //        .listAllProductos(1,10, ProductoAdminService.ORDER.DEFAULT,true, categoriaAdminDto.getIdCategoria()))
        //        .withRel("productos_categoria"));
        categoriaAdminDto.add(linkTo(methodOn(ProductoAdminRestController.class)
                .listAllProductos(1,10, ProductoAdminService.ORDER.DEFAULT,false,categoriaAdminDto.getIdCategoria()))
                .withRel("productos_categoria"));

        categoriaAdminDto.add(linkTo(methodOn(TalleAdminRestController.class)
                .listAllTalles(categoriaAdminDto.getIdCategoria(),1,10,false)).withRel("talles"));

        categoriaAdminDto.add(linkTo(methodOn(TalleAdminRestController.class)
                .listAllTalles(categoriaAdminDto.getIdCategoria(),1,10,true)).withRel("talles_activos"));
    }

    @Override
    public Categoria mapToBusiness(CategoriaAdminDto dto){
        if(dto == null) throw new NullPointerException("La categoria no puede ser nula");
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(dto.getIdCategoria());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setFechaBaja(dto.getFechaBaja());
        if(dto.getCategoriaSuperior() != null) {
            //categoria.setCategoriaSuperior(this.mapToBusiness(dto.getCategoriaSuperior()));
            categoria.setCategoriaSuperior(categoriaEntityMapper
                        .mapToBusiness(categoriaEntityRepository
                                .getById(dto.getCategoriaSuperior().getIdCategoria())));
        }
        return categoria;
    }

    @Override
    public List<CategoriaAdminDto> mapAllToDto(List<Categoria> businessList){
        if(businessList == null || businessList.isEmpty())
                        throw new NullPointerException("La lista de categorías no puede ser nula ni vacía");

        //HashSet<Integer> computedCategories = new HashSet<>();
        //return this.mapAllToDtoRecursive(computedCategories, businessList);

        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    //La idea es buena para la api de los clientes, para la api administrativa mejor se muestra toda la lista.
    private List<CategoriaAdminDto> mapAllToDtoRecursive(HashSet<Integer> computedCategories, List<Categoria> businessList){

        List<CategoriaAdminDto> categoriasDto = new ArrayList<>();
        for(Categoria categoria: businessList){
            if(computedCategories.contains(categoria.getIdCategoria())){
                continue;
            }
            CategoriaAdminDto categoriaAdminDto = this.mapToDto(categoria);
            categoriasDto.add(categoriaAdminDto);
            computedCategories.add(categoria.getIdCategoria());
            if(categoria.esCategoriaPadre()){
                //categoriaAdminDto.setSubCategorias(this.mapAllToDtoRecursive(computedCategories, categoria.getSubCategorias()));
            }
        }

        return categoriasDto;
    }

    @Override
    public List<Categoria> mapAllToBusiness(List<CategoriaAdminDto> dtoList){

        if(dtoList == null || dtoList.isEmpty())
                    throw new NullPointerException("La lista de categorias no puede ser nula ni vacía");

        return dtoList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
