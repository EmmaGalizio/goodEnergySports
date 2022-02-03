package emma.galzio.goodenergysports.productos.commons.persistence.repository;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.ImagenProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenProductoEntityRepository extends JpaRepository<ImagenProductoEntity, String> {

    Integer countByProducto(ProductoEntity productoEntity);
    Page<ImagenProductoEntity> findByProducto(ProductoEntity productoEntity, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT url FROM imagen_producto where producto = ?1 AND orden = ?2")
    String getImageUrl(Integer producto, Integer orden);

    ImagenProductoEntity findByProductoAndOrden(ProductoEntity producto, Integer orden);

    List<ImagenProductoEntity> deleteByProductoAndOrdenGreaterThan(ProductoEntity productoEntity, Integer orden);

    @Query("DELETE FROM ImagenProductoEntity im WHERE im.producto=?1 and im.orden>?2")
    @Modifying
    void deleteImagenesProductoOrdenMayorQue(ProductoEntity codigoProducto, Integer orden);
}