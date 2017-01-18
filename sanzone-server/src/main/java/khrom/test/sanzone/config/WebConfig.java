package khrom.test.sanzone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by DEV on 1/10/2017.
 */
@Configuration
@EnableWebMvc
@Import( CORSConfig.class )
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private CORSConfig corsConfig;

    @Override
    public void addCorsMappings( CorsRegistry registry ) {

        registry.addMapping( "/v1/**" )
                .allowedOrigins( corsConfig.getAccessControlAllowOrigins() )
                .allowedMethods( corsConfig.getAccessControlAllowMethods() )
                .allowedHeaders( corsConfig.getAccessControlAllowHeaders() )
                .allowCredentials( corsConfig.getAccessControlAllowCredentials() )
                .maxAge( corsConfig.getAccessControlAllowMaxAge() );
    }
}