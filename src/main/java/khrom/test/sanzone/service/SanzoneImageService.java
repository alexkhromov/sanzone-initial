package khrom.test.sanzone.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import ij.process.ColorProcessor;
import khrom.test.sanzone.config.GoogleStaticMapConfig;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static khrom.test.sanzone.common.util.ImageUtil.getImageType;
import static khrom.test.sanzone.common.util.MapUtil.*;
import static khrom.test.sanzone.common.util.enums.DistanceUnit.METER;

/**
 * Created by DEV on 9/12/2016.
 *
 * TODO need to double check: how to deal with default settings and custom settings
 */
@Service
public class SanzoneImageService {

    private static final String GOOGLE_STATIC_MAPS_API_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=%s&key=%s";
    private static final String GOOGLE_STATIC_MAPS_API_WITH_MARKERS_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=&markers=%s&key=%s";
    private static final String GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=%s&path=fillcolor:0xAA000033|color:0xFFFFFF00|enc:%s&key=%s";

    private static final String PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN = "/sanzone/%s";
    private static final String PATH_TO_GOOGLE_MAP_IMAGE_FILE_PATTERN = "/sanzone/%s/%s_google_map.%s";
    private static final String PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN = "/sanzone/%s/%s_google_map_polyline.%s";
    private static final String PATH_TO_SANZONE_FILE_PATTERN = "/sanzone/%s/%s_sanzone.%s";

    @Autowired
    private GoogleStaticMapConfig googleStaticMapConfig;

    @Autowired
    private ReportGeneratorService reportGeneratorService;

