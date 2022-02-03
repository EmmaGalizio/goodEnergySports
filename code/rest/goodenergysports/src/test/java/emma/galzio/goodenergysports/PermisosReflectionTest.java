package emma.galzio.goodenergysports;

import emma.galzio.goodenergysports.security.discovering.ReflectionResourcesDiscoveringService;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoBusinessToEntityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PermisosReflectionTest {

    @Autowired
    private PermisoBusinessToEntityMapper permisoBusinessToEntityMapper;
    @Autowired
    private PermisoRepository permisoRepository;

    @Test
    public void testReflection(){

        ReflectionResourcesDiscoveringService reflectionResourcesDiscoveringService =
                new ReflectionResourcesDiscoveringService(permisoRepository,permisoBusinessToEntityMapper);

        reflectionResourcesDiscoveringService.descubrirRecursosExpuestos("/api");

    }
}
