package khrom.test.sanzone.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import ij.gui.Arrow;
import ij.process.ColorProcessor;
import khrom.test.sanzone.config.GoogleStaticMapConfig;
import khrom.test.sanzone.config.SessionSettings;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;
import net.sf.jasperreports.engine.JRException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static ij.gui.Arrow.NOTCHED;
import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.pow;
import static java.lang.Math.round;
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

    private static final String GOOGLE_STATIC_MAPS_API_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=%s&style=feature:poi|element:labels|visibility:off&style=feature:transit.station|element:labels|visibility:off&key=%s";
    private static final String GOOGLE_STATIC_MAPS_API_WITH_MARKERS_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=&markers=%s&key=%s";

    private static final String POLYLINE_PATH_PARAMETER = "path=fillcolor:0xAA000033|color:0xFFFFFF00|enc:%s";
    private static final String GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%s&size=%sx%s&scale=%s&maptype=%s&format=%s&language=%s&%s&key=%s";

    private static final String PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN = "/sanzone/%s";
    private static final String PATH_TO_STORAGE_WORK_DIRECTORY_MAPS_PATTERN = "/sanzone/%s/maps";
    private static final String PATH_TO_MAP_FILE_PATTERN = "/sanzone/%s/maps/%s.%s";
    private static final String PATH_TO_GOOGLE_MAP_IMAGE_FILE_PATTERN = "/sanzone/%s/%s_google_map.%s";
    private static final String PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN = "/sanzone/%s/%s_google_map_polyline.%s";
    private static final String PATH_TO_HORIZONTAL_DIAGRAM_FILE_PATTERN = "/sanzone/%s/%s_sanzone_H.%s";
    private static final String PATH_TO_VERTICAL_DIAGRAM_FILE_PATTERN = "/sanzone/%s/%s_sanzone_V.%s";
    private static final String PATH_TO_HORIZONTAL_DIAGRAM_TEST_FILE_PATTERN = "/sanzone/%s/%s_test_H.%s";
    private static final String PATH_TO_VERTICAL_DIAGRAM_TEST_FILE_PATTERN = "/sanzone/%s/%s_test_V.%s";

    @Autowired
    private GoogleStaticMapConfig googleStaticMapConfig;

    @Autowired
    private SessionSettings sessionSettings;

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
        plotSanzoneByPixelsDataForSector( sanzone, sector, ratioPixelToMeter, format( PATH_TO_HORIZONTAL_DIAGRAM_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ), session );
    }

    public void createSummarySanzone( CreateSanzoneRequest dto ) throws IOException {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        //TODO need to decide where put this logic
        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();
        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( latitude, longitude, METER );

        prepareSessionSettings( dto, sessionSettings );

        List< java.awt.Point > sanzoneH = calculateSanzoneForSummaryH( dto, sessionSettings );

        plotHorizontalDiagram( sanzoneH, ratioPixelToMeter,
                               format( PATH_TO_HORIZONTAL_DIAGRAM_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ),
                               format( PATH_TO_HORIZONTAL_DIAGRAM_TEST_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        //TODO this methods should be called for each sector from list
        sessionSettings.setSectorN( 1 );
        Map< Double, Set< Integer > > sanzoneV = calculateSanzoneForSummaryV( dto, sessionSettings );
        plotVerticalDiagram( sanzoneV, dto.getSectors(), 1, ratioPixelToMeter,
                             format( PATH_TO_VERTICAL_DIAGRAM_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ),
                             format( PATH_TO_VERTICAL_DIAGRAM_TEST_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {
            reportGeneratorService.generateReport( session, dto.getSectors() );
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getGoogleStaticMap( Double latitude, Double longitude, File map ) {

        try {

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, sessionSettings.getObjectsForCommonPattern() ) );

            BufferedImage image = ImageIO.read( url );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), map );

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

    private void getGoogleStaticMapWithPolylineForSummary( double [][] sanzone, List< CreateSectorDTO > sectors, File map ) {

        try {

            LatLng [] coordinates = getCoordinatesForSummary( sanzone, sectors, METER );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sectors.get( 0 ).getLatitude(), sectors.get( 0 ).getLongitude(), PolylineEncoding.encode( coordinates ) ) ) );

            BufferedImage image = ImageIO.read( url );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), map );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSector( double [][] sanzone, CreateSectorDTO sector, double ratioPixelToMeter, String fileName, String session ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        Polygon polygon = getPolygonForSector( sanzone, centerX, centerY, sector.getAzimuth(), ratioPixelToMeter );

        File file = new File( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        try {

            BufferedImage googleMap = ImageIO.read(file);

            BufferedImage sanzoneImg = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType(googleMap) );
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

    private void plotHorizontalDiagram( List< java.awt.Point > sanzone, double ratioPixelToMeter, String destFileName, String testFileName ) {

        int centerX = sessionSettings.getWidthCenter();
        int centerY = sessionSettings.getHeightCenter();

        try {

            int [] pixels = new int [ sessionSettings.getImageWidth() * sessionSettings.getImageHeight() ] ;

            for ( java.awt.Point point : sanzone ) {

                int xPoint = centerX + point.x;
                int yPoint = centerY - point.y;

                pixels[ yPoint * sessionSettings.getImageWidth() + xPoint ] = 0xFFFFFF;
            }

            BufferedImage sanzoneImg = new BufferedImage( sessionSettings.getImageWidth(), sessionSettings.getImageHeight(), TYPE_BYTE_GRAY );
            sanzoneImg.setRGB( 0, 0, sessionSettings.getImageWidth(), sessionSettings.getImageHeight(), pixels, 0, sessionSettings.getImageWidth() );

            ColorProcessor processor = new ColorProcessor( sanzoneImg );
            processor.scale( ratioPixelToMeter, ratioPixelToMeter );
            Graphics2D g2 = sanzoneImg.createGraphics();
            g2.drawImage( processor.createImage(), null, null );

            //TODO next 2 lines is only for test purpose ( modify method signature when testFileName become unnecessary )
            Path testPath = Files.createFile( Paths.get( testFileName ) );
            ImageIO.write( sanzoneImg, googleStaticMapConfig.getFormat(), testPath.toFile() );

            g2.dispose();

            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat sanzoneMatSrc = new Mat( sanzoneImg.getHeight(), sanzoneImg.getWidth(), CvType.CV_8UC1 );
            sanzoneMatSrc.put( 0, 0, ( ( DataBufferByte ) sanzoneImg.getRaster().getDataBuffer() ).getData() );

            Imgproc.threshold( sanzoneMatSrc, sanzoneMatSrc, 250.0, 255.0, Imgproc.THRESH_BINARY );

            List< MatOfPoint > contours = new ArrayList<>();
            Mat hierarchy = new Mat();

            Imgproc.findContours( sanzoneMatSrc, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point( 0, 0 ) );

            List< MatOfPoint2f > matOfPoint2fs = new ArrayList<>();

            BufferedImage googleMap;

            if ( googleStaticMapConfig.getMaxSize() > sessionSettings.getSizeX() ) {

                URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, sessionSettings.getObjectsForCommonPattern() ) );
                googleMap = ImageIO.read( url );

            } else {
                googleMap = getGoogleMap( ratioPixelToMeter );
            }

            g2 = googleMap.createGraphics();

            g2.setStroke( new BasicStroke( 3.0f, CAP_BUTT, CAP_ROUND ) );

            for ( MatOfPoint point : contours ) {

                MatOfPoint2f source = new MatOfPoint2f();
                point.convertTo( source, CvType.CV_32F );
                MatOfPoint2f approximated = new MatOfPoint2f();
                Imgproc.GaussianBlur( source, approximated, new Size( 3, 3 ), 0, 0 );
                matOfPoint2fs.add( approximated );
            }

            for ( int i = 0; i < contours.size(); i++ ) {

                Polygon polygon = new Polygon();

                MatOfPoint2f approximated = matOfPoint2fs.get( i );
                approximated.toList().stream().forEach( p -> polygon.addPoint( ( int ) p.x, ( int ) p.y ) );

                double [] contourHierarchy = hierarchy.get( 0, i );

                if ( contourHierarchy != null && ( int ) contourHierarchy[ 3 ] == -1 ) {

                    g2.setColor( Color.RED );
                    g2.setComposite( AlphaComposite.SrcOver.derive( 0.2f ) );

                    int child;
                    if ( ( child = ( int ) contourHierarchy[ 2 ] ) != -1 ) {

                        Area parent = new Area( polygon );
                        Polygon childArea = new Polygon();
                        MatOfPoint2f approximatedChild = matOfPoint2fs.get( child );
                        approximatedChild.toList().stream().forEach( p -> childArea.addPoint( ( int ) p.x, ( int ) p.y ) );
                        parent.subtract( new Area( childArea ) );

                        g2.fill( parent );

                    } else {
                        g2.fillPolygon( polygon );
                    }
                }

                g2.setColor( Color.BLACK );
                g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                g2.drawPolygon( polygon );
            }

            Image scaledSanzone = googleMap.getScaledInstance( sessionSettings.getResultSize(), sessionSettings.getResultSize(), SCALE_SMOOTH );

            BufferedImage result = new BufferedImage( sessionSettings.getResultSize(), sessionSettings.getResultSize(), TYPE_INT_RGB );

            g2.dispose();
            g2 = result.createGraphics();

            g2.drawImage( scaledSanzone, 0, 0, null );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            g2.setColor( Color.BLACK );
            g2.drawRect( 0, 0, result.getWidth() - 1, result.getHeight() - 1 );

            for ( int i = 40; i < result.getWidth(); i += 40 ) {

                g2.drawLine( i, 0, i, result.getHeight() - 1 );
                g2.drawLine( 0, i, result.getWidth() - 1, i );
            }

            g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
            g2.setColor( Color.RED );
            g2.fillOval( sessionSettings.getResultSize() / 2 - 3, sessionSettings.getResultSize() / 2 - 3, 6, 6 );

            Path path = Files.createFile( Paths.get( destFileName ) );

            ImageIO.write( result, googleStaticMapConfig.getFormat(), path.toFile() );

            // @formatter:off
            /*String pathParameters  = matOfPoint2fs
                                        .stream()
                                        .map( p2f -> p2f.toList().stream()
                                                                 .map( point -> new Point( ( centerY - point.y ) / ratioPixelToMeter, ( point.x - centerX ) / ratioPixelToMeter ) )
                                                                 .collect( Collectors.toList() ) )
                                        .map( list -> getCoordinatesForSummary( list, sectors, METER ) )
                                        .map( PolylineEncoding::encode )
                                        .collect( Collector.of( () -> new StringJoiner( "&" ),
                                                                ( j, p ) -> j.add( format( POLYLINE_PATH_PARAMETER, p ) ),
                                                                ( j1, j2 ) -> j1.merge( j2 ),
                                                                StringJoiner::toString  ) );*/
            // @formatter:on

            /*URL polyApi = new URL( format( GOOGLE_STATIC_MAPS_API_WITH_POLYLINE_URL_PATTERN,
                    googleStaticMapConfig.getObjectsForPolylinePattern( sectors.get( 0 ).getLatitude(), sectors.get( 0 ).getLongitude(), pathParameters ) ) );

            BufferedImage image = ImageIO.read( polyApi );

            Path map = Files.createFile( Paths.get( mapFileName ) );

            ImageIO.write( image, googleStaticMapConfig.getFormat(), map.toFile() );*/

        } catch ( IOException e ) {
        }
    }

    private void plotVerticalDiagram( Map< Double, Set< Integer > > sanzoneV, List< CreateSectorDTO > sectors,
                                      int sectorN, double ratioPixelToMeter, String destFileName, String testFileName ) {

        int resultWidth = 400;
        int resultHeight = 400;

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        double heightZoneMin = sanzoneV.keySet().stream().min( Double::compareTo ).get();
        double heightZoneMax = sanzoneV.keySet().stream().max( Double::compareTo ).get();
        double distanceZone = sanzoneV.values()
                .stream()
                .map( set -> set.stream().max( Integer::compareTo ).get() )
                .collect( Collectors.toList() )
                .stream().max( Integer::compareTo ).get();

        double distanceFactor = pow( 2, ( int ) distanceZone / 38 == 0 ? -1 : ( int ) distanceZone / 88 );
        double heightFactor = pow( 2, ( int ) heightZoneMax / 38 == 0 ? -1 : ( int ) heightZoneMax / 88 );

        int checkedHeightMin = centerY + ( int ) round( ( sectors.get( sectorN - 1 ).getHeight() - heightZoneMin ) * POINT_STEP );
        int checkedHeightFactor = 1;

        if ( checkedHeightMin >= googleStaticMapConfig.getImageHeight() ) {
            checkedHeightFactor += checkedHeightMin / googleStaticMapConfig.getImageHeight();
        }

        double scaleX = ratioPixelToMeter * 2 / distanceFactor;
        double scaleY = ratioPixelToMeter * 2 * checkedHeightFactor / ( POINT_STEP * heightFactor );

        int baseX = ( int ) round( centerX - ( int ) round( distanceZone * scaleX / 2 ) - googleStaticMapConfig.getImageWidth() / 10D );
        int baseY = ( int ) round( centerY - googleStaticMapConfig.getImageHeight() / 10D - sectors.get( sectorN - 1 ).getHeight() * ratioPixelToMeter * 2 / heightFactor );

        try {

            int [] pixels = new int [ googleStaticMapConfig.getImageWidth() * googleStaticMapConfig.getImageHeight() ] ;

            for ( Map.Entry< Double, Set< Integer > > entry: sanzoneV.entrySet() ) {

                int yPoint = centerY + ( int ) round( ( sectors.get( sectorN - 1 ).getHeight() - entry.getKey() ) * POINT_STEP / checkedHeightFactor );

                for ( Integer distance: entry.getValue() ) {

                    int xPoint = centerX - ( int ) round( distanceZone / 2 ) + distance;

                    try {
                        pixels[ yPoint * googleStaticMapConfig.getImageWidth() + xPoint ] = 0xFFFFFF;
                    } catch ( ArrayIndexOutOfBoundsException ex ) {
                        System.out.println( String.format( "[ xPoint, yPoint ] = [ %s, %s ]; height = %.2f; distance = %s", xPoint, yPoint, entry.getKey(), distance ) );
                        throw ex;
                    }
                }
            }

            double displayHeigth = heightZoneMin;
            double displayDistance = distanceZone;

            // convert meters to pixels
            heightZoneMin *= ratioPixelToMeter * 2;
            distanceZone *= ratioPixelToMeter * 2;

            BufferedImage sanzoneImg = new BufferedImage( googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), TYPE_BYTE_GRAY );
            sanzoneImg.setRGB( 0, 0, googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), pixels, 0, googleStaticMapConfig.getImageWidth() );

            ColorProcessor processor = new ColorProcessor( sanzoneImg );
            processor.setBackgroundValue( 1.0 );
            processor.scale( scaleX, scaleY );
            processor.translate( -baseX, baseY );

            Graphics2D g2 = sanzoneImg.createGraphics();
            g2.drawImage( processor.createImage(), null, null );

            //TODO next 2 lines is only for test purpose ( modify method signature when testFileName become unnecessary )
            Path testPath = Files.createFile( Paths.get( testFileName ) );
            ImageIO.write( sanzoneImg, googleStaticMapConfig.getFormat(), testPath.toFile() );

            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat sanzoneMatSrc = new Mat( sanzoneImg.getHeight(), sanzoneImg.getWidth(), CvType.CV_8UC1 );
            sanzoneMatSrc.put( 0, 0, ( ( DataBufferByte ) sanzoneImg.getRaster().getDataBuffer() ).getData() );

            Imgproc.threshold( sanzoneMatSrc, sanzoneMatSrc, 250.0, 255.0, Imgproc.THRESH_BINARY );

            List< MatOfPoint > contours = new ArrayList<>();
            Mat hierarchy = new Mat();

            Imgproc.findContours( sanzoneMatSrc, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point( 0, 0 ) );

            List< MatOfPoint2f > matOfPoint2fs = new ArrayList<>();

            BufferedImage diagram = new BufferedImage( googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), TYPE_INT_RGB );

            g2.dispose();
            g2 = diagram.createGraphics();

            g2.setColor( Color.WHITE );
            g2.fillRect ( 0, 0, diagram.getWidth(), diagram.getHeight() );
            g2.setStroke( new BasicStroke( 2.0f, CAP_BUTT, CAP_ROUND ) );

            for ( MatOfPoint point : contours ) {

                MatOfPoint2f source = new MatOfPoint2f();
                point.convertTo( source, CvType.CV_32F );
                MatOfPoint2f approximated = new MatOfPoint2f();

                Imgproc.GaussianBlur( source, approximated, new Size( 3, 3 ), 0, 0 );

                matOfPoint2fs.add( approximated );
            }

            for ( int i = 0; i < matOfPoint2fs.size(); i++ ) {

                Polygon polygon = new Polygon();

                MatOfPoint2f approximated = matOfPoint2fs.get( i );
                approximated.toList().stream().forEach( p -> polygon.addPoint( ( int ) p.x, ( int ) p.y ) );

                double [] contourHierarchy = hierarchy.get( 0, i );

                if ( contourHierarchy != null && ( int ) contourHierarchy[ 3 ] == -1 ) {

                    g2.setColor( Color.RED );
                    g2.setComposite( AlphaComposite.SrcOver.derive( 0.2f ) );

                    int child;
                    if ( ( child = ( int ) contourHierarchy[ 2 ] ) != -1 ) {

                        Area parent = new Area( polygon );
                        Polygon childArea = new Polygon();
                        MatOfPoint2f approximatedChild = matOfPoint2fs.get( child );
                        approximatedChild.toList().stream().forEach( p -> childArea.addPoint( ( int ) p.x, ( int ) p.y ) );
                        parent.subtract( new Area( childArea ) );

                        g2.fill( parent );

                    } else {
                        g2.fillPolygon( polygon );
                    }
                }

                g2.setColor( Color.BLACK );
                g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                g2.drawPolygon( polygon );
            }

            Image scaledSanzone = diagram.getScaledInstance( resultWidth, resultHeight, SCALE_SMOOTH );

            BufferedImage result = new BufferedImage( resultWidth, resultHeight, TYPE_INT_RGB );

            g2.dispose();
            g2 = result.createGraphics();

            g2.drawImage( scaledSanzone, 0, 0, null );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            g2.setColor( Color.BLACK );
            g2.drawRect( 0, 0, result.getWidth() - 1, result.getHeight() - 1 );

            for ( int i = 40; i < result.getWidth(); i += 40 ) {

                if ( result.getWidth() / i == 10 ) {
                    g2.drawLine( 0, i, result.getWidth(), i );
                    g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                    Arrow arrow = new Arrow( i, result.getHeight(), i, 0 );
                    arrow.setStyle( NOTCHED );
                    arrow.setStrokeWidth( 3.0 );
                    arrow.setFillColor( Color.BLACK );
                    arrow.drawOverlay( g2 );
                    g2.dispose();
                    g2 = result.createGraphics();
                    g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
                    g2.setColor( Color.BLACK );
                    continue;
                }

                if ( result.getWidth() - i == 40 ) {
                    g2.drawLine( i, 0, i, result.getHeight() );
                    g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                    Arrow arrow = new Arrow( 0, i, result.getWidth(), i );
                    arrow.setStyle( NOTCHED );
                    arrow.setStrokeWidth( 3.0 );
                    arrow.setFillColor( Color.BLACK );
                    arrow.drawOverlay( g2 );
                    g2.dispose();
                    g2 = result.createGraphics();
                    g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
                    g2.setColor( Color.BLACK );
                    continue;
                }

                g2.drawLine( i, 0, i, result.getHeight() );
                g2.drawLine( 0, i, result.getWidth(), i );
            }

            g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );

            g2.setColor( Color.RED );
            g2.fillOval( ( int ) round( resultWidth / 10D ) - 3,
                         ( int ) round( resultHeight / 2D + resultHeight * baseY / ( 2D * centerY ) ) - 3,
                         6, 6 );

            g2.setStroke( new BasicStroke( 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float [] { 20, 10 }, 0 ) );
            g2.setColor( Color.GREEN );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            // +1 / -1 used for correction of stroke width
            g2.drawLine( 0, ( int ) round( resultHeight * 9 / 10D ) - ( int ) round( resultHeight * heightZoneMin / ( 2D * centerY * heightFactor ) ) + 1,
                         result.getWidth(), ( int ) round( resultHeight * 9 / 10D ) - ( int ) round( resultHeight * heightZoneMin / ( 2D * centerY * heightFactor ) ) + 1 );
            g2.drawLine( ( int ) round( resultWidth / 10D ) + ( int ) round( resultWidth * distanceZone / ( 2D * centerX * distanceFactor ) ) + 1, 0,
                         ( int ) round( resultWidth / 10D ) + ( int ) round( resultWidth * distanceZone / ( 2D * centerX * distanceFactor ) ) + 1, result.getHeight() );

            Path path = Files.createFile( Paths.get( destFileName ) );

            ImageIO.write( result, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }

    private BufferedImage getGoogleMap( double ratioPixelToMeter ) {

        BufferedImage googleMap = new BufferedImage( sessionSettings.getImageWidth(), sessionSettings.getImageHeight(), TYPE_INT_RGB );

        Graphics2D g2 = googleMap.createGraphics();

        double startDistance = pow( 2 * pow( googleStaticMapConfig.getSizeX() *
                                             googleStaticMapConfig.getScale() *
                                             ( sessionSettings.getSessionScale() - 1 ) / ( 2 * ratioPixelToMeter ), 2 ), 0.5 );
        double incrementDistance = googleStaticMapConfig.getSizeX() * googleStaticMapConfig.getScale() / ratioPixelToMeter ;

        LatLng start = getLatLng( sessionSettings.getCenter().lat, sessionSettings.getCenter().lng, startDistance, 315, METER );

        for ( int i = 0; i < sessionSettings.getSessionScale(); i++ ) {

            LatLng next = new LatLng( start.lat, start.lng );

            for ( int j = 0; j < sessionSettings.getSessionScale(); j++ ) {

                try {

                    URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, googleStaticMapConfig.getObjectsForCommonPattern( next.lat, next.lng, 1 ) ) );
                    BufferedImage temp = ImageIO.read( url );

                    g2.drawImage( temp, j * googleStaticMapConfig.getImageWidth(), i * googleStaticMapConfig.getImageWidth(), null );

                } catch (IOException e) {
                    e.printStackTrace();
                }

                next = getLatLng( next.lat, next.lng, incrementDistance, 90, METER );
            }

            start = getLatLng( start.lat, start.lng, incrementDistance, 180, METER );
        }

        g2.dispose();

        return googleMap;
    }
}