package emma.galzio.goodenergysports.model.mappers;

import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import emma.galzio.goodenergysports.model.businessModel.Categoria;
import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.persistence.jpaRepository.CategoriaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaMapper implements Mapper<Categoria, CategoriaEntity, CategoriaDto> {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoriaRepository categoriaRepository;


    public Categoria mapFromEntity(CategoriaEntity categoriaEntity){

        Categoria categoria = modelMapper.map(categoriaEntity, Categoria.class);
        return categoria;
    }

    public Categoria mapFromDto(CategoriaDto categoriaDto){

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TypeMap<CategoriaDto, Categoria> typeMap;
        if(modelMapper.getTypeMap(CategoriaDto.class, Categoria.class) == null) {
             typeMap = modelMapper.createTypeMap(categoriaDto, Categoria.class);
            typeMap.addMappings(mapper -> {
                mapper.skip(Categoria::setCategoriaSuperior);
            });
        } else{
            typeMap = modelMapper.getTypeMap(CategoriaDto.class, Categoria.class);
        }

        Categoria categoria = typeMap.map(categoriaDto);
        if(categoriaDto.getIdCategoriaSuperior() != null) {
            CategoriaEntity categoriaSuperior = categoriaRepository.getOne(categoriaDto.getIdCategoriaSuperior());
            categoria.setCategoriaSuperior(this.mapFromEntity(categoriaSuperior));
        }
        if(!categoriaDto.isBajaRegistrada()) categoria.setFechaBaja(null);


        List<CategoriaEntity> categoriaEntityList = categoriaRepository
                                                                .findByCategoriaSuperior(categoria.getIdCategoria());
        categoria.setSubCategorias(
                    categoriaEntityList.stream()
                                        .map((categoriaEntity -> this.mapFromEntity(categoriaEntity)))
                                        .collect(Collectors.toList())
        );

        return categoria;
    }

    public CategoriaEntity mapToEntity(Categoria categoria){

        return modelMapper.map(categoria, CategoriaEntity.class);

    }

    public CategoriaDto mapToDto(Categoria categoria){

        CategoriaDto categoriaDto = modelMapper.map(categoria, CategoriaDto.class);

        if(categoria.getCategoriaSuperior() != null) {
            categoriaDto.setIdCategoriaSuperior(categoria.getCategoriaSuperior().getIdCategoria());
            categoriaDto.setNombreCategoriaSuperior(categoria.getCategoriaSuperior().getNombre());
        }

        if(categoria.getSubCategorias() != null && !categoria.getSubCategorias().isEmpty()){
            categoriaDto.setSubCategorias(
                    categoria.getSubCategorias().stream()
                                                .collect(Collectors.toMap(Categoria::getIdCategoria, Categoria::getNombre))
            );
        }
        if(categoria.getFechaBaja() != null) categoriaDto.setBajaRegistrada(true);
        return categoriaDto;
    }


    public List<Categoria> mapAllFromEntity(List<CategoriaEntity> categorias){

        if(categorias == null) throw new IllegalArgumentException("La lista de entidades no puede ser nula");
        return categorias.stream().map((categoriaEntity -> this.mapFromEntity(categoriaEntity)))
                                                                            .collect(Collectors.toList());

    }

    public List<CategoriaDto> mapAllToDto(List<Categoria> categorias){
        if(categorias == null) throw new IllegalArgumentException("La lista de entidades no puede ser nula");
        return categorias.stream().map(categoria -> this.mapToDto(categoria))
                                                    .collect(Collectors.toList());
    }


}
