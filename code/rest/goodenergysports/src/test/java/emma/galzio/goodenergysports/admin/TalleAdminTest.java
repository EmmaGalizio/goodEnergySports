package emma.galzio.goodenergysports.admin;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.TalleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
public class TalleAdminTest {

    @Autowired
    private TalleRepository talleRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void registrarBajaTest(){

        TalleEntityId talleEntityId = new TalleEntityId("XXL",2);
        talleRepository.registrarBaja(LocalDate.now(),talleEntityId);

    }
}
