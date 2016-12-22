package khrom.test.sanzone.config;

import com.google.maps.model.LatLng;

/**
 * Created by DEV on 12/22/2016.
 */
//@Configuration
//@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request" )
//@ConfigurationProperties( prefix = "plot.sanzone.settings" )
public class SanzoneSettings {

    private int sectorN;
    private double distance;

    private LatLng center;

    public int getSectorN() {
        return sectorN;
    }

    public void setSectorN(int sectorN) {
        this.sectorN = sectorN;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }
}