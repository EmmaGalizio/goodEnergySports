package emma.galzio.goodenergysports.productos.commons.persistence.repository;


import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TalleRepository extends JpaRepository<TalleEntity, TalleEntityId> {

    List<TalleEntity> findByCategoria(CategoriaEntity categoriaEntity, Pageable pageable);
    List<TalleEntity> findByCategoria(CategoriaEntity categoriaEntity);
    List<TalleEntity> findByCategoriaAndFechaBajaIsNull(CategoriaEntity categoriaEntity, Pageable pageable);
    Integer countByCategoriaAndFechaBajaIsNull(CategoriaEntity categoriaEntity);
    Integer countByCategoria(CategoriaEntity categoriaEntity);

    @Modifying
    @Query("UPDATE TalleEntity t SET t.fechaBaja=?1 WHERE t.id=?2")
    void registrarBaja(LocalDate localDate, TalleEntityId talleEntityId);

}
