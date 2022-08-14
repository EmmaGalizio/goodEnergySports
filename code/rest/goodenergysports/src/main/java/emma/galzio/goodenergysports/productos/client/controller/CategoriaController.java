package emma.galzio.goodenergysports.productos.client.controller;

import emma.galzio.goodenergysports.productos.client.transferObject.CategoriaDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Data
public class CategoriaController implements ICategoriaController {

    private final CategoriaEntityRepository categoriaRepository;
    private final EntityMapper<CategoriaEntity, Categoria> categoriaEntityMapper;
    private final BusinessToDtoMapper<CategoriaDto,Categoria> categoriaTransferMapper;

    @Override
    @Transactional
    public List<CategoriaDto> listarCategorias(SORT sortOption){

        //Incluir parametro adicional para indicar si quiere ver todas las categorias
        //O solo las categorias padre y las categorías hijas solo dentro de sus categoria padre
        Sort sort = Sort.by(Sort.Direction.valueOf(sortOption.getDir()),sortOption.getField());
        List<CategoriaEntity> categoriasEntities = categoriaRepository.findByFechaBajaIsNull(sort);
        List<Categoria> categorias = categoriaEntityMapper.mapAllToBusiness(categoriasEntities);
        return categoriaTransferMapper.mapAllToDto(categorias);
    }

    @Override
    @Transactional
    public CategoriaDto buscarCategoria(Integer idCategoria) {

        if(idCategoria == null) return null;
        CategoriaEntity categoriaEntity = categoriaRepository.getById(idCategoria);
        try{
            Categoria categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
            return categoriaTransferMapper.mapToDto(categoria);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("Categoría no encontrada");
            domainException.addCause("ID_CATEGORIA", "No se pudo encontrar una categoría con id "+idCategoria);
            throw domainException;
        }
    }

    @Override
    public CategoriaDto buscarCategoria(String nombre) {
        if(nombre == null) return null;
        CategoriaEntity categoriaEntity = categoriaRepository.findByNombreIgnoreCase(nombre);
        try{
            Categoria categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
            return categoriaTransferMapper.mapToDto(categoria);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("Categoría no encontrada");
            domainException.addCause("NOMBRE_CATEGORIA", "No se pudo encontrar una categoría con nombre "+nombre);
            throw domainException;
        }
    }
}
