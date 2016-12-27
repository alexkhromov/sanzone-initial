package khrom.test.sanzone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DEV on 12/27/2016.
 */
@Configuration
@ConfigurationProperties( prefix = "default.settings" )
public class DefaultSettings {

    private Integer resultSize;
    private Integer maxDistance;
    private Integer pointStep;

    public Integer getResultSize() {
        return resultSize;
    }

    public void setResultSize(Integer resultSize) {
        this.resultSize = resultSize;
    }

    public Integer getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Integer maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Integer getPointStep() {
        return pointStep;
    }

    public void setPointStep(Integer pointStep) {
        this.pointStep = pointStep;
    }
}