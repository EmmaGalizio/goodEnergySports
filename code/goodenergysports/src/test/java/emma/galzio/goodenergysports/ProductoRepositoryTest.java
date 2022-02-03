package emma.galzio.goodenergysports;

import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.model.entityModel.ProductoEntity;
import emma.galzio.goodenergysports.persistence.jpaRepository.ProductoRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    //@Test
    @Transactional
    public void testFindByCategoria(){

        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setIdCategoria(1);

        Pageable pageable = PageRequest.of(0,20);
        Page<ProductoEntity> productosPage = productoRepository.findByCategoriaAndFechaBajaIsNull(categoriaEntity, pageable);

        assertThat(productosPage.getTotalElements()).isEqualTo(0);

    }

    //@Test
    @Transactional
    public void testFindAll(){

        List<ProductoEntity> productos = productoRepository.findAll();
        assertThat(productos).isNotNull();
        assertThat(productos.isEmpty()).isFalse();

        productos.forEach(producto ->{
            System.out.println(producto);
        });

    }

    @Test
    @Transactional
    public void testFindAllOrderBy(){

        List<ProductoEntity> productos = productoRepository.findByOrderByFechaBajaAscCodigoProductoAsc(null).getContent();
        assertThat(productos).isNotNull();
        assertThat(productos.isEmpty()).isFalse();



    }


}
