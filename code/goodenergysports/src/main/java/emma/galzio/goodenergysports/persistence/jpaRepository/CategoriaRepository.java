package emma.galzio.goodenergysports.persistence.jpaRepository;

import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {



    @Query("SELECT cat FROM CategoriaEntity cat ORDER BY cat.fechaBaja Asc, cat.idCategoria Asc")
    Page<CategoriaEntity> findAllOrderByFechaBajaAndIdCategoria(Pageable pageable);

    @Cacheable("categorias")
    @Query("SELECT cat FROM CategoriaEntity cat WHERE cat.fechaBaja is null ")
    Page<CategoriaEntity> findAllActiveCategoria(Pageable pageable);

    @Query("SELECT cat FROM CategoriaEntity cat WHERE cat.categoriaSuperior.idCategoria=?1")
    List<CategoriaEntity> findByCategoriaSuperior(Integer categoriaSuperior);

    @Modifying
    @Query("UPDATE CategoriaEntity cat SET cat.fechaBaja=?2 WHERE cat.idCategoria=?1")
    void deactivateCategoria(Integer id, LocalDate fechaBaja);

}
