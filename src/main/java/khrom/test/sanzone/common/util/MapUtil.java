package khrom.test.sanzone.common.util;

import com.google.maps.model.LatLng;
import javafx.geometry.Point2D;
import khrom.test.sanzone.common.util.enums.DistanceUnit;
import khrom.test.sanzone.common.util.enums.SearchDirection;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;
import static khrom.test.sanzone.common.constant.Constant.EARTH_RADIUS;
import static khrom.test.sanzone.common.util.enums.SearchDirection.*;

/**
 * Created by DEV on 9/16/2016.
 */
public class MapUtil {

    private static final int MAX_DISTANCE = 200;

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

    public static LatLng [] getCoordinatesForSector( CreateSectorDTO sector, DistanceUnit unit ) {


        double [][] source = calculateSanzoneForSector( sector );

        LatLng [] coordinates = new LatLng[ source.length * 2 + 1 ];

        double latitude = sector.getLatitude();
        double longitude = sector.getLongitude();
        double azimuth = sector.getAzimuth();

        int offset = 2;
        for ( int i = 0; i < source.length; i++ ) {

            if ( i == 0 ) {

                coordinates[ i ] = new LatLng( latitude, longitude );
                coordinates[ coordinates.length - 1 ] = new LatLng( latitude, longitude );
                coordinates[ source.length ] = getLatLng( latitude, longitude, source[ i ][ 1 ], azimuth, unit );

                continue;
            }

            coordinates[ source.length - i ] = getLatLng( latitude, longitude, source[ i ][ 1 ], azimuth + source[ i ][ 0 ], unit );
            coordinates[ source.length - i + offset ] = getLatLng( latitude, longitude, source[ i ][ 1 ], azimuth - source[ i ][ 0 ], unit );

            offset += 2;
        }

        return coordinates;
    }

    public static LatLng [] getCoordinatesForSummary( List< CreateSectorDTO > sectors, DistanceUnit unit ) {

        double [][] summary = calculateSanzoneForSummary( sectors );

        List< Point2D > points = getBorderPointsForSummary( summary );

        List< Point2D > polarPoints = new LinkedList<>();

        for ( int i = 0; i < points.size() - 1; i++ ) {

            Point2D point2D = points.get( i );

            double phi = atan( ( MAX_DISTANCE - point2D.getX() ) / ( point2D.getY() - MAX_DISTANCE ) ) * 180D / PI;

            if ( ( MAX_DISTANCE - point2D.getX() <= 0 ) && ( point2D.getY() - MAX_DISTANCE  >= 0 ) ) {
                phi = abs( phi ) + 90;
            } else if ( ( MAX_DISTANCE - point2D.getX() <= 0 ) && ( point2D.getY() - MAX_DISTANCE <= 0 ) ) {
                phi = 270 - abs( phi );
            } else if ( ( MAX_DISTANCE - point2D.getX() >= 0 ) && ( point2D.getY() - MAX_DISTANCE <= 0 ) ) {
                phi = abs( phi ) + 270;
            } else {
                phi = 90 - abs( phi );
            }

            double distance = sqrt( pow( point2D.getX() - MAX_DISTANCE, 2D ) + pow( MAX_DISTANCE - point2D.getY(), 2D ) );

            polarPoints.add( new Point2D( phi, distance ) );
        }

        LatLng [] coordinates = new LatLng[ polarPoints.size() - 1 ];

        double latitude = sectors.get( 0 ).getLatitude();
        double longitude = sectors.get( 0 ).getLongitude();

        for ( int i = 0; i < coordinates.length; i++ ) {
            coordinates[ i ] = getLatLng( latitude, longitude, polarPoints.get( i ).getY(), polarPoints.get( i ).getX(), unit );
        }

        return coordinates;
    }

