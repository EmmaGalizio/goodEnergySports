package emma.galzio.goodenergysports;

import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import emma.galzio.goodenergysports.model.businessModel.Categoria;
import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.model.mappers.CategoriaMapper;
import emma.galzio.goodenergysports.persistence.jpaRepository.CategoriaRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class CategoriaMapperTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    //@Test
    @Transactional
    public void testEntityToBusiness(){

        CategoriaEntity categoriaEntity = categoriaRepository.getOne(1);

        List<CategoriaEntity> categoriaEntityList = categoriaEntity.getSubCategorias();

        Categoria categoria = categoriaMapper.mapFromEntity(categoriaEntity);

        assertThat(categoria).isNotNull();
        assertThat(categoria.getIdCategoria()).isEqualTo(1);
        assertThat(categoria.getSubCategorias()).isNotNull();
        assertThat(categoria.getSubCategorias().isEmpty()).isFalse();
        System.out.println(categoria);

    }

    //@Test
    @Transactional
    public void testDtoToBusiness(){

        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setIdCategoria(1);
        categoriaDto.setNombre("Calzas");
        categoriaDto.setDescripcion("categoria de todas las calzas");

        Categoria categoria = categoriaMapper.mapFromDto(categoriaDto);
        assertThat(categoria).isNotNull();
        System.out.println(categoria);

    }

    //@Test
    @Transactional
    public void testBusinessToEntity(){
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setDescripcion("Categoria de todas las calzas");
        categoria.setNombre("Calzas");

        CategoriaEntity categoriaEntity = categoriaMapper.mapToEntity(categoria);
        assertThat(categoriaEntity).isNotNull();
        assertThat(categoriaEntity.getIdCategoria()).isEqualTo(1);

    }

    //@Test
    @Transactional
    public void testBusinessToDto(){
        Categoria categoria = categoriaMapper.mapFromEntity(categoriaRepository.getOne(1));
        assertThat(categoria).isNotNull();
        CategoriaDto categoriaDto = categoriaMapper.mapToDto(categoria);

        assertThat(categoriaDto).isNotNull();
        assertThat(categoriaDto.getIdCategoria()).isEqualTo(1);
        System.out.println(categoriaDto);
    }

    //@Test
    @Transactional
    public void testValidacionBaja(){
        CategoriaEntity categoriaEntity = categoriaRepository.getOne(9);
        Categoria categoria = categoriaMapper.mapFromEntity(categoriaEntity);
        assertThat(categoria.sePuedeRegistrarBaja()).isFalse();


    }

}
