package emma.galzio.goodenergysports.client;

import emma.galzio.goodenergysports.productos.commons.persistence.filter.ProductoSpecification;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductoTest {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaEntityRepository categoriaRepository;

    @Test
    @Transactional
    public void testOptionalQueryParams(){

        CategoriaEntity categoria = categoriaRepository.getById(2);
        Specification<ProductoEntity> specification = ProductoSpecification.categoriaFilter(Optional.of(categoria))
                .and(ProductoSpecification.precioFilter(Optional.of(new BigDecimal(0)),Optional.of(new BigDecimal(3500))));
        List<ProductoEntity> productos = productoRepository.findAll(specification);
        System.out.println("Productos");
        for(ProductoEntity producto: productos){
            System.out.println("Codigo: " + producto.getCodigoProducto());
            System.out.println("Nombre: "+ producto.getNombre());
            System.out.println("Precio: "+producto.getPrecio());
        }
    }
}
