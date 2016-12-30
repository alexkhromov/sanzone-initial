package khrom.test.sanzone.config;

import com.google.maps.model.LatLng;
import khrom.test.sanzone.common.util.MapUtil;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.service.SanzoneCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static khrom.test.sanzone.common.util.enums.DistanceUnit.METER;

/**
 * Created by DEV on 12/22/2016.
 */
@Component
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request" )
public class SessionSettings {

    @Autowired
    private GoogleStaticMapConfig mapConfig;

    @Autowired
    private DefaultSettings defaultSettings;

    @Autowired
    private SanzoneCalculationService calculationService;

    private Integer sectorN;
    private Integer sessionScale;
    private Integer distance;

    private Double heightMax;
    private Double heightMin;

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

    public Double getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(Double heightMax) {
        this.heightMax = heightMax;
    }

    public Double getHeightMin() {
        return heightMin;
    }

    public void setHeightMin(Double heightMin) {
        this.heightMin = heightMin;
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

        return defaultSettings.getResultSize() * ( sessionScale > 1 ? 2 : 1 );
    }

    public Integer getDefaultMaxSize() {

        return defaultSettings.getMaxDistance();
    }

    public Integer getDefaultPointStep() {

        return defaultSettings.getPointStep();
    }

    public void prepareSessionSettings( CreateSanzoneRequest dto ) {

        AtomicInteger count = new AtomicInteger( 1 );

        Map< Integer, Double > sectorsDistance = dto.getSectors()
                .stream()
                .map( sector -> {

                    double P = sector.getAntenna().getP();
                    double G = sector.getAntenna().getG();
                    double TL = sector.getAntenna().getTL();
                    double EF = sector.getAntenna().getEF();

                    G = pow( 10, G / 10 );
                    TL = pow( 10, TL / 10 );

                    return new AbstractMap.SimpleEntry<>( count.getAndIncrement(), pow( ( 8D * P * G * TL * EF ) / 10, 0.5 ) );

                } )
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );

        List< LatLng > coordinates = dto.getSectors()
                .stream()
                .map( sector -> new LatLng( sector.getLatitude(), sector.getLongitude() ) )
                .collect( Collectors.toList() );

        double latitude = coordinates
                .stream()
                .mapToDouble( latLng -> latLng.lat )
                .average()
                .getAsDouble();

        double sinComponent = coordinates
                .stream()
                .mapToDouble( latLng -> sin( toRadians( latLng.lng ) ) )
                .average()
                .getAsDouble();

        double cosComponent = coordinates
                .stream()
                .mapToDouble( latLng -> cos( toRadians( latLng.lng ) ) )
                .average()
                .getAsDouble();

        double longitude = toDegrees( atan2( sinComponent, cosComponent ) );

        LatLng center = new LatLng( latitude, longitude );

        count.set( 1 );
        dto.getSectors()
                .stream()
                .forEach( sector -> {

                    double offset = MapUtil.distance( sector.getLatitude(), sector.getLongitude(), 0, latitude, longitude, 0, METER );

                    double distance = sectorsDistance.get( count.intValue() );
                    sectorsDistance.put( count.intValue(), distance + offset );
                } );

        double maxDistance = sectorsDistance.entrySet()
                .stream()
                .map( entry -> entry.getValue() )
                .collect( Collectors.toList() )
                .stream()
                .max( Double::compareTo )
                .get();

        int sessionScale = ( int ) ( maxDistance / 100 ) + 1;

        this.center = center;
        this.distance = 100 * sessionScale;
        this.sessionScale = sessionScale >= 2 ? sessionScale : 1;

        this.heightMax = 0.0;
        this.heightMin = 0.0;

        for ( int i = 0, s = 1; i < dto.getSectors().size(); i++, s++ ) {

            this.sectorN = s;
            dto.setAzimuthM( dto.getSectors().get( i ).getAzimuth() );

            Map< Double, Set< Double > > sanzoneV = calculationService.calculateSanzoneForSummaryV( dto, this );

            double heightZoneMax = sanzoneV.keySet().stream().max( Double::compareTo ).get();
            double heightZoneMin = sanzoneV.keySet().stream().min( Double::compareTo ).get();

            this.heightMax = this.heightMax < heightZoneMax ? heightZoneMax : this.heightMax;
            this.heightMin = this.heightMin > heightZoneMin ? heightZoneMin : this.heightMin;
        }
    }
}