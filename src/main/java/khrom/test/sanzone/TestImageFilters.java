package khrom.test.sanzone;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by DEV on 11/9/2016.
 */
public class TestImageFilters {

    public static void main( String[] args ) {

        try{

            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            Mat sanzoneMatSrc = Imgcodecs.imread( "C:\\sanzone\\3895c5f9-d062-4eba-b790-f2fb8db2d4d2\\3895c5f9-d062-4eba-b790-f2fb8db2d4d2_test.jpg",  Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE );

            Imgproc.threshold( sanzoneMatSrc, sanzoneMatSrc, 100.0, 255.0, Imgproc.THRESH_BINARY );

            Mat sanzoneMatDst = sanzoneMatSrc.clone();
            //sanzoneMatDst.release();

            //Imgproc.dilate( sanzoneMatSrc, sanzoneMatDst, Imgproc.getStructuringElement( Imgproc.MORPH_ELLIPSE, new Size( 3, 3 ) ) );
            //Imgcodecs.imwrite( "dilation.jpg", sanzoneMatDst );

            List< MatOfPoint > contours = new ArrayList<>();

            Imgproc.findContours( sanzoneMatDst, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point( 0, 0 ) );

            Mat contoursIm = new Mat( sanzoneMatDst.width(), sanzoneMatDst.height(), CvType.CV_8UC1 );
            Imgproc.drawContours( contoursIm, contours, -1, new Scalar( 255 ), 1 );
            Imgcodecs.imwrite( "contoursIm.jpg", contoursIm );

            BufferedImage test = new BufferedImage( sanzoneMatDst.width(), sanzoneMatDst.height(), BufferedImage.TYPE_3BYTE_BGR );
            Graphics2D g2 = test.createGraphics();

            g2.setColor( Color.WHITE );
            g2.setStroke( new BasicStroke( 3.0f, CAP_BUTT, CAP_ROUND ) );

            for ( MatOfPoint point: contours ) {

                Polygon polygon = new Polygon();

                if ( Imgproc.contourArea( point ) < 32 ) {
                    continue;
                }

                System.out.println( Imgproc.contourArea( point ) );

                /*MatOfPoint source = new MatOfPoint();
                source.fromList( point.toList() );
                MatOfPoint approximated = new MatOfPoint();
                Imgproc.blur( source, approximated, new Size( 3, 3 ), new Point( -1, -1 ) );*/

                MatOfPoint2f source = new MatOfPoint2f();
                point.convertTo( source, CvType.CV_32F );
                MatOfPoint2f approximated = new MatOfPoint2f();
                Imgproc.GaussianBlur( source, approximated, new Size( 11, 11 ), 0, 0 );

                approximated.toList().stream().forEach( p -> polygon.addPoint( ( int ) p.x, ( int ) p.y ) );

                g2.drawPolygon( polygon );
            }

            Image scaledSanzone = test.getScaledInstance( 400, 400, SCALE_SMOOTH );

            BufferedImage result = new BufferedImage( 400, 400, TYPE_INT_RGB );

            g2.dispose();
            g2 = result.createGraphics();

            g2.drawImage( scaledSanzone, 0, 0, null );
            g2.setComposite( AlphaComposite.SrcOver.derive( 0.5f ) );
            g2.setColor( Color.WHITE );
            g2.drawRect( 0, 0, result.getWidth() - 1, result.getHeight() - 1 );

            for ( int i = 40; i < result.getWidth(); i += 40 ) {

                g2.drawLine( i, 0, i, result.getHeight() - 1 );
                g2.drawLine( 0, i, result.getWidth() - 1, i );
            }

            g2.setComposite( AlphaComposite.SrcOver.derive( 1.0f ) );
            g2.setColor( Color.RED );
            g2.fillOval( 200 - 3, 200 - 3, 6, 6 );

            Files.deleteIfExists( Paths.get( "test.jpg" ) );
            Path path = Files.createFile( Paths.get( "test.jpg" ) );

            ImageIO.write( result, "jpg", path.toFile() );

        }catch ( Exception e ) {
            System.out.println( "error: " + e.getMessage() );
        }
    }
}