    public void createSectorSanzoneImage( CreateSanzoneRequest dto ) {

        String session = UUID.randomUUID().toString();

        CreateSectorDTO sector = dto.getSectors().get( 0 );

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( sector.getLatitude(), sector.getLongitude(), METER );

        double [][] sanzone = calculateSanzoneForSector( sector );

        getGoogleStaticMapWithPolylineForSector( sanzone, sector, session );

        // This call is optional. It is draw sanzone border using "image pixels" approach.
        // It is also insure that plot from "coordinates approach" draw same image as "image pixels"
        plotSanzoneByPixelsDataForSector( sanzone, sector, ratioPixelToMeter, format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ), session );
    }

    public void createSummarySanzoneImage( CreateSanzoneRequest dto ) {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        // TODO issue: we need to resolve situation when coordinates are different for sectors!!!
        // TODO suggestion: as a center get first sector and calculate offsets for other sectors
        // TODO             and after rotation apply them in MapUtil.calculateSummarySanzone()
        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        // no matter which of sector's coordinates used for this ratio:
        // - same image width, zoom and scale is used for calculation
        // - actually this ratio can be assigned to constant variables
        // - but we still need to know how to calculate it
        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( latitude, longitude, METER );

        double [][] sanzone = calculateSanzoneForSummary( dto.getSectors() );

        getGoogleStaticMapWithPolylineForSummary( sanzone, dto.getSectors(), session );

        getGoogleStaticMap( latitude, longitude, session );

        // This call is optional. It is draw sanzone border using "image pixels" approach.
        // It is also insure that plot from "coordinates approach" draw same image as "image pixels"
        plotSanzoneByPixelsDataForSummary( sanzone, ratioPixelToMeter, format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ), session);

        try {
            reportGeneratorService.generateReport( session, dto.getSectors() );
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getGoogleStaticMap( Double latitude, Double longitude, String session ) {

        try {

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, googleStaticMapConfig.getObjectsForCommonPattern( latitude, longitude ) ) );

            BufferedImage image = ImageIO.read( url );

            Path path = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void getGoogleStaticMapWithPolylineForSector( double [][] sanzone, CreateSectorDTO sector, String session ) {

        try {

            LatLng [] coordinates = getCoordinatesForSector( sanzone, sector, METER );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sector.getLatitude(), sector.getLongitude(), PolylineEncoding.encode( coordinates ) ) ) );

            BufferedImage image = ImageIO.read( url );

            Path path = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void getGoogleStaticMapWithPolylineForSummary( double [][] sanzone, List< CreateSectorDTO > sectors, String session ) {

        try {

            LatLng [] coordinates = getCoordinatesForSummary( sanzone, sectors, METER );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sectors.get( 0 ).getLatitude(), sectors.get( 0 ).getLongitude(), PolylineEncoding.encode( coordinates ) ) ) );

            BufferedImage image = ImageIO.read( url );

            Path path = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSector( double [][] sanzone, CreateSectorDTO sector, double ratioPixelToMeter, String fileName, String session ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        Polygon polygon = getPolygonForSector( sanzone, centerX, centerY, sector.getAzimuth(), ratioPixelToMeter );

        File file = new File( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {

            BufferedImage googleMap = ImageIO.read( file );

            BufferedImage sanzoneImg = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType( googleMap ) );
            Graphics2D g = sanzoneImg.createGraphics();

            Color border = new Color( 255, 0, 0, 127 );

            g.drawImage( googleMap, null, 0, 0 );
            g.setColor( border );
            g.fillOval( centerX - 3, centerY - 3, 6, 6 );
            g.drawPolygon( polygon );

            Path path = Files.createFile( Paths.get( fileName ) );

            ImageIO.write( sanzoneImg, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSummary( double [][] sanzone, double ratioPixelToMeter, String fileName, String session ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        File file = new File( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {

            BufferedImage googleMap = ImageIO.read( file );

            BufferedImage sanzoneImg = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType( googleMap ) );
            Graphics2D g = sanzoneImg.createGraphics();

            Color border = new Color( 255, 0, 0, 127 );

            g.drawImage( googleMap, null, 0, 0 );
            g.setColor( border );
            g.fillOval( centerX - 3, centerY - 3, 6, 6 );

            //TODO issue: we need to determine how to handle and draw sanzone and border
            //TODO        when sectors coordinates are different!!!
            boolean coordinatesMatch = true;

            if ( coordinatesMatch ) {

                Polygon polygon = getPolygonForSummary( getBorderPointsForSummary( sanzone ), centerX, centerY, ratioPixelToMeter );

                g.drawPolygon( polygon );

            } else {

                for ( int i = 0; i < sanzone.length; i++ ) {

                    for ( int j = 0; j < sanzone[ i ].length; j++ ) {

                        if ( sanzone[ i ][ j ] >= 10 ) {

                            int xPoint = centerX + ( int ) ( ( j - ( sanzone[ i ].length / 2 ) ) * ratioPixelToMeter );
                            int yPoint = centerY + ( int ) ( ( i - ( sanzone.length / 2 ) ) * ratioPixelToMeter );

                            g.fillOval( xPoint - 1, yPoint - 1, 3, 3 );
                        }
                    }
                }
            }

            Path path = Files.createFile( Paths.get( fileName ) );

            ImageIO.write( sanzoneImg, googleStaticMapConfig.getFormat(), path.toFile() );

            boolean fillSanzone = false;

            // STEP # 1 prepare sanzone image
            Color inside = new Color( 255, 0, 0, 255 );
            Color outside = new Color( 255, 255, 255, 255 );
            Color border2 = new Color( 0, 0, 0, 255 );

            int heightOffset = ( googleMap.getHeight() - sanzone.length ) / 2;
            int widthOffset = ( googleMap.getWidth() - sanzone[ 0 ].length ) / 2;

            BufferedImage sanzoneImg2 = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), BufferedImage.TYPE_INT_RGB );

            for ( int i = 0; i < googleMap.getHeight(); i++ ) {

                for ( int j = 0; j < googleMap.getWidth(); j++ ) {

                    if ( i <= heightOffset || i > sanzone.length - 1 + heightOffset ||
                            j <= widthOffset || j > sanzone[ i - heightOffset ].length - 1 + widthOffset ) {

                        sanzoneImg2.setRGB( j, i, outside.getRGB() );

                        continue;
                    }

                    if ( sanzone[ i - heightOffset ][ j - widthOffset ] >= 10 ) {
                        sanzoneImg2.setRGB( j, i, inside.getRGB() );
                    } else {
                        sanzoneImg2.setRGB( j, i, outside.getRGB() );
                    }
                }
            }

            ColorProcessor processor = new ColorProcessor( sanzoneImg2 );
            processor.blurGaussian( 3.0 );
            processor.scale( ratioPixelToMeter, ratioPixelToMeter );

            Graphics2D g2 = sanzoneImg2.createGraphics();

            g2.drawImage( processor.createImage(), null, null );

            g2.dispose();

            // STEP # 2 prepare map image
            BufferedImage map = ImageIO.read( new File( format( PATH_TO_GOOGLE_MAP_IMAGE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            // STEP # 3 combine map and sanzone
            BufferedImage combined = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), BufferedImage.TYPE_INT_RGB );

            g2 = combined.createGraphics();

            g2.drawImage( map, null, null );

            int transparentColor = outside.getRGB() | 0xFF000000;

            for ( int i = 0; i < sanzoneImg2.getHeight(); i++ ) {

                for ( int j = 0; j < sanzoneImg2.getWidth(); j++ ) {

                    if ( ( sanzoneImg2.getRGB( j, i ) | 0xFF000000 ) != transparentColor ) {

                        Color color = new Color( sanzoneImg2.getRGB( j, i ) );

                        if ( color.getGreen() < 150 && color.getBlue() < 150 ) {

                            if( color.getGreen() > 110 && color.getBlue() > 110 ) {

                                g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                                g2.setColor( border2 );
                                g2.fillOval( j - 1, i - 1, 3, 3 );

                            } else if ( fillSanzone ){

                                g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
                                g2.setColor( new Color( sanzoneImg2.getRGB( j, i ) ) );
                                g2.fillRect( j, i, 1, 1 );
                            }
                        }
                    }
                }
            }

            g2.dispose();

            Path path2 = Files.createFile( Paths.get( "C:\\sanzone\\" + session + "\\test.jpg" ) );

            ImageIO.write( combined, googleStaticMapConfig.getFormat(), path2.toFile() );

        } catch ( IOException e ) {
        }
    }
}