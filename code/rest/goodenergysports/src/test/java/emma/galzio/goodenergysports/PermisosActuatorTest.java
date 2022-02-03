package emma.galzio.goodenergysports;

import emma.galzio.goodenergysports.security.discovering.ActuatorResourcesDiscoveringService;
import emma.galzio.goodenergysports.security.discovering.ResourcesDiscoveringService;
import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoBusinessToEntityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PermisosActuatorTest {

    @Autowired
    private  PermisoRepository permisoRepository;
    @Autowired
    private  PermisoBusinessToEntityMapper permisoBusinessToEntityMapper;

    @Test
    public void  testWebClientActuatorRequest(){
        ResourcesDiscoveringService resoursesDiscoveringService =
                new ActuatorResourcesDiscoveringService(permisoRepository,permisoBusinessToEntityMapper);

        List<Permiso> permisos = resoursesDiscoveringService.descubrirRecursosExpuestos("/api");
    }

}
