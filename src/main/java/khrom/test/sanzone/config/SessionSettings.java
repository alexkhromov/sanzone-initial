package khrom.test.sanzone.config;

import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created by DEV on 12/22/2016.
 */
@Component
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request" )
public class SessionSettings {

    @Autowired
    private GoogleStaticMapConfig mapConfig;

    @Value( "${default.size}" )
    private Integer size;

    private Integer sectorN;
    private Integer sessionScale;
    private Integer distance;

    private LatLng center;

    public Integer getSectorN() {
        return sectorN;
    }

    public void setSectorN(Integer sectorN) {
        this.sectorN = sectorN;
    }

    public Integer getSessionScale() {
        return sessionScale;
    }

    public void setSessionScale(Integer sessionScale) {
        this.sessionScale = sessionScale;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public int getSizeX() {
        return sessionScale * mapConfig.getSizeX();
    }

    public int getSizeY() {
        return sessionScale * mapConfig.getSizeY();
    }

    public int getImageWidth() {

        return sessionScale * mapConfig.getImageWidth();
    }

    public int getImageHeight() {

        return sessionScale * mapConfig.getImageHeight();
    }

    public int getWidthCenter() {

        return sessionScale * mapConfig.getWidthCenter();
    }

    public int getHeightCenter() {

        return sessionScale * mapConfig.getHeightCenter();
    }

    public Object [] getObjectsForCommonPattern() {

        return mapConfig.getObjectsForCommonPattern( center.lat, center.lng, sessionScale );
    }

    public Integer getResultSize() {

        return size * ( sessionScale > 1 ? 2 : 1 );
    }
}