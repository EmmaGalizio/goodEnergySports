package emma.galzio.goodenergysports.persistence.jpaRepository;

import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.model.entityModel.ProductoEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {

    @Cacheable("activeProductoByCategoria")
    Page<ProductoEntity> findByCategoriaAndFechaBajaIsNull(CategoriaEntity categoriaEntity, Pageable pageable);

    @Cacheable("orderedProducts")
    Page<ProductoEntity> findByOrderByFechaBajaAscCodigoProductoAsc(Pageable pageable);

    @Cacheable("productosCount")
    long count();

}
