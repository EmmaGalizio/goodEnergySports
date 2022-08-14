package emma.galzio.goodenergysports.admin;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaEntityRepository repository;

    //@Test
    public void findAllCategoriesTest(){

        Pageable pageable = PageRequest.of(0,9);
        List<CategoriaEntity> categoriaEntities = repository.findByOrderByFechaBajaAscIdCategoriaAsc(pageable).getContent();

        categoriaEntities.forEach(categoriaEntity -> {
            System.out.println("Nombre: " + categoriaEntity.getNombre());
        });
    }

    //@Test
    public void countAllCategories(){

        Integer count = repository.countBy();
        System.out.println("Cantidad de categorias: " + count);

    }

    //@Test
    public void listSubCategoriesIds(){
        List<Integer> ids = repository.findSubCategoriesId(1);
        System.out.println(ids);

    }

    //@Test
    public void testListAllCategories(){
        Pageable pageable = PageRequest.of(0,10);
        List<CategoriaEntity> categoriaEntityList = repository.findByOrderByFechaBajaAscIdCategoriaAsc(pageable).getContent();
        System.out.println(categoriaEntityList);
    }

    //@Test
    public void testFindCategoriaSuperior(){
        CategoriaEntity categoriaEntity = repository.findCategoriaSuperior(2);
        System.out.println("Nombre: " + categoriaEntity.getNombre() + " ID: " + categoriaEntity.getIdCategoria());
    }


    @Transactional
    @Test
    public void testNombreUniqueConstraintViolation(){
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setNombre("Calzas");
        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(
                ()-> repository.save(categoriaEntity)
        );

    }
}
