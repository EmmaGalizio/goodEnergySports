package emma.galzio.goodenergysports.productos.commons.persistence.repository;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer>,
                                            JpaSpecificationExecutor<ProductoEntity> {

    @Query("SELECT MAX(p.codigoProducto) FROM ProductoEntity p")
    Integer getLastId();


    Page<ProductoEntity> findBy(Pageable pageable);

    Page<ProductoEntity> findByFechaBajaIsNull(Pageable pageable);

    Page<ProductoEntity> findByCategoria(CategoriaEntity categoria, Pageable pageable);
    Page<ProductoEntity> findByCategoriaAndFechaBajaIsNull(CategoriaEntity categoria, Pageable pageable);

    Integer countByCategoriaAndFechaBajaIsNull(CategoriaEntity categoriaEntity);
    Integer countByCategoria(CategoriaEntity categoriaEntity);
    Integer countByFechaBajaIsNull();
    Integer countBy();


}
