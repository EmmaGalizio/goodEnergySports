package emma.galzio.goodenergysports.model.mappers;

import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import emma.galzio.goodenergysports.model.TransferModel.ProductoDto;
import emma.galzio.goodenergysports.model.businessModel.Categoria;
import emma.galzio.goodenergysports.model.businessModel.Producto;
import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.model.entityModel.ProductoEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoMapper implements Mapper<Producto, ProductoEntity, ProductoDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("categoriaMapper")
    private Mapper<Categoria, CategoriaEntity, CategoriaDto> categoriaMapper;


    @Override
    public Producto mapFromDto(ProductoDto dto) {
        return null;
    }

    @Override
    public Producto mapFromEntity(ProductoEntity entity) {
        Producto producto = modelMapper.map(entity, Producto.class );
        return producto;
    }

    @Override
    public ProductoEntity mapToEntity(Producto bussines) {
        return null;
    }

    @Override
    public ProductoDto mapToDto(Producto bussines) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TypeMap<Producto, ProductoDto> typeMap;

        if(modelMapper.getTypeMap(Producto.class, ProductoDto.class) == null) {
            typeMap = modelMapper.createTypeMap(bussines, ProductoDto.class);
            typeMap.addMappings(mapper -> {
                mapper.skip(ProductoDto::setImagenes);
                mapper.skip(ProductoDto::setCategoria);
            });
        } else{
            typeMap = modelMapper.getTypeMap(Producto.class, ProductoDto.class);
        }
        CategoriaDto categoriaDto = categoriaMapper.mapToDto(bussines.getCategoria());
        ProductoDto productoDto = typeMap.map(bussines);
        productoDto.setCategoria(categoriaDto);

        productoDto.setImagenes(bussines.getImagenes().stream()
                .map((imagenProducto -> imagenProducto.getUrlImagen())).collect(Collectors.toList()));

        return productoDto;

    }

    @Override
    public List<Producto> mapAllFromEntity(List<ProductoEntity> entities) throws NullPointerException, IllegalArgumentException {
        if(entities == null) throw new NullPointerException("La lista de entidades no puede ser nula");
        if(entities.isEmpty()) throw new IllegalArgumentException("La lista de entidades no puede estar vacía");
        return entities.stream().map((categoriaEntity) -> this.mapFromEntity(categoriaEntity))
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductoDto> mapAllToDto(List<Producto> bussines) {
        if(bussines == null) throw new NullPointerException("La lista de entidades no puede ser nula");
        if(bussines.isEmpty()) throw new IllegalArgumentException("La lista de entidades no puede estar vacía");
        return bussines.stream().map((categoriaBussines) -> this.mapToDto(categoriaBussines))
                .collect(Collectors.toList());
    }
}
