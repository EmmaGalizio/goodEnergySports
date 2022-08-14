package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service("categoriaAdminService")
public class CategoriaAdminService implements ICategoriaAdminService {

    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;
    @Autowired
    @Qualifier("categoriaEntityMapper")
    private EntityMapper<CategoriaEntity, Categoria> categoriaEntityMapper;
    @Autowired
    @Qualifier("categoriaAdminTransferMaper")
    private TransferMapper<CategoriaAdminDto, Categoria> categoriaTransferMapper;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public List<CategoriaAdminDto> listAllCategories(Integer page, Integer size, boolean active) throws DomainException {

        if(page == null || page < 0) page = 1;
        if(size == null || size <= 0) size = 10;
        Pageable pageable = PageRequest.of(page-1, size);
        List<CategoriaEntity> categoriaEntityList;
        if(!active) {
            categoriaEntityList = categoriaEntityRepository
                    .findByOrderByFechaBajaAscIdCategoriaAsc(pageable).getContent();
        } else{
            categoriaEntityList = categoriaEntityRepository.findByFechaBajaIsNull(pageable).getContent();
        }
        return categoriaTransferMapper
                        .mapAllToDto(categoriaEntityMapper.mapAllToBusiness(categoriaEntityList));
    }

    @Override
    public int countAllCategories(boolean active){

        if(!active) {
            return categoriaEntityRepository.countBy();
        }
        return categoriaEntityRepository.countByFechaBajaIsNull();
    }

    @Override
    @Transactional
    public CategoriaAdminDto getCategoriaById(Integer id){

        try{
            CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(id);
            return categoriaTransferMapper.mapToDto(categoriaEntityMapper.mapToBusiness(categoriaEntity));
        }catch(Exception e){
            DomainException domainException = new DomainException("No existe una categoria con el id indicado");
            domainException.addCause("ID", "No existe una categoria con el id indicado");
            domainException.setStatus(HttpStatus.NOT_FOUND);
            throw domainException;
        }
    }


    @Override
    @Transactional
    public CategoriaAdminDto postNewCategoria(CategoriaAdminDto categoriaAdminDtoaDto) {

        Categoria categoria = categoriaTransferMapper.mapToBusiness(categoriaAdminDtoaDto);

        if(!categoria.esCategoriaValida()) return null;
        if(categoria.getIdCategoria() != null){
            DomainException domainException = new DomainException("El identificador de la nueva categoria debe ser nulo, es un campo autogenerado");
            domainException.addCause("ID", "El identificador de la nueva categoria debe ser un valor autogenerado");
            throw domainException;
        }
        if(categoria.esCategoriaHija() && !categoria.getCategoriaSuperior().estaActiva()){
            DomainException domainException = new DomainException("No se puede registrar una categoría cuyo padre se encuentra inactivo");
            domainException.addCause("CATEGORIA_SUPERIOR", "No es posible registrar una categoría hija de otra categoría que se encuentra inactiva");
            throw domainException;
        }
        CategoriaEntity categoriaEntity = categoriaEntityMapper.mapToEntity(categoria);
        try {
            categoriaEntity = categoriaEntityRepository.save(categoriaEntity);
        }catch(DataIntegrityViolationException e){
            DomainException domainException = new DomainException("Categoría repetida");
            domainException.addCause("NOMBRE_CATEGORIA", "Ya existe una categoría registrada llamada "+categoria.getNombre());
            throw domainException;
        }
        return categoriaTransferMapper.mapToDto(categoriaEntityMapper.mapToBusiness(categoriaEntity));
    }

    @Override
    @Transactional
    public CategoriaAdminDto updateCategoria(CategoriaAdminDto categoriaAdminDto) {

        try{
            CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(categoriaAdminDto.getIdCategoria());
            if(categoriaEntity.getFechaBaja() == null && categoriaAdminDto.getFechaBaja() != null){
                DomainException domainException = new DomainException("Unable to deactivate a category, use http method DELETE instead");
                domainException.addCause("FECHA_BAJA", "No se puede registrar la baja de una categoria mediante este método");
                throw domainException;
            }

        }catch(EntityNotFoundException | NullPointerException e){
            DomainException domainException = new DomainException();
            if(e instanceof EntityNotFoundException)
            domainException.addCause("ID", "El id indicado no corresponde con ninguna categoria registrada");
            if(e instanceof NullPointerException)
                domainException.addCause("ID", "El id debe ser un valor entero positivo");
            throw domainException;
        }
        Categoria categoria = categoriaTransferMapper.mapToBusiness(categoriaAdminDto);
        if(!categoria.esCategoriaValida()) return null;
        if(categoria.estaActiva() && categoria.esCategoriaHija() && !categoria.getCategoriaSuperior().estaActiva()){
            DomainException domainException = new DomainException("Categoria superior inactiva: "+categoria.getCategoriaSuperior().getNombre());
            domainException.addCause("CATEGORIA_SUPERIOR", "No es posible dar de alta una categoría cuya categoría superior se encuentra inactiva");
            throw domainException;
        }
        CategoriaEntity categoriaEntity = categoriaEntityMapper.mapToEntity(categoria);
        categoriaEntity = categoriaEntityRepository.save(categoriaEntity);
        categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
        return categoriaTransferMapper.mapToDto(categoria);
    }

    @Override
    @Transactional
    public CategoriaAdminDto disableCategoria(Integer idCategoria){

        try{
            CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(idCategoria);
            Integer activeProductosCount = productoRepository.countByCategoriaAndFechaBajaIsNull(categoriaEntity);
            if(activeProductosCount == null || activeProductosCount != 0){
                DomainException domainException = new DomainException("No se puede registrar la baja de la categoría debido a que posee productos activos");
                domainException.addCause("PRODUCTO", "No se puede registrar la baja de la categoría debido a que posee productos activos");
                throw domainException;
            }
            Categoria categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
            categoria.registrarBaja();
            categoriaEntityRepository.deactivateCategoria(categoria.getIdCategoria(), categoria.getFechaBaja());
            return categoriaTransferMapper.mapToDto(categoria);

        }catch (EntityNotFoundException e){
            DomainException domainException = new DomainException();
            domainException.addCause("ID", "El id indicado no corresponde con ninguna categoria registrada");
            throw domainException;
        }

    }

    @Override
    public Categoria getCategoriaBusiness(Integer id) {
        if(id == null) return null;
        CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(id);
        try{
            return categoriaEntityMapper.mapToBusiness(categoriaEntity);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException();
            domainException.addCause("CATEGORIA", "No existe una categoría registrada con el ID: "+id);
            throw domainException;
        }
    }
}
