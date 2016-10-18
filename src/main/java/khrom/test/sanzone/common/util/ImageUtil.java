package khrom.test.sanzone.common.util;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.*;

/**
 * Created by DEV on 9/13/2016.
 */
public class ImageUtil {

    public static int getImageType( BufferedImage image ) {

        switch ( image.getType() ) {

            case TYPE_CUSTOM:

                if ( image.getAlphaRaster() != null ) {

                    return TYPE_INT_ARGB_PRE;

                } else {

                    return TYPE_INT_RGB;
                }

            case TYPE_BYTE_BINARY:
            case TYPE_BYTE_INDEXED:

                if ( image.getColorModel().hasAlpha( )) {

                    return TYPE_INT_ARGB_PRE;

                } else {

                    // Handle non-alpha variant
                    return TYPE_INT_RGB;
                }
        }

        return image.getType();
    }
}