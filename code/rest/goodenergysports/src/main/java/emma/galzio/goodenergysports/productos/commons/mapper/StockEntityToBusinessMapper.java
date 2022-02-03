package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.StockEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockEntityToBusinessMapper implements EntityToBusinessMapper<StockEntity, Stock> {

    @Autowired
    private EntityMapper<TalleEntity, Talle> talleEntityMapper;

    @Override
    public Stock mapToBusiness(StockEntity entity) {
        Talle talle = talleEntityMapper.mapToBusiness(entity.getTalle());
        Stock stock = new Stock();
        stock.setTalle(talle);
        stock.setStockDisponible(entity.getStockDisponible());
        stock.setFechaBaja(entity.getFechaBaja());
        return stock;
    }

    @Override
    public List<Stock> mapAllToBusiness(List<StockEntity> entityList) {
    if(entityList == null || entityList.isEmpty()) throw new NullPointerException("La lista de stock no puede estar vacía");
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());

    }
}
