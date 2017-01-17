package khrom.test.sanzone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DEV on 1/10/2017.
 */
@Configuration
@ConfigurationProperties( prefix = "endpoints.cors" )
public class CORSConfig {

    private String [] accessControlAllowOrigins;
    private String [] accessControlAllowMethods;
    private String [] accessControlAllowHeaders;
    private Boolean accessControlAllowCredentials;
    private Long accessControlAllowMaxAge;

    public String[] getAccessControlAllowOrigins() {
        return accessControlAllowOrigins;
    }

    public void setAccessControlAllowOrigins(String[] accessControlAllowOrigins) {
        this.accessControlAllowOrigins = accessControlAllowOrigins;
    }

    public String[] getAccessControlAllowMethods() {
        return accessControlAllowMethods;
    }

    public void setAccessControlAllowMethods(String[] accessControlAllowMethods) {
        this.accessControlAllowMethods = accessControlAllowMethods;
    }

    public String[] getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(String[] accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public Boolean getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(Boolean accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }

    public Long getAccessControlAllowMaxAge() {
        return accessControlAllowMaxAge;
    }

    public void setAccessControlAllowMaxAge(Long accessControlAllowMaxAge) {
        this.accessControlAllowMaxAge = accessControlAllowMaxAge;
    }
}