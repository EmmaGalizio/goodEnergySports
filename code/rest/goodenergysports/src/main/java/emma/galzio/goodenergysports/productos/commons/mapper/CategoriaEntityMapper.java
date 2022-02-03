package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service("categoriaEntityMapper")
public class CategoriaEntityMapper implements EntityMapper<CategoriaEntity, Categoria>{

    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;

    @Override
    public CategoriaEntity mapToEntity(Categoria business){
        if(business == null) throw new NullPointerException("Categoria must not be null");
        business.esCategoriaValida();

        CategoriaEntity categoriaEntity = new CategoriaEntity();
        if(business.getIdCategoria() != null) categoriaEntity.setIdCategoria(business.getIdCategoria());
        categoriaEntity.setNombre(business.getNombre());
        categoriaEntity.setDescripcion(business.getDescripcion());
        categoriaEntity.setFechaBaja(business.getFechaBaja());

        if(business.esCategoriaHija()){
            if(business.getCategoriaSuperior() != null) {
                categoriaEntity.setCategoriaSuperior(this.mapToEntity(business.getCategoriaSuperior()));
            } else{
                categoriaEntity
                        .setCategoriaSuperior(categoriaEntityRepository
                                .findCategoriaSuperior(business.getIdCategoriaSuperior()));
            }
        }

        return categoriaEntity;
    }

    @Override
    public Categoria mapToBusiness(CategoriaEntity entity){
        if(entity == null) throw new NullPointerException("Categoria must not be null");

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(entity.getIdCategoria());
        categoria.setNombre(entity.getNombre());
        categoria.setDescripcion(entity.getDescripcion());
        categoria.setFechaBaja(entity.getFechaBaja());
        if(entity.getCategoriaSuperior() != null){
            categoria.setIdCategoriaSuperior(entity.getCategoriaSuperior().getIdCategoria());
        }
        if(entity.getSubCategorias() != null && entity.getSubCategorias().size() != 0){
            categoria.setSubCategorias(this.mapAllToBusiness(entity.getSubCategorias()));
        }
        return categoria;
    }

    @Override
    public List<CategoriaEntity> mapAllToEntity(List<Categoria> businessList){
        if(businessList == null || businessList.isEmpty()) throw new NullPointerException("Categorias list must not be null or empty");

        return businessList.stream().map(this::mapToEntity).collect(Collectors.toList());

    }

    @Override
    public List<Categoria> mapAllToBusiness(List<CategoriaEntity> entityList){
        if(entityList == null || entityList.isEmpty()) throw new NullPointerException("Categoria must not be null or empty");

        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());

    }
}
