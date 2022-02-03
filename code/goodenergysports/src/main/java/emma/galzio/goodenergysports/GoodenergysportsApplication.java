package emma.galzio.goodenergysports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GoodenergysportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodenergysportsApplication.class, args);
    }

}
