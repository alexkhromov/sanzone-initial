package khrom.test.sanzone.common.util;

import com.google.maps.model.LatLng;
import javafx.geometry.Point2D;
import khrom.test.sanzone.common.util.enums.DistanceUnit;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;
import static khrom.test.sanzone.common.constant.Constant.EARTH_RADIUS;

/**
 * Created by DEV on 9/16/2016.
 */
public class MapUtil {

    /**
     * Returns an angle of map image in degrees that falls on given image width size and given zoom.
     *
     * @param sizeX the image width in pixels
     * @param zoom the image zoom
     * @return an angle of map image in degrees for given image width size and given zoom
     */
    public static double getImageWidthDegree( int sizeX, int zoom ) {

        return ( sizeX / 256D ) * ( 360D / pow( 2, zoom ) );
    }

    /**
     * Returns a ratio of image pixels to distance.
     *
     * @param sizeX the image width in pixels
     * @param scale the image scale
     * @param distance the distance
     * @return ratio of image pixels to distance for given latitude, longitude and distance unit
     */
    public static double getRatioPixelToDistance( int sizeX, int scale, double distance ) {

        return ( sizeX * scale ) / 2D / distance;
    }

    /**
     * Returns an image width or height center for given scale in pixels.
     *
     * @param size the image width or height size in pixels
     * @param scale the image scale
     * @return an image width or height center for given scale
     */
    public static int getImageCenter( int size, int scale ) {

        return size * scale / 2;
    }

    /**
     * Returns a distance between two coordinates.
     *
     * @param lat1 the real first latitude coordinate
     * @param lon1 the real first longitude coordinate
     * @param alt1 the first altitude ( Sea level )
     * @param lat2 the real second latitude coordinate
     * @param lon2 the real second longitude coordinate
     * @param alt2 the second altitude ( Sea level )
     * @param unit the distance unit
     * @return a distance between two coordinates in a given distance unit
     */
    public static double distance( double lat1, double lon1, double alt1,
                                   double lat2, double lon2, double alt2, DistanceUnit unit ) {

        double latDistance = toRadians( lat2 - lat1 );
        double lonDistance = toRadians( lon2 - lon1 );
        double height = alt1 - alt2;

        double a = sin( latDistance / 2D ) * sin( latDistance / 2D ) + cos( toRadians( lat1 ) ) * cos( toRadians( lat2 ) ) * sin( lonDistance / 2D ) * sin( lonDistance / 2D );

        double c = 2D * atan2( sqrt( a ), sqrt( 1D - a ) );

        double distance = EARTH_RADIUS * c * unit.getRatio();

        distance = pow( distance, 2 ) + pow( height, 2 );

        return sqrt( distance );
    }

    public static LatLng getLatLng( double latitude, double longitude, double distance, double azimuth, DistanceUnit unit ) {

        double lat = asin( sin( toRadians( latitude ) ) * cos( distance / ( EARTH_RADIUS * unit.getRatio() ) ) +
                           cos( toRadians( latitude ) ) * sin( distance / ( EARTH_RADIUS * unit.getRatio() ) ) * cos( toRadians( azimuth ) ) );

        double dlon = atan2( sin( toRadians( azimuth ) ) * sin( distance / ( EARTH_RADIUS * unit.getRatio() ) ) * cos( toRadians( latitude ) ),
                             cos( distance / ( EARTH_RADIUS * unit.getRatio() ) ) - sin( toRadians( latitude ) ) * sin( lat ) );

        double lon = ( ( toRadians( longitude ) + dlon + 3D * PI ) % ( 2D * PI ) ) - PI;

        return new LatLng( toDegrees( lat ), toDegrees( lon ) );
    }

