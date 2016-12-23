package khrom.test.sanzone.config;

import khrom.test.sanzone.common.util.MapUtil;
import khrom.test.sanzone.common.util.enums.DistanceUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by DEV on 9/16/2016.
 */
@Configuration
@ConfigurationProperties( prefix = "google.static.map" )
public class GoogleStaticMapConfig {

    private int zoom;
    private int sizeX;
    private int sizeY;
    private int scale;
    private String mapType;
    private String format;
    private String language;
    private String markers;
    private String key;

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMarkers() {
        return markers;
    }

    public void setMarkers(String markers) {
        this.markers = markers;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns a map image width.
     *
     * @return a map image width
     */
    public int getImageWidth() {

        return sizeX * scale;
    }

    /**
     * Returns a map image height.
     *
     * @return a map image height
     */
    public int getImageHeight() {

        return sizeY * scale;
    }

    /**
     * Returns an angle of map image in degrees that falls on default image width size and default zoom.
     *
     * @return an angle of map image in degrees for default image width size and default zoom
     */
    public double getImageWidthDegree() {

        return MapUtil.getImageWidthDegree( sizeX, zoom );
    }

    /**
     * Returns an angle of map image in degrees that falls on given image width size and default zoom.
     *
     * @param sizeX the image width in pixels
     * @return an angle of map image in degrees for given image width size and default zoom
     */
    public double getImageWidthDegree( int sizeX ) {

        return MapUtil.getImageWidthDegree( sizeX, zoom );
    }

    /**
     * Returns an angle of map image in degrees that falls on given image width size and given zoom.
     *
     * @param sizeX the image width in pixels
     * @param zoom the image zoom
     * @return an angle of map image in degrees for given image width size and given zoom
     */
    public double getImageWidthDegree( int sizeX, int zoom ) {

        return MapUtil.getImageWidthDegree( sizeX, zoom );
    }

    /**
     * Returns a ratio of image pixels to distance.
     * This method is used default image width size and default scale.
     *
     * @param latitude the real latitude coordinate
     * @param longitude the real longitude coordinate
     * @param unit the distance unit
     * @return ratio of image pixels to distance for given latitude, longitude and distance unit
     */
    public double getRatioPixelToDistance( double latitude, double longitude, DistanceUnit unit ) {

        double distance = MapUtil.distance( latitude, longitude, 0, latitude, longitude + getImageWidthDegree() / 2, 0, unit );

        return MapUtil.getRatioPixelToDistance( sizeX, scale, distance );
    }

    /**
     * Returns a ratio of image pixels to distance.
     * This method is used default image width size and default scale.
     *
     * @param distance the distance, can be measured in meters, kilometers
     * @return ratio of image pixels to distance for given distance
     */
    public double getRatioPixelToDistance( double distance ) {

        return MapUtil.getRatioPixelToDistance( sizeX, scale, distance );
    }

    /**
     * Returns an image width center in pixels.
     * This method is used default image width size in pixels and default scale.
     *
     * @return an image width for default width and scale
     */
    public int getWidthCenter() {

        return MapUtil.getImageCenter( sizeX, scale );
    }

    /**
     * Returns an image height center in pixels.
     * This method is used default image height size in pixels and default scale.
     *
     * @return an image height for default width and scale
     */
    public int getHeightCenter() {

        return MapUtil.getImageCenter( sizeY, scale );
    }

    public Object [] getObjectsForCommonPattern( Double latitude, Double longitude, Integer sessionScale ) {

        return new Object [] { latitude, longitude,
                               zoom,
                               sizeX * ( sessionScale != null ? sessionScale : 1 ),
                               sizeY * ( sessionScale != null ? sessionScale : 1 ),
                               scale, mapType, format, language, key };
    }

    public Object [] getObjectsForPolylinePattern( Double latitude, Double longitude, String polyline ) {

        return new Object [] { latitude, longitude, zoom, sizeX, sizeY, scale, mapType, format, language, polyline, key };
    }
}