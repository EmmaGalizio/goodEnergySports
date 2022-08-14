package emma.galzio.goodenergysports.productos.client.controller;

import emma.galzio.goodenergysports.productos.admin.controller.ICategoriaAdminService;
import emma.galzio.goodenergysports.productos.client.transferObject.ProductoDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId;
import emma.galzio.goodenergysports.productos.commons.persistence.filter.ProductoSpecification;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.TalleRepository;
import emma.galzio.goodenergysports.productos.commons.utils.ProductoFilter;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductoController  implements IProductoController{

    private final ProductoRepository productoRepository;
    private final ICategoriaAdminService categoriaAdminService;
    private final EntityMapper<CategoriaEntity,Categoria> categoriaEntityMapper;
    private final TalleRepository talleRepository;
    private final EntityMapper<ProductoEntity,Producto> productoEntityMapper;
    private final BusinessToDtoMapper<ProductoDto,Producto> productoBusinessDtoMapper;

    @Override
    @Transactional
    public List<ProductoDto> listarProductos(Integer page, Integer size, ProductoFilter filtros) {

        Sort sort = Sort.by(Sort.Direction.valueOf(filtros.getSort().getDir()),filtros.getSort().getField());
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Categoria categoria = categoriaAdminService.getCategoriaBusiness(filtros.getCategoria());
        CategoriaEntity categoriaEntity = categoria != null ? categoriaEntityMapper.mapToEntity(categoria) : null;
        List<String> talles = this.parseTalles(filtros.getTalles());
        Optional<CategoriaEntity> categoriaOptional = Optional.ofNullable(categoriaEntity);
        Specification<ProductoEntity> productoSpecification = ProductoSpecification
                                            .categoriaFilter(categoriaOptional)
                                            .and(ProductoSpecification.precioFilter((filtros.getPrecioMin() != null ?
                                                                                            Optional.of(new BigDecimal(filtros.getPrecioMin())) :
                                                                                            Optional.empty()),
                                                                                    (filtros.getPrecioMax() != null ?
                                                                                            Optional.of(new BigDecimal(filtros.getPrecioMax())) :
                                                                                            Optional.empty()))
                                            .and(ProductoSpecification.tallesFilter(Optional.ofNullable(talles),
                                                                                    categoriaOptional)))
                                            .and(ProductoSpecification.productosActivos());
        Page<ProductoEntity> productosEntitiesPage = productoRepository.findAll(productoSpecification,pageable);
        List<Producto> productos = productoEntityMapper.mapAllToBusiness(productosEntitiesPage.getContent());
        productos.forEach(Producto::eliminarStockInactivo);
        return productoBusinessDtoMapper.mapAllToDto(productos);
    }

    @Override
    @Transactional
    public ProductoDto buscarProducto(Integer codigo) {
        //Si se utiliza únicamente con la interfaz REST no debería ser null nunca
        if(codigo == null) return null;
        ProductoEntity productoEntity = productoRepository.getById(codigo);
        try{
            Producto producto = productoEntityMapper.mapToBusiness(productoEntity);
            producto.validate();
            producto.eliminarStockInactivo();
            return productoBusinessDtoMapper.mapToDto(producto);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("Producto no encontrado");
            domainException.addCause("CODIGO", "El código indicado no corresponde a un producto registrado");
            throw domainException;
        }
    }

    private List<TalleEntity> parseTalles(String sTalles, CategoriaEntity categoriaEntity){
        if(sTalles == null || sTalles.trim().isEmpty()) return null;
        if(categoriaEntity == null) return null;
        String[] sTallesArray = sTalles.trim().split(",");
        List<String> sTallesList = new LinkedList<>();
        //Podría hacerlo directamente recorriendo el array, pero el repositorio necesita una Collection
        //Por lo que es lo mismo hacerlo así
        for(String talle : sTallesArray){
            sTallesList.add(talle.trim());
        }
        return talleRepository.findByCategoriaAndId_TalleIn(categoriaEntity,sTallesList);
    }
    private List<String> parseTalles(String sTalles){
        if(sTalles == null || sTalles.trim().isEmpty()) return null;
        String[] sTallesArray = sTalles.trim().split(",");
        List<String> sTallesList = new LinkedList<>();
        //Es necesario hacer el trim si o si de todos los elementos.
        //Pordría hacerlo directamente dentro del array y despues instanciar un ArrayList a partir
        //del array pero el constructor utiliza Arrays.copyOf o o algo de eso y es lo mismo o peor
        for(String talle : sTallesArray){
            sTallesList.add(talle.trim());
        }
        return sTallesList;
    }
}
