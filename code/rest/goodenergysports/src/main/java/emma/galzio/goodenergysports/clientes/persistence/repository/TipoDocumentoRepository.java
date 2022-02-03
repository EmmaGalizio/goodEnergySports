package emma.galzio.goodenergysports.clientes.persistence.repository;

import emma.galzio.goodenergysports.clientes.persistence.entity.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, String> {
}