    public static Polygon getPolygonForSector( double [][] source, int centerX, int centerY, double azimuth, double ratioPixelToMeter ) {

        int [] xPoints  = new int [ source.length * 2 ];
        int [] yPoints  = new int [ source.length * 2 ];
        int nPoints = source.length * 2;

        int offset = 2;
        for ( int i = 0; i < source.length; i++ ) {

            if ( i == 0 ) {

                xPoints[ i ] = centerX;
                yPoints[ i ] = centerY;

                xPoints[ source.length ] = centerX + ( int ) ( source[ i ][ 1 ] * cos( toRadians( 90D - azimuth - source[ i ][ 0 ] ) ) * ratioPixelToMeter );
                yPoints[ source.length ] = centerY - ( int ) ( source[ i ][ 1 ] * sin( toRadians( 90D - azimuth - source[ i ][ 0 ] ) ) * ratioPixelToMeter );

                continue;
            }

            xPoints[ source.length - i ] = centerX + ( int ) ( source[ i ][ 1 ] * cos( toRadians( 90D - azimuth - source[ i ][ 0 ] ) ) * ratioPixelToMeter );
            yPoints[ source.length - i ] = centerY - ( int ) ( source[ i ][ 1 ] * sin( toRadians( 90D - azimuth - source[ i ][ 0 ] ) ) * ratioPixelToMeter );

            xPoints[ source.length - i + offset ] = centerX + ( int ) ( source[ i ][ 1 ] * cos( toRadians( 90D - azimuth + source[ i ][ 0 ] ) ) * ratioPixelToMeter );
            yPoints[ source.length - i + offset ] = centerY - ( int ) ( source[ i ][ 1 ] * sin( toRadians( 90D - azimuth + source[ i ][ 0 ] ) ) * ratioPixelToMeter );

            offset += 2;
        }

        return new Polygon( xPoints, yPoints, nPoints );
    }

    public static Polygon getPolygonForSummary( List< Point2D > points, int centerX, int centerY, double ratioPixelToMeter ) {

        int [] xPoints  = new int [ points.size() ];
        int [] yPoints  = new int [ points.size() ];
        int nPoints = points.size();

        for ( int i = 0; i < points.size(); i++ ) {

            Point2D point = points.get( i );

            xPoints[ i ] = centerX + ( int ) ( ( point.getY() - MAX_DISTANCE ) * ratioPixelToMeter );
            yPoints[ i ] = centerY - ( int ) ( ( MAX_DISTANCE - point.getX() ) * ratioPixelToMeter );
        }

        return new Polygon( xPoints, yPoints, nPoints );
    }

    public static double [][] calculateSanzoneForSector( CreateSectorDTO sector ) {

        double P = sector.getAntenna().getP();
        double G = sector.getAntenna().getG();
        double TL = sector.getAntenna().getTL();
        double EF = sector.getAntenna().getEF();
        double Q05 = sector.getAntenna().getQ05();

        G = pow( 10, G / 10 );
        TL = pow( 10, TL / 10 );

        double [][] rawData = new double[ MAX_DISTANCE + 1 ][ MAX_DISTANCE + 1 ];

        for ( int i = 1; i < rawData.length; i++ ) {

            for ( int j = 0, offsetY = MAX_DISTANCE / 2; j < rawData.length; j++, offsetY-- ) {

                double phi = atan( ( ( double ) offsetY ) / ( ( double ) i ) ) * 180D / PI;
                rawData[ j ][ i ] = ( 8D * P * G * TL * EF * ( exp( -0.69D * pow( phi * 2D / Q05, 2D ) ) ) ) / ( pow( ( ( double ) i ), 2D ) + pow( ( ( double ) offsetY ), 2D ) );
            }
        }

        /*// @formatter:off
        final AtomicInteger counter = new AtomicInteger( 0 );
        String temp  = DoubleStream.of( rawData[ 100 ] )
                                   .boxed()
                                   .map( String::valueOf )
                                   .collect( Collector.of( () -> new StringJoiner( ", ", "{ ", " }" ),
                                                           ( j, p ) -> j.add( "[ " + p + ";" + counter.getAndIncrement() + " ]" ),
                                                           ( j1, j2 ) -> j1.merge( j2 ),
                                                           StringJoiner::toString ) );
        // @formatter:on*/

        List< Point2D > points = new ArrayList<>();

        for ( int i = 1; i < MAX_DISTANCE; i++ ) {

            Point2D p = null;

            for ( int j = 0; j <= MAX_DISTANCE / 2; j++ ) {

                if ( rawData[ j ][ i ] > 10 ) {

                    p = new Point2D( i, ( MAX_DISTANCE / 2 ) - j );
                    points.add( p );
                    break;
                }
            }

            if ( p == null || p.getY() <= 0 ) {
                break;
            }
        }

        double levelAtZeroDegree = sqrt( 8D * P * G * TL * EF / 10D );

        List< Point2D > polarPoints = new ArrayList<>();

        polarPoints.add( new Point2D( 0D, levelAtZeroDegree ) );

        for ( int i = points.size() - 1; i > 0; i-- ) {

            Point2D point2D = points.get( i );

            double phi = atan( point2D.getY() / point2D.getX() ) * 180D / PI;
            double distance = sqrt( pow( point2D.getX(), 2D ) + pow( point2D.getY(), 2D ) );

            polarPoints.add( new Point2D( phi, distance ) );
        }

        double [][] source = new double[ polarPoints.size() ][];

        for ( int i = 0; i < source.length; i++ ) {

            Point2D polarPoint = polarPoints.get( i );

            source[ i ] = new double[] { polarPoint.getX(), polarPoint.getY() };
        }

        return source;
    }

