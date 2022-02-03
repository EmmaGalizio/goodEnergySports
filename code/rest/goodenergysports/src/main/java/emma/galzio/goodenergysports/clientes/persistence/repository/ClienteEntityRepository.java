package emma.galzio.goodenergysports.clientes.persistence.repository;

import emma.galzio.goodenergysports.clientes.persistence.entity.ClienteEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.TipoDocumentoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteEntityRepository extends JpaRepository<ClienteEntity, Integer> {

    ClienteEntity findByTipoDocumentoAndNroDocumento(TipoDocumentoEntity tipoDocumento, String nroDocumento);

    @Query("select c from ClienteEntity c where c.usuario.fechaBaja is null")
    List<ClienteEntity> findActiveClientes(Pageable pageable);


}