    public static LatLng [] getCoordinatesForSector( double [][] sanzone, CreateSectorDTO sector, DistanceUnit unit ) {

        LatLng [] coordinates = new LatLng[ sanzone.length * 2 + 1 ];

        double latitude = sector.getLatitude();
        double longitude = sector.getLongitude();
        double azimuth = sector.getAzimuth();

        int offset = 2;
        for ( int i = 0; i < sanzone.length; i++ ) {

            if ( i == 0 ) {

                coordinates[ i ] = new LatLng( latitude, longitude );
                coordinates[ coordinates.length - 1 ] = new LatLng( latitude, longitude );
                coordinates[ sanzone.length ] = getLatLng( latitude, longitude, sanzone[ i ][ 1 ], azimuth, unit );

                continue;
            }

            coordinates[ sanzone.length - i ] = getLatLng( latitude, longitude, sanzone[ i ][ 1 ], azimuth + sanzone[ i ][ 0 ], unit );
            coordinates[ sanzone.length - i + offset ] = getLatLng( latitude, longitude, sanzone[ i ][ 1 ], azimuth - sanzone[ i ][ 0 ], unit );

            offset += 2;
        }

        return coordinates;
    }

    //TODO NOT DELETE!!!
    public static LatLng [] getCoordinatesForSummary( List< org.opencv.core.Point > points, List< CreateSectorDTO > sectors, DistanceUnit unit ) {

        List< Point2D > polarPoints = new LinkedList<>();

        for ( int i = 0; i < points.size(); i++ ) {

            org.opencv.core.Point point = points.get( i );

            double phi = atan( point.x / point.y ) * 180D / PI;

            if ( point.x <= 0 && point.y >= 0 ) {
                phi = abs( phi ) + 90;
            } else if ( point.x <= 0 && point.y <= 0 ) {
                phi = 270 - abs( phi );
            } else if ( point.x >= 0 && point.y  <= 0 ) {
                phi = abs( phi ) + 270;
            } else {
                phi = 90 - abs( phi );
            }

            double distance = sqrt( pow( point.x, 2D ) + pow( point.y, 2D ) );

            polarPoints.add( new Point2D( phi, distance ) );
        }

        LatLng [] coordinates = new LatLng[ polarPoints.size() ];

        double latitude = sectors.get( 0 ).getLatitude();
        double longitude = sectors.get( 0 ).getLongitude();

        for ( int i = 0; i < coordinates.length; i++ ) {
            coordinates[ i ] = getLatLng( latitude, longitude, polarPoints.get( i ).getY(), polarPoints.get( i ).getX(), unit );
        }

        return coordinates;
    }

    public static Polygon getPolygonForSector( double [][] sanzone, int centerX, int centerY, double azimuth, double ratioPixelToMeter ) {

        int [] xPoints  = new int [ sanzone.length * 2 ];
        int [] yPoints  = new int [ sanzone.length * 2 ];
        int nPoints = sanzone.length * 2;

        int offset = 2;
        for ( int i = 0; i < sanzone.length; i++ ) {

            if ( i == 0 ) {

                xPoints[ i ] = centerX;
                yPoints[ i ] = centerY;

                xPoints[ sanzone.length ] = centerX + ( int ) ( sanzone[ i ][ 1 ] * cos( toRadians( 90D - azimuth - sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );
                yPoints[ sanzone.length ] = centerY - ( int ) ( sanzone[ i ][ 1 ] * sin( toRadians( 90D - azimuth - sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );

                continue;
            }

            xPoints[ sanzone.length - i ] = centerX + ( int ) ( sanzone[ i ][ 1 ] * cos( toRadians( 90D - azimuth - sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );
            yPoints[ sanzone.length - i ] = centerY - ( int ) ( sanzone[ i ][ 1 ] * sin( toRadians( 90D - azimuth - sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );

            xPoints[ sanzone.length - i + offset ] = centerX + ( int ) ( sanzone[ i ][ 1 ] * cos( toRadians( 90D - azimuth + sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );
            yPoints[ sanzone.length - i + offset ] = centerY - ( int ) ( sanzone[ i ][ 1 ] * sin( toRadians( 90D - azimuth + sanzone[ i ][ 0 ] ) ) * ratioPixelToMeter );

            offset += 2;
        }

        return new Polygon( xPoints, yPoints, nPoints );
    }
}