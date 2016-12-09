package khrom.test.sanzone.common.util;

import com.google.maps.model.LatLng;
import javafx.geometry.Point2D;
import khrom.test.sanzone.common.util.enums.DistanceUnit;
import khrom.test.sanzone.common.util.enums.SearchDirection;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;
import static khrom.test.sanzone.common.constant.Constant.EARTH_RADIUS;
import static khrom.test.sanzone.common.util.enums.DistanceUnit.METER;

/**
 * Created by DEV on 9/16/2016.
 */
public class MapUtil {

    public static final int MAX_DISTANCE = 200;
    private static final int POINT_STEP = 1;

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

    public static LatLng [] getCoordinatesForSummary( double [][] sanzone, List< CreateSectorDTO > sectors, DistanceUnit unit ) {

        List< Point2D > points = getBorderPointsForSummary( sanzone );

        List< Point2D > polarPoints = new LinkedList<>();

        for ( int i = 0; i < points.size() - 1; i = i + POINT_STEP ) {

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

    public static Polygon getPolygonForSummary( List< Point2D > points, int centerX, int centerY, double ratioPixelToMeter ) {

        int [] xPoints  = new int [ points.size() / POINT_STEP ];
        int [] yPoints  = new int [ points.size() / POINT_STEP ];
        int nPoints = points.size() / POINT_STEP;

        for ( int i = 0; i < points.size() / POINT_STEP; i++ ) {

            Point2D point = points.get( i * POINT_STEP );

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
        double Q05 = sector.getAntenna().getQ05H();

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

        double [][] sanzone = new double[ polarPoints.size() ][];

        for ( int i = 0; i < sanzone.length; i++ ) {

            Point2D polarPoint = polarPoints.get( i );

            sanzone[ i ] = new double[] { polarPoint.getX(), polarPoint.getY() };
        }

        return sanzone;
    }

    public static double [][] calculateSanzoneForSummary( List< CreateSectorDTO > sectors ) {

        double [][] summary = new double[ ( MAX_DISTANCE * 2 ) + 1 ][ ( MAX_DISTANCE * 2 ) + 1 ];

        for ( CreateSectorDTO sector: sectors ) {

            double P = sector.getAntenna().getP();
            double G = sector.getAntenna().getG();
            double TL = sector.getAntenna().getTL();
            double EF = sector.getAntenna().getEF();
            double Q05 = sector.getAntenna().getQ05H();

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

    public static List< Point > calculateSanzoneForSummaryV2( List< CreateSectorDTO > sectors ) {

        List< Point > summary = new ArrayList<>();

        double P, G, TL, EF, Q05H;

        double latitude = sectors.get( 0 ).getLatitude();
        double longitude = sectors.get( 0 ).getLongitude();

        double [][] offsets = new double[ sectors.size() ][ 3 ];

        for ( int i = 0; i < offsets.length; i++ ) {

            // offsetLat
            if ( Double.compare( sectors.get( i ).getLatitude(), latitude ) != 0 ) {
                offsets[ i ][ 0 ] = distance( latitude, longitude, 0, sectors.get( i ).getLatitude(), longitude, 0, METER ) * Double.compare( latitude, sectors.get( i ).getLatitude() );
            }

            // offsetLon
            if ( Double.compare( sectors.get( i ).getLongitude(), longitude ) != 0 ) {
                offsets[ i ][ 1 ] = distance( latitude, longitude, 0, latitude, sectors.get( i ).getLongitude(), 0, METER ) * Double.compare( sectors.get( i ).getLongitude(), longitude );
            }

            // azimuthToPolarAngle
            offsets[ i ][ 2 ] = 90 - sectors.get( i ).getAzimuth() + ( 90 - sectors.get( i ).getAzimuth() <= 0 ? 360 : 0 );
        }

        for ( int i = 0, y = MAX_DISTANCE; i < 2 * MAX_DISTANCE + 1; i++, y-- ) {

            for ( int j = 0, x = -MAX_DISTANCE; j < 2 * MAX_DISTANCE + 1; j++, x++ ) {

                double intensity = 0;

                for ( int s = 0, sectorN = 1; s < sectors.size(); s++, sectorN++ ) {

                    CreateSectorDTO sector = sectors.get( s );

                    double polarAngle = atan2( y + offsets[ s ][ 0 ], x - offsets[ s ][ 1 ] ) * 360 / ( 2 * PI );
                    polarAngle += polarAngle < 0 ? 360 : 0;

                    if ( offsets[ s ][ 2 ] - 90 + ( offsets[ s ][ 2 ] - 90 < 0 ? 360 : 0 ) > polarAngle && ( offsets[ s ][ 2 ] + 90 ) % 360 < polarAngle ) {
                        continue;
                    }

                    double phi = polarAngle - offsets[ s ][ 2 ];

                    if ( phi > 90  ) {
                        phi -= 360;
                    } else if ( phi < -90 ) {
                        phi += 360;
                    }

                    P = sector.getAntenna().getP();
                    G = sector.getAntenna().getG();
                    TL = sector.getAntenna().getTL();
                    EF = sector.getAntenna().getEF();
                    Q05H = sector.getAntenna().getQ05H();

                    G = pow( 10, G / 10 );
                    TL = pow( 10, TL / 10 );

                    intensity += ( 8D * P * G * TL * EF * ( exp( -0.69D * pow( phi * 2D / Q05H, 2D ) ) ) ) / ( pow( x - offsets[ s ][ 1 ], 2D ) + pow( y + offsets[ s ][ 0 ], 2D ) );
                }

                if ( intensity >= 10 ) {
                    summary.add( new Point( x, y ) );
                }
            }
        }

        return summary;
    }

    public static List< Point > calculateSanzoneForSummaryV3( CreateSanzoneRequest dto ) {

        List< Point > summary = new ArrayList<>();

        double H, P, G, TL, EF, Q05H, Q05V;

        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        List< CreateSectorDTO > sectors = dto.getSectors();

        double [][] offsets = new double[ sectors.size() ][ 3 ];

        for ( int i = 0; i < offsets.length; i++ ) {

            // offsetLat
            if ( Double.compare( sectors.get( i ).getLatitude(), latitude ) != 0 ) {
                offsets[ i ][ 0 ] = distance( latitude, longitude, 0, sectors.get( i ).getLatitude(), longitude, 0, METER ) * Double.compare( latitude, sectors.get( i ).getLatitude() );
            }

            // offsetLon
            if ( Double.compare( sectors.get( i ).getLongitude(), longitude ) != 0 ) {
                offsets[ i ][ 1 ] = distance( latitude, longitude, 0, latitude, sectors.get( i ).getLongitude(), 0, METER ) * Double.compare( sectors.get( i ).getLongitude(), longitude );
            }

            // azimuthToPolarAngle
            offsets[ i ][ 2 ] = 90 - sectors.get( i ).getAzimuth() + ( 90 - sectors.get( i ).getAzimuth() <= 0 ? 360 : 0 );
        }

        for ( int i = 0, y = MAX_DISTANCE; i < 2 * MAX_DISTANCE + 1; i++, y-- ) {

            for ( int j = 0, x = -MAX_DISTANCE; j < 2 * MAX_DISTANCE + 1; j++, x++ ) {

                double intensity = 0;

                for ( int s = 0, sectorN = 1; s < sectors.size(); s++, sectorN++ ) {

                    CreateSectorDTO sector = sectors.get( s );

                    double polarAngle = atan2( y + offsets[ s ][ 0 ], x - offsets[ s ][ 1 ] ) * 360 / ( 2 * PI );
                    polarAngle += polarAngle < 0 ? 360 : 0;

                    if ( offsets[ s ][ 2 ] - 90 + ( offsets[ s ][ 2 ] - 90 < 0 ? 360 : 0 ) > polarAngle && ( offsets[ s ][ 2 ] + 90 ) % 360 < polarAngle ) {
                        continue;
                    }

                    double phi = polarAngle - offsets[ s ][ 2 ];

                    if ( phi > 90  ) {
                        phi -= 360;
                    } else if ( phi < -90 ) {
                        phi += 360;
                    }

                    H = sector.getHeight();
                    P = sector.getAntenna().getP();
                    G = sector.getAntenna().getG();
                    TL = sector.getAntenna().getTL();
                    EF = sector.getAntenna().getEF();
                    Q05H = sector.getAntenna().getQ05H();
                    Q05V = sector.getAntenna().getQ05V();

                    G = pow( 10, G / 10 );
                    TL = pow( 10, TL / 10 );

                    double teta = atan( ( H - dto.getHeight() ) / pow( pow( x - offsets[ s ][ 1 ], 2D ) + pow( y + offsets[ s ][ 0 ], 2D ), 0.5 ) ) * 180D / PI;

                    intensity += ( 8D * P * G * TL * EF * ( exp( -0.69D * pow( phi * 2D / Q05H, 2D ) ) ) * ( exp( -0.69D * pow( ( teta - sector.getDownTilt() ) * 2D / Q05V, 2D ) ) ) )
                                 / ( pow( x - offsets[ s ][ 1 ], 2D ) + pow( y + offsets[ s ][ 0 ], 2D ) + pow( H - dto.getHeight(), 2D ) );
                }

                if ( intensity >= 10 ) {
                    summary.add( new Point( x, y ) );
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
            Point2D next = direction.getNext( previous );

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