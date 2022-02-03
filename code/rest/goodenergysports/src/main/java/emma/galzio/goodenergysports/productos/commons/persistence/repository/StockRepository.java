package emma.galzio.goodenergysports.productos.commons.persistence.repository;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.StockEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.StockEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<StockEntity, StockEntityId> {

    //No lo hago con paginación porque nunca va a llegar a los 20 items, calculo que no pasará de los 5 talles
    List<StockEntity> findByProducto(ProductoEntity productoEntity);
    List<StockEntity>findByProductoAndAndFechaBajaIsNull(ProductoEntity productoEntity);
    Integer countByProducto(ProductoEntity productoEntity);
    Integer countByProductoAndFechaBajaIsNull(ProductoEntity producto);


}
