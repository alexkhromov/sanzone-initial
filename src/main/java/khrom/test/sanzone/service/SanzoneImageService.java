package khrom.test.sanzone.service;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import ij.process.ColorProcessor;
import khrom.test.sanzone.config.GoogleStaticMapConfig;
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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
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
    private static final String PATH_TO_TEST_FILE_PATTERN = "/sanzone/%s/%s_test.%s";

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

    public void createSummarySanzoneImage( CreateSanzoneRequest dto ) throws IOException {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        //TODO issue: we need to resolve situation when coordinates are different for sectors!!!
        //TODO suggestion: as a center get first sector and calculate offsets for other sectors
        //TODO             and after rotation apply them in MapUtil.calculateSummarySanzone()
        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        // no matter which of sector's coordinates used for this ratio:
        // - same image width, zoom and scale is used for calculation
        // - actually this ratio can be assigned to constant variables
        // - but we still need to know how to calculate it
        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( latitude, longitude, METER );

        double [][] sanzone = calculateSanzoneForSummary( dto.getSectors() );

        Path map = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_WITH_POLYLINE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );
        Path destination = Files.createFile( Paths.get( format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

        getGoogleStaticMapWithPolylineForSummary( sanzone, dto.getSectors(), map.toFile() );
        // This call is optional. It is draw sanzone border using "image pixels" approach.
        // It is also insure that plot from "coordinates approach" draw same image as "image pixels"
        plotSanzoneByPixelsDataForSummary( sanzone, ratioPixelToMeter, map.toFile(), destination.toFile() );

        try {
            reportGeneratorService.generateReport( session, dto.getSectors() );
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSummarySanzoneImageWithColorProcessing( CreateSanzoneRequest dto ) throws IOException {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance(latitude, longitude, METER);

        double [][] sanzone = calculateSanzoneForSummary( dto.getSectors() );

        Path map = Files.createFile( Paths.get( format( PATH_TO_GOOGLE_MAP_IMAGE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );
        Path destination = Files.createFile( Paths.get( format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) ) );

        getGoogleStaticMap(latitude, longitude, map.toFile());
        plotSanzoneByPixelsDataForSummaryWithColorProcessing( sanzone, ratioPixelToMeter, map.toFile(), destination.toFile() );

        try {
            reportGeneratorService.generateReport( session, dto.getSectors() );
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSummarySanzoneImageWithOpenCV( CreateSanzoneRequest dto ) throws IOException {

        String session = UUID.randomUUID().toString();

        try {

            Files.createDirectories( Paths.get( format( PATH_TO_STORAGE_WORK_DIRECTORY_PATTERN, session ) ) );

        } catch ( IOException e ) {
        }

        double latitude = dto.getSectors().get( 0 ).getLatitude();
        double longitude = dto.getSectors().get( 0 ).getLongitude();

        double ratioPixelToMeter = googleStaticMapConfig.getRatioPixelToDistance( latitude, longitude, METER );

        double [][] sanzone = calculateSanzoneForSummaryV2( dto.getSectors() );

        plotSanzoneByPixelsDataForSummaryWithOpenCV( sanzone, dto.getSectors(), ratioPixelToMeter,
                                                     format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ),
                                                     format( PATH_TO_TEST_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

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

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, googleStaticMapConfig.getObjectsForCommonPattern( latitude, longitude ) ) );

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
                    googleStaticMapConfig.getObjectsForPolylinePattern(sectors.get(0).getLatitude(), sectors.get(0).getLongitude(), PolylineEncoding.encode(coordinates)) ) );

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

    private void plotSanzoneByPixelsDataForSummary( double [][] sanzone, double ratioPixelToMeter, File map, File destination ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        Color inside = new Color( 255, 0, 0, 255 );
        Color border = new Color( 0, 0, 0, 255 );

        try {

            BufferedImage googleMap = ImageIO.read( map );

            BufferedImage sanzoneImg = new BufferedImage( googleMap.getWidth(), googleMap.getHeight(), getImageType( googleMap ) );
            Graphics2D g2 = sanzoneImg.createGraphics();

            g2.drawImage( googleMap, null, 0, 0 );
            g2.setColor( border );

            //TODO issue: we need to determine how to handle and draw sanzone and border
            //TODO        when sectors coordinates are different!!!
            boolean coordinatesMatch = true;

            if ( coordinatesMatch ) {

                Polygon polygon = getPolygonForSummary( getBorderPointsForSummary( sanzone ), centerX, centerY, ratioPixelToMeter );

                g2.setStroke( new BasicStroke( 3.0f, CAP_BUTT, CAP_ROUND ) );
                g2.drawPolygon( polygon );

            } else {

                for ( int i = 0; i < sanzone.length; i++ ) {

                    for ( int j = 0; j < sanzone[ i ].length; j++ ) {

                        if ( sanzone[ i ][ j ] >= 10 ) {

                            int xPoint = centerX + ( int ) ( ( j - ( sanzone[ i ].length / 2 ) ) * ratioPixelToMeter );
                            int yPoint = centerY + ( int ) ( ( i - ( sanzone.length / 2 ) ) * ratioPixelToMeter );

                            g2.fillOval( xPoint - 1, yPoint - 1, 3, 3 );
                        }
                    }
                }
            }

            g2.dispose();

            Image scaledSanzone = sanzoneImg.getScaledInstance( 400, 400, SCALE_SMOOTH );
            BufferedImage result = new BufferedImage( 400, 400, TYPE_INT_RGB );

            g2 = result.createGraphics();

            g2.drawImage( scaledSanzone, 0, 0, null );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            g2.setColor( border);
            g2.drawRect( 0, 0 , result.getWidth() - 1, result.getHeight() - 1 );

            for ( int i = 40; i < result.getWidth(); i += 40 ) {

                g2.drawLine( i, 0, i, result.getHeight() - 1 );
                g2.drawLine( 0, i, result.getWidth() - 1, i );
            }

            g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
            g2.setColor( inside );
            g2.fillOval( 200 - 3, 200 - 3, 6, 6 );

            ImageIO.write( result, googleStaticMapConfig.getFormat(), destination );

            } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSummaryWithColorProcessing( double [][] sanzone, double ratioPixelToMeter, File map, File destination ) {

        boolean fillSanzone = false;

        Color inside = new Color( 255, 0, 0, 255 );
        Color outside = new Color( 255, 255, 255, 255 );
        Color border = new Color( 0, 0, 0, 255 );

        try {

            // STEP # 1 prepare sanzone image
            int widthOffset = ( googleStaticMapConfig.getImageWidth() - sanzone[ 0 ].length ) / 2;
            int heightOffset = ( googleStaticMapConfig.getImageHeight() - sanzone.length ) / 2;

            BufferedImage sanzoneImg = new BufferedImage( googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), TYPE_INT_RGB );

            for ( int i = 0; i < googleStaticMapConfig.getImageHeight(); i++ ) {

                for ( int j = 0; j < googleStaticMapConfig.getImageWidth(); j++ ) {

                    if ( i <= heightOffset || i > sanzone.length - 1 + heightOffset ||
                            j <= widthOffset || j > sanzone[ i - heightOffset ].length - 1 + widthOffset ) {

                        sanzoneImg.setRGB( j, i, outside.getRGB() );

                        continue;
                    }

                    if ( sanzone[ i - heightOffset ][ j - widthOffset ] >= 10 ) {
                        sanzoneImg.setRGB( j, i, inside.getRGB() );
                    } else {
                        sanzoneImg.setRGB( j, i, outside.getRGB() );
                    }
                }
            }

            ColorProcessor processor = new ColorProcessor( sanzoneImg );
            processor.blurGaussian( 3.0 );
            processor.scale( ratioPixelToMeter, ratioPixelToMeter );

            Graphics2D g2 = sanzoneImg.createGraphics();

            g2.drawImage( processor.createImage(), null, null );
            g2.dispose();

            // STEP # 2 prepare map image
            BufferedImage mapImage = ImageIO.read( map );

            // STEP # 3 combine sanzone and map
            BufferedImage combined = new BufferedImage( googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), TYPE_INT_RGB );

            g2 = combined.createGraphics();

            g2.drawImage( mapImage, null, null );

            int transparentColor = outside.getRGB() | 0xFF000000;

            for ( int i = 0; i < sanzoneImg.getHeight(); i++ ) {

                for ( int j = 0; j < sanzoneImg.getWidth(); j++ ) {

                    if ( ( sanzoneImg.getRGB( j, i ) | 0xFF000000 ) != transparentColor ) {

                        Color color = new Color( sanzoneImg.getRGB( j, i ) );

                        if ( color.getGreen() < 150 && color.getBlue() < 150 ) {

                            if( color.getGreen() > 110 && color.getBlue() > 110 ) {

                                g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                                g2.setColor( border );
                                g2.fillOval( j - 1, i - 1, 3, 3 );

                            } else if ( fillSanzone ){

                                g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
                                g2.setColor( new Color( sanzoneImg.getRGB( j, i ) ) );
                                g2.fillRect( j, i, 1, 1 );
                            }
                        }
                    }
                }
            }

            g2.dispose();

            Image scaledSanzone = combined.getScaledInstance( 400, 400, SCALE_SMOOTH );
            BufferedImage result = new BufferedImage( 400, 400, TYPE_INT_RGB );

            g2 = result.createGraphics();

            g2.drawImage( scaledSanzone, 0, 0, null );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            g2.setColor( border );
            g2.drawRect( 0, 0 , result.getWidth() - 1, result.getHeight() - 1 );

            for ( int i = 40; i < result.getWidth(); i += 40 ) {

                g2.drawLine( i, 0, i, result.getHeight() - 1 );
                g2.drawLine( 0, i, result.getWidth() - 1, i );
            }

            g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
            g2.setColor( inside );
            g2.fillOval( 200 - 3, 200 - 3, 6, 6 );
            g2.dispose();

            ImageIO.write( result, googleStaticMapConfig.getFormat(), destination );

        } catch ( IOException e ) {
        }
    }

    private void plotSanzoneByPixelsDataForSummaryWithOpenCV( double [][] sanzone, List< CreateSectorDTO > sectors, double ratioPixelToMeter, String destFileName, String testFileName ) {

        int centerX = googleStaticMapConfig.getWidthCenter();
        int centerY = googleStaticMapConfig.getHeightCenter();

        try {

            int [] pixels = new int [ googleStaticMapConfig.getImageWidth() * googleStaticMapConfig.getImageHeight() ] ;

            for ( int i = 0; i < sanzone.length; i++ ) {

                for ( int j = 0; j < sanzone[ i ].length; j++ ) {

                    if ( sanzone[ i ][ j ] >= 10 ) {

                        /*int xPoint = centerX + ( int ) ( ( j - ( sanzone[ i ].length / 2 ) ) * ratioPixelToMeter );
                        int yPoint = centerY + ( int ) ( ( i - ( sanzone.length / 2 ) ) * ratioPixelToMeter );*/
                        //TODO-improvement_#1: calculate sanzone image pixels ( comment block above and uncomment this if needed )
                        int xPoint = centerX + ( j - ( sanzone[ i ].length / 2 ) );
                        int yPoint = centerY + ( i - ( sanzone.length / 2 ) );

                        pixels[ yPoint * googleStaticMapConfig.getImageWidth() + xPoint ] = 0xFFFFFF;
                    }
                }
            }

            BufferedImage sanzoneImg = new BufferedImage( googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), TYPE_BYTE_GRAY );
            sanzoneImg.setRGB( 0, 0, googleStaticMapConfig.getImageWidth(), googleStaticMapConfig.getImageHeight(), pixels, 0, googleStaticMapConfig.getImageWidth() );

            //TODO-improvement_#1: scale whole image with imageJ processor and define what is the best ( uncomment block below if needed )
            ColorProcessor processor = new ColorProcessor( sanzoneImg );
            processor.scale( ratioPixelToMeter, ratioPixelToMeter );
            Graphics2D g2 = sanzoneImg.createGraphics();
            g2.drawImage( processor.createImage(), null, null );

            //TODO next 2 lines is only for test purpose ( modify method signature when testFileName become unnecessary )
            Path testPath = Files.createFile( Paths.get( testFileName ) );
            ImageIO.write( sanzoneImg, googleStaticMapConfig.getFormat(), testPath.toFile() );

            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

            Mat sanzoneMatSrc = new Mat( sanzoneImg.getHeight(), sanzoneImg.getWidth(), CvType.CV_8UC1 );
            sanzoneMatSrc.put( 0, 0, ( ( DataBufferByte ) sanzoneImg.getRaster().getDataBuffer() ).getData() );

            Imgproc.threshold( sanzoneMatSrc, sanzoneMatSrc, 250.0, 255.0, Imgproc.THRESH_BINARY );

            Mat sanzoneMatDst = sanzoneMatSrc.clone();
            //TODO-improvement_#1: need to optimize Mat objects when only blurring is used
            //sanzoneMatDst.release();

            //Imgproc.dilate( sanzoneMatSrc, sanzoneMatDst, Imgproc.getStructuringElement( Imgproc.MORPH_ELLIPSE, new Size( 3, 3 ) ) );
            //TODO-improvement_#1: we need erode scaled image because sanzone is white area ( comment dilation above and uncomment eroding below if needed )
            //TODO-improvement_#1: OR probably we need just skip eroding for scaled image and just find contours and blur them
            //TODO-improvement_#1: so in this case comment both: dilation and eroding; also need to optimize Mat objects probably we need just sanzoneMatSrc
            //Imgproc.erode( sanzoneMatSrc, sanzoneMatDst, Imgproc.getStructuringElement( Imgproc.MORPH_ELLIPSE, new Size( 3, 3 ) ) );

            List< MatOfPoint > contours = new ArrayList<>();

            Imgproc.findContours( sanzoneMatDst, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point( 0, 0 ) );

            URL url = new URL( format( GOOGLE_STATIC_MAPS_API_URL_PATTERN, googleStaticMapConfig.getObjectsForCommonPattern( sectors.get( 0 ).getLatitude(), sectors.get( 0 ).getLongitude() ) ) );

            BufferedImage googleMap = ImageIO.read( url );

            //Graphics2D g2 = googleMap.createGraphics();
            //TODO-improvement_#1: create graphics from proper buffered image ( comment g2 above and uncomment block below if needed )
            g2.dispose();
            g2 = googleMap.createGraphics();

            g2.setStroke( new BasicStroke( 3.0f, CAP_BUTT, CAP_ROUND ) );

            for ( MatOfPoint point: contours ) {

                Polygon polygon = new Polygon();

                /*if ( Imgproc.contourArea( point ) < 32 ) {
                    continue;
                }
                System.out.println( Imgproc.contourArea( point ) );*/

                /*MatOfPoint source = new MatOfPoint();
                source.fromList( point.toList() );
                MatOfPoint approximated = new MatOfPoint();*/
                //TODO-improvement_#1: for blur function use above source and for GaussianBlur use below source
                MatOfPoint2f source = new MatOfPoint2f();
                point.convertTo( source, CvType.CV_32F );
                MatOfPoint2f approximated = new MatOfPoint2f();

                //Imgproc.blur( source, approximated, new Size( 1, 43 ), new Point( -1, -1 ) );
                //TODO-improvement_#1: for blurring scaled image we need to modify size value ( comment blurring above and uncomment blurring below if needed )
                //TODO-improvement_#1: also for blurring we can use two functions blur or GaussianBlur ( just use proper source )
                //Imgproc.blur( source, approximated, new Size( 3, 3 ), new Point( -1, -1 ) );
                Imgproc.GaussianBlur( source, approximated, new Size( 3, 3 ), 0, 0 );

                approximated.toList().stream().forEach( p -> polygon.addPoint( ( int ) p.x, ( int ) p.y ) );

                g2.setColor( Color.RED );
                g2.setComposite( AlphaComposite.SrcOver.derive( 0.2f ) );
                g2.fillPolygon( polygon );

                g2.setColor( Color.BLACK );
                g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
                g2.drawPolygon( polygon );
            }

            Image scaledSanzone = googleMap.getScaledInstance( 400, 400, SCALE_SMOOTH );

            BufferedImage result = new BufferedImage( 400, 400, TYPE_INT_RGB );

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
            g2.fillOval( 200 - 3, 200 - 3, 6, 6 );

            Path path = Files.createFile( Paths.get( destFileName ) );

            ImageIO.write( result, googleStaticMapConfig.getFormat(), path.toFile() );

        } catch ( IOException e ) {
        }
    }
}