    public static double [][] calculateSanzoneForSummary( List< CreateSectorDTO > sectors ) {

        double [][] summary = new double[ ( MAX_DISTANCE * 2 ) + 1 ][ ( MAX_DISTANCE * 2 ) + 1 ];

        for ( CreateSectorDTO sector: sectors ) {

            double P = sector.getAntenna().getP();
            double G = sector.getAntenna().getG();
            double TL = sector.getAntenna().getTL();
            double EF = sector.getAntenna().getEF();
            double Q05 = sector.getAntenna().getQ05();

            G = pow( 10, G / 10 );
            TL = pow( 10, TL / 10 );

            double [][] sectorSanzone = new double[ MAX_DISTANCE + 1 ][ MAX_DISTANCE + 1 ];

            for ( int i = 1; i < sectorSanzone.length; i++ ) {

                for ( int j = 0, offsetY = MAX_DISTANCE / 2; j < sectorSanzone.length; j++, offsetY-- ) {

                    double phi = atan( ( ( double ) offsetY ) / ( ( double ) i ) ) * 180D / PI;
                    sectorSanzone[ j ][ i ] = ( 8D * P * G * TL * EF * ( exp( -0.69D * pow( phi * 2D / Q05, 2D ) ) ) ) / ( pow( ( ( double ) i ), 2D ) + pow( ( ( double ) offsetY ), 2D ) );
                }
            }

            boolean [][] isSet = new boolean[ ( MAX_DISTANCE * 2 ) + 1 ][ ( MAX_DISTANCE * 2 ) + 1 ];

            for ( int i = 0, offsetY = MAX_DISTANCE / 2; i < sectorSanzone.length; i++, offsetY-- ) {

                for ( int j = 1; j < sectorSanzone[ i ].length; j++ ) {

                    int X = MAX_DISTANCE - ( int ) ( ( ( double ) j ) * sin( toRadians( 90D - sector.getAzimuth() ) ) + ( ( double ) offsetY ) * cos( toRadians( 90D - sector.getAzimuth() ) ) );
                    int Y = MAX_DISTANCE + ( int ) ( ( ( double ) j ) * cos( toRadians( 90D - sector.getAzimuth() ) ) - ( ( double ) offsetY ) * sin( toRadians( 90D - sector.getAzimuth() ) ) );

                    if ( X >= 0 && summary.length > X && Y >= 0 && summary[ X ].length > Y ) {

                        if ( !isSet[ X ][ Y ] ) {

                            summary[ X ][ Y ] += sectorSanzone[ i ][ j ];
                            isSet[ X ][ Y ] = true;
                        }
                    }
                }
            }
        }

        return summary;
    }

    public static List< Point2D > getBorderPointsForSummary( double [][] summary ) {

        List< Point2D > points = new LinkedList<>();
        Point2D start = null;

        for ( int i = 0; i < MAX_DISTANCE ; i++ ) {

            if ( summary[ MAX_DISTANCE ][ i ] >= 10 ) {

                start = new Point2D( MAX_DISTANCE, i );
                points.add( start );

                break;
            }
        }

        SearchDirection direction = SearchDirection.NORTH_WEST;

        for ( ; ; ) {

            Point2D previous = points.get( points.size() - 1 );
            Point2D next = null;

            switch ( direction ) {

                case NORTH:

                    next = NORTH.getNext( previous );

                    break;

                case NORTH_EAST:

                    next = NORTH_EAST.getNext( previous );

                    break;

                case EAST:

                    next = EAST.getNext( previous );

                    break;

                case SOUTH_EAST:

                    next = SOUTH_EAST.getNext( previous );

                    break;

                case SOUTH:

                    next = SOUTH.getNext( previous );

                    break;

                case SOUTH_WEST:

                    next = SOUTH_WEST.getNext( previous );

                    break;

                case WEST:

                    next = WEST.getNext( previous );

                    break;

                case NORTH_WEST:

                    next = NORTH_WEST.getNext( previous );

                    break;
            }

            if ( summary[ ( int ) next.getX() ][ ( int ) next.getY() ] >= 10 ) {

                points.add( next );
                direction = direction.getNextPointDirection();
            } else {

                direction = direction.getRotatePointDirection();
                continue;
            }

            if ( ( ( int ) start.getX() == ( int ) points.get( points.size() - 1 ).getX() ) &&
                    ( ( int ) start.getY() == ( int ) points.get( points.size() - 1 ).getY() ) ) {

                break;
            }
        }

        return points;
    }
}