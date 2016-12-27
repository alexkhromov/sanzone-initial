package khrom.test.sanzone.service;

import khrom.test.sanzone.config.SessionSettings;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;
import static java.util.Arrays.asList;
import static khrom.test.sanzone.common.util.MapUtil.distance;
import static khrom.test.sanzone.common.util.enums.DistanceUnit.METER;

/**
 * Created by DEV on 12/27/2016.
 */
@Service
public class SanzoneCalculationService {

    private static final int MAX_DISTANCE = 200;
    private static final int POINT_STEP = 5;

    public List< Point > calculateSanzoneForSummaryH( CreateSanzoneRequest dto, SessionSettings settings ) {

        List< Point > summary = new ArrayList<>();

        double H, P, G, TL, EF, Q05H, Q05V;

        double latitude = settings.getCenter().lat;
        double longitude = settings.getCenter().lng;

        List<CreateSectorDTO> sectors = dto.getSectors();

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

        for ( int i = 0, y = settings.getDistance(); i < 2 * settings.getDistance() + 1; i++, y-- ) {

            for ( int j = 0, x = -settings.getDistance(); j < 2 * settings.getDistance() + 1; j++, x++ ) {

                double intensity = 0;

                for ( int s = 0; s < sectors.size(); s++ ) {

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

                    double teta = atan( ( H - dto.getHeightM() ) / pow( pow( x - offsets[ s ][ 1 ], 2D ) + pow( y + offsets[ s ][ 0 ], 2D ), 0.5 ) ) * 180D / PI;

                    intensity += ( 8D * P * G * TL * EF * exp( -0.69D * pow( phi * 2D / Q05H, 2D ) ) * exp( -0.69D * pow( ( teta - sector.getDownTilt() ) * 2D / Q05V, 2D ) ) )
                            / ( pow( x - offsets[ s ][ 1 ], 2D ) + pow( y + offsets[ s ][ 0 ], 2D ) + pow( H - dto.getHeightM(), 2D ) );
                }

                if ( intensity >= 10 ) {
                    summary.add( new Point( x, y ) );
                }
            }
        }

        return summary;
    }

    public Map< Double, Set< Double > > calculateSanzoneForSummaryV( CreateSanzoneRequest dto, SessionSettings sessionSettings ) {

        Map< Double, Set< Double > > summary = new HashMap<>();

        double H, P, G, TL, EF, Q05H, Q05V;

        double latitude = dto.getSectors().get( sessionSettings.getSectorN() - 1 ).getLatitude();
        double longitude = dto.getSectors().get( sessionSettings.getSectorN() - 1 ).getLongitude();
        double height = dto.getSectors().get( sessionSettings.getSectorN() - 1 ).getHeight();
        double polarAngleM = 90 - dto.getAzimuthM() + ( 90 - dto.getAzimuthM() <= 0 ? 360 : 0 );

        List< CreateSectorDTO > sectors = dto.getSectors();

        DecimalFormat df = new DecimalFormat( "#.##" );

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

        summary.put( height, new HashSet<>( asList( 0.0 ) ) );

        //TODO get max height from sessionSettings
        for ( int h = 0; h < ( height + MAX_DISTANCE / 2 ) * POINT_STEP; h++ ) {

            for ( int d = 0; d < sessionSettings.getDistance() * POINT_STEP; d++ ) {

                double intensity = 0;

                double X = cos( toRadians( polarAngleM ) ) * d / POINT_STEP;
                double Y = sin( toRadians( polarAngleM ) ) * d / POINT_STEP;

                for ( int s = 0; s < sectors.size(); s++ ) {

                    CreateSectorDTO sector = sectors.get( s );

                    double polarAngle = atan2( Y + offsets[ s ][ 0 ], X - offsets[ s ][ 1 ] ) * 360 / ( 2 * PI );
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

                    double teta = atan( ( H - ( double ) h / POINT_STEP ) / pow( pow( X - offsets[ s ][ 1 ], 2D ) + pow( Y + offsets[ s ][ 0 ], 2D ), 0.5 ) ) * 180D / PI;

                    intensity += ( 8D * P * G * TL * EF * exp( -0.69D * pow( phi * 2D / Q05H, 2D ) ) * exp( -0.69D * pow( ( teta - sector.getDownTilt() ) * 2D / Q05V, 2D ) ) )
                            / ( pow( X - offsets[ s ][ 1 ], 2D ) + pow( Y + offsets[ s ][ 0 ], 2D ) + pow( H - ( double ) h / POINT_STEP, 2D ) );
                }

                if ( intensity >= 10 ) {

                    double distance = Double.valueOf( df.format( pow( pow( X, 2D ) + pow( Y , 2D ), 0.5 ) ) );

                    if ( summary.containsKey( ( double ) h / POINT_STEP ) ) {
                        summary.get( ( double ) h / POINT_STEP ).add( distance );
                    } else {
                        summary.put( ( double ) h / POINT_STEP, new HashSet<>( asList( distance ) ) );
                    }
                }
            }
        }

        return summary;
    }

}