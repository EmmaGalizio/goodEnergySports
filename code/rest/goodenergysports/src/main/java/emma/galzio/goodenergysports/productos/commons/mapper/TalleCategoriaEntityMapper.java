package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("talleCategoriaEntityMapper")
public class TalleCategoriaEntityMapper implements EntityMapper<TalleEntity, Talle> {

    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;

    @Override
    public TalleEntity mapToEntity(Talle business) {

        if(business == null) throw new DomainException("El talle no puede ser nulo");
        TalleEntityId talleEntityId = new TalleEntityId();
        talleEntityId.setTalle(business.getTalle());
        talleEntityId.setCategoriaProducto(business.getIdCategoriaTalle());

        TalleEntity talleEntity = new TalleEntity();
        talleEntity.setId(talleEntityId);
        talleEntity.setEquivalencia(business.getEquivalencia());
        talleEntity.setFechaBaja(business.getFechaBaja());
        talleEntity.setCategoria(categoriaEntityRepository.getById(business.getIdCategoriaTalle()));

        return talleEntity;
    }

    @Override
    public Talle mapToBusiness(TalleEntity entity) {

        if(entity == null) throw new DomainException("El talle no puede ser nulo");
        Talle talle = new Talle();
        talle.setTalle(entity.getId().getTalle());
        talle.setEquivalencia(entity.getEquivalencia());
        talle.setFechaBaja(entity.getFechaBaja());
        talle.setIdCategoriaTalle(entity.getId().getCategoriaProducto());
        return talle;
    }

    @Override
    public List<TalleEntity> mapAllToEntity(List<Talle> businessList) {
        if(businessList == null || businessList.isEmpty()) throw new DomainException("La lista de talles no puede estar vacía");
        return businessList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<Talle> mapAllToBusiness(List<TalleEntity> entityList) {
        if(entityList == null || entityList.isEmpty()) throw new DomainException("La lista de talles no puede estar vacía");
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
