package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.TalleAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Categoria;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.TalleRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TalleAdminService implements ICategoriaTalleAdminService {

    @Autowired
    private CategoriaEntityRepository categoriaRepository;
    @Autowired
    private TalleRepository talleRepository;
    @Autowired
    private TransferMapper<TalleAdminDto, Talle> talleCategoriaTransferMapper;
    @Autowired
    private EntityMapper<TalleEntity, Talle> talleCategoriaEntityMapper;
    @Autowired
    private EntityMapper<CategoriaEntity,Categoria> categoriaEntityMapper;


    @Override
    @Transactional
    public List<TalleAdminDto> listAllTalleByCategoria(Integer idCategoria, Integer page,
                                                       Integer size, boolean active) {

        if(idCategoria == null){
            throw new NullPointerException("El Id de la categoría no puede ser nulo");
        }
        CategoriaEntity categoriaEntity;
        try{
            categoriaEntity = categoriaRepository.getById(idCategoria);
        } catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe una categoria con el Id indicado");
            domainException.addCause("ID_CATEGORIA", "No existe una categoría con el Id indicado");
            throw domainException;
        }
        List<TalleEntity> tallesEntiy;
        Pageable pageable = PageRequest.of(page -1, size);
        if(active){
            tallesEntiy = talleRepository.findByCategoriaAndFechaBajaIsNull(categoriaEntity, pageable);
        } else{
            tallesEntiy = talleRepository.findByCategoria(categoriaEntity, pageable);
        }
        List<Talle> talles = talleCategoriaEntityMapper.mapAllToBusiness(tallesEntiy);
        return talleCategoriaTransferMapper.mapAllToDto(talles);
    }

    public Integer countTalles(Integer idCategoria, boolean active){
        if(idCategoria == null){
            throw new NullPointerException("El Id de la categoría no puede ser nulo");
        }
        CategoriaEntity categoriaEntity;
        try{
            categoriaEntity = categoriaRepository.getById(idCategoria);
        } catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe una categoria con el Id indicado");
            domainException.addCause("ID_CATEGORIA", "No existe una categoría con el Id indicado");
            throw domainException;
        }

        if(active){
            return talleRepository.countByCategoriaAndFechaBajaIsNull(categoriaEntity);
        }
        return talleRepository.countByCategoria(categoriaEntity);

    }

    public TalleAdminDto findTalle(Integer idCategoria, String sTalle){

        if(idCategoria == null) throw new DomainException("La categoría no puede ser nula");
        if(sTalle == null || sTalle.trim().isEmpty()) throw new DomainException("Es necesario especificar el talle");

        TalleEntityId talleEntityId = new TalleEntityId();
        talleEntityId.setCategoriaProducto(idCategoria);
        talleEntityId.setTalle(sTalle.trim().toUpperCase());

        TalleEntity talleEntity;
        try{
            talleEntity = talleRepository.getById(talleEntityId);
        } catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("El talle indicado no existe");
            domainException.addCause("ID_TALLE", "No se pudo encontrar el talle " + sTalle + "para la categoría indicada");
            throw domainException;
        }

        Talle talle = talleCategoriaEntityMapper.mapToBusiness(talleEntity);
        return talleCategoriaTransferMapper.mapToDto(talle);
    }

    public TalleAdminDto crearNuevoTalle(TalleAdminDto talleAdminDto, Integer idCategoria){

        CategoriaEntity categoriaEntity = categoriaRepository.getById(idCategoria);
        Categoria categoria;
        try{
            categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
        } catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe la categoría indicada");
            domainException.addCause("CATEGORIA", "La categoría indicada no corresponde a una registrada");
            throw domainException;
        }
        if (!categoria.estaActiva()){
            DomainException domainException = new DomainException("No es posible registrar un nuevo talle para una categoría inactiva");
            domainException.addCause("CATEGORIA", "No es posible registrar un nuevo talle para una categoria inactiva");
            throw domainException;
        }
        Talle talle = talleCategoriaTransferMapper.mapToBusiness(talleAdminDto);
        talle.validate();
        TalleEntity talleEntity = talleRepository.save(talleCategoriaEntityMapper.mapToEntity(talle));
        talle = talleCategoriaEntityMapper.mapToBusiness(talleEntity);
        return talleCategoriaTransferMapper.mapToDto(talle);

    }

    public TalleAdminDto registrarBaja(Integer idCategoria, String sTalle){

        CategoriaEntity categoriaEntity = categoriaRepository.getById(idCategoria);
        try{
            Categoria categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe una categoría registrada con el identificador indicado");
            domainException.addCause("CATEGORIA", "No existe una categoría registrada con el identificador indicado");
            throw domainException;
        }
        try{
            TalleEntityId auxId = new TalleEntityId(sTalle.toUpperCase(),idCategoria);
            TalleEntity talleEntity = talleRepository.getById(auxId);
            Talle talle = talleCategoriaEntityMapper.mapToBusiness(talleEntity);
            talle.registrarBaja();
            talleRepository.registrarBaja(talle.getFechaBaja(),talleEntity.getId());
            return talleCategoriaTransferMapper.mapToDto(talle);
        }catch (EntityNotFoundException e){
            DomainException domainException = new DomainException("El talle "+sTalle+" no se encuentra registrado para la categoria indicada");
            domainException.addCause("TALLE", "El talle "+sTalle+" no se encuentra registrado para la categoria indicada");
            throw domainException;
        }
    }

    public TalleAdminDto actualizarTalle(TalleAdminDto talleAdminDto, Integer idCategoria, String sTalle){

        Talle talle = talleCategoriaTransferMapper.mapToBusiness(talleAdminDto);
        talle.validate();
        TalleEntityId talleEntityId = new TalleEntityId(sTalle,idCategoria);
        TalleEntity talleEntity = talleRepository.getById(talleEntityId);
        Talle tallePersistido;
        try{
            tallePersistido = talleCategoriaEntityMapper.mapToBusiness(talleEntity);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("El talle "+sTalle+" no se encuentra registrado para la categoria indicada");
            domainException.addCause("TALLE", "El talle "+sTalle+" no se encuentra registrado para la categoria indicada");
            throw domainException;
        }
        CategoriaEntity categoriaEntity = categoriaRepository.getById(idCategoria);
        Categoria categoria;
        try{
            categoria = categoriaEntityMapper.mapToBusiness(categoriaEntity);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe una categoría registrada con el identificador indicado");
            domainException.addCause("CATEGORIA", "No existe una categoría registrada con el identificador indicado");
            throw domainException;
        }
        if(talle.estaActivo() && !tallePersistido.estaActivo() && !categoria.estaActiva()){
            DomainException domainException = new DomainException("No es posible activar el talle "+sTalle+" debido a que la categoria a la que corresponde se encuentra registrada de baja");
            domainException.addCause("CATEGORIA", "No es posible activar el talle "+sTalle+" debido a que la categoria a la que corresponde se encuentra registrada de baja, por favor primero active la categoria correspondiente");
            throw domainException;
        }
        if(!talle.estaActivo() && tallePersistido.estaActivo()){
            DomainException domainException = new DomainException("No es posible registrar la baja de un talle mediante este método");
            domainException.addCause("TALLE", "No es posible registrar la baja de un talle utilizando este método");
            throw domainException;
        }
        talleRepository.save(talleCategoriaEntityMapper.mapToEntity(talle));
        return talleCategoriaTransferMapper.mapToDto(talle);

    }

}
