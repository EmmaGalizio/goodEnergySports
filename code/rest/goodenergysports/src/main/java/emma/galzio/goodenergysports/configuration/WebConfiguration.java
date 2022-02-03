package emma.galzio.goodenergysports.configuration;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${goodEnergy.WebResourceDirectory}")
    private String resorucesPath;
    @Value("${goodEnergy.WebResourcePattern}")
    private String resourcesPathPattern;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("file:"+resorucesPath+ File.separator);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    @SneakyThrows
    public Algorithm getJWTSignAlgorithm(@Value("${goodEnergy.jwt.secret}") String secret){
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedSecretBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        return Algorithm.HMAC256(hashedSecretBytes);
    }
}
