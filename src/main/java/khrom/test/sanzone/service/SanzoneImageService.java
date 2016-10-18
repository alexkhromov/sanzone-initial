package khrom.test.sanzone.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import khrom.test.sanzone.config.GoogleStaticMapConfig;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        getGoogleStaticMapWithPolylineForSector( sector, session );

        // This call is optional. It is draw sanzone border using "image pixels" approach.
        // It is also insure that plot from "coordinates approach" draw same image as "image pixels"
        plotSanzoneByPixelsDataForSector( sector, ratioPixelToMeter, format(PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat()), session );
    }

    public void createSummarySanzoneImage( CreateSanzoneRequest dto ) {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        // TODO need to resolve situation when coordinates are different for sectors!!!
        // TODO suggestion: as a center get first sector and calculate offsets for other sectors
        // TODO             and after rotation apply them in MapUtil.calculateSummarySanzone()
        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        // no matter which of sector's coordinates used for this ratio:
        // - same image width, zoom and scale is used for calculation
        // - actually this ratio can be assigned to constant variables
        // - but we still need to know how to calculate it
        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( latitude, longitude, METER );

        getGoogleStaticMapWithPolylineForSummary( dto.getSectors(), session );

        // This call is optional. It is draw sanzone border using "image pixels" approach.
        // It is also insure that plot from "coordinates approach" draw same image as "image pixels"
        plotSanzoneByPixelsDataForSummary( dto.getSectors(), ratioPixelToMeter, format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ), session);

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

    private void getGoogleStaticMapWithPolylineForSector( CreateSectorDTO sector, String session ) {

        try {

            LatLng [] coordinates = getCoordinatesForSector( sector, METER );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sector.getLatitude(), sector.getLongitude(), PolylineEncoding.encode( coordinates ) ) ) );

            BufferedImage image = ImageIO.read( url );

            Path path = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void getGoogleStaticMapWithPolylineForSummary( List< CreateSectorDTO > sectors, String session ) {

        try {

            LatLng [] coordinates = getCoordinatesForSummary( sectors, METER );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sectors.get( 0 ).getLatitude(), sectors.get( 0 ).getLongitude(), PolylineEncoding.encode( coordinates ) ) ) );

            BufferedImage image = ImageIO.read( url );

            Path path = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSector( CreateSectorDTO sector, double ratioPixelToMeter, String fileName, String session ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        Polygon polygon = getPolygonForSector( calculateSanzoneForSector( sector ), centerX, centerY, sector.getAzimuth(), ratioPixelToMeter );

        File file = new File( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {

            BufferedImage googleMap = ImageIO.read( file );

            BufferedImage sanzone = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType( googleMap ) );
            Graphics2D g = sanzone.createGraphics();

            Color border = new Color( 255, 0, 0, 127 );

            g.drawImage( googleMap, null, 0, 0 );
            g.setColor( border );
            g.fillOval( centerX - 3, centerY - 3, 6, 6 );
            g.drawPolygon( polygon );

            Path path = Files.createFile( Paths.get( fileName ) );

            ImageIO.write( sanzone, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSummary( List< CreateSectorDTO > sectors, double ratioPixelToMeter, String fileName, String session ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        File file = new File( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {

            BufferedImage googleMap = ImageIO.read( file );

            BufferedImage sanzone = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType( googleMap ) );
            Graphics2D g = sanzone.createGraphics();

            Color border = new Color( 255, 0, 0, 127 );

            g.drawImage( googleMap, null, 0, 0 );
            g.setColor( border );
            g.fillOval( centerX - 3, centerY - 3, 6, 6 );

            double [][] summary = calculateSanzoneForSummary( sectors );

            for ( int i = 0; i < summary.length; i++ ) {

                for ( int j = 0; j < summary[ i ].length; j++ ) {

                    if ( summary[ i ][ j ] >= 10 ) {

                        int xPoint = centerX + ( int ) ( ( j - ( summary[ i ].length / 2 ) ) * ratioPixelToMeter );
                        int yPoint = centerY + ( int ) ( ( i - ( summary.length / 2 ) ) * ratioPixelToMeter );

                        g.fillOval( xPoint - 1, yPoint - 1, 3, 3 );
                    }
                }
            }

            Path path = Files.createFile( Paths.get( fileName ) );

            ImageIO.write( sanzone, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }
}