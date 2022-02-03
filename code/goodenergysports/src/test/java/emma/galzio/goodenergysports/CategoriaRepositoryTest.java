package emma.galzio.goodenergysports;

import static org.assertj.core.api.Assertions.*;

import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.persistence.jpaRepository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //@Test
    @Transactional
    public void testCategoriaRepoFindAll(){

        List<CategoriaEntity> categorias = categoriaRepository.findAll();

        assertThat(categorias.isEmpty()).isFalse();
        categorias.forEach(categoria ->{
            System.out.println(categoria);

        });
    }

    //@Test
    public void testCategoriaRepoCount(){
        long countCategoria = categoriaRepository.count();

        assertThat(new Long(countCategoria)).isNotZero();
        System.out.println("Cantidad de Categorias: " + countCategoria);
    }

    //@Test
    public void findAllSortedTest(){


        Sort sort = Sort.by(Sort.Direction.ASC, "idCategoria").and(Sort.by(Sort.Direction.ASC, "fechaBaja"));
        Pageable pageable = PageRequest.of(0, 20);
        Page<CategoriaEntity> categoriaEntityPage = categoriaRepository
                                                .findAllOrderByFechaBajaAndIdCategoria(pageable);
        List<CategoriaEntity> categorias = categoriaEntityPage.get().collect(Collectors.toList());
        assertThat(categorias.isEmpty()).isFalse();
        System.out.println("Categorias Ordenadas");
        categorias.forEach((categoria) -> {
            System.out.println(categoria);
        });

    }
    //@Test
    public void findAllByCategoriaSuperiorTest(){
        List<CategoriaEntity> categoriaEntityList = categoriaRepository.findByCategoriaSuperior(1);
        assertThat(categoriaEntityList).isNotNull();
        assertThat(categoriaEntityList.size()).isEqualTo(3);
    }

}
