package khrom.test.sanzone.common.util.enums;

import javafx.geometry.Point2D;

/**
 * Created by DEV on 10/17/2016.
 */
public enum SearchDirection {

    NORTH( -1, 0, "NORTH_WEST", "NORTH_EAST" ),
    NORTH_EAST( -1, 1, "NORTH_WEST", "EAST" ),
    EAST( 0, 1, "NORTH_EAST", "SOUTH_EAST" ),
    SOUTH_EAST( 1, 1, "NORTH_EAST", "SOUTH" ),
    SOUTH( 1, 0, "SOUTH_EAST", "SOUTH_WEST" ),
    SOUTH_WEST( 1, -1, "SOUTH_EAST", "WEST" ),
    WEST( 0, -1, "SOUTH_WEST", "NORTH_WEST" ),
    NORTH_WEST( -1, -1, "SOUTH_WEST", "NORTH" );

    private int offsetX;
    private int offsetY;
    private String nextPointDirection;
    private String rotatePointDirection;

    SearchDirection( int offsetX, int offsetY, String nextPointDirection, String rotatePointDirection ) {

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.nextPointDirection = nextPointDirection;
        this.rotatePointDirection = rotatePointDirection;
    }

    public Point2D getNext( Point2D previous ) {

        return new Point2D( previous.getX() + offsetX, previous.getY() + offsetY );
    }

    public SearchDirection getNextPointDirection() {
        return SearchDirection.valueOf( nextPointDirection );
    }

    public SearchDirection getRotatePointDirection() {
        return SearchDirection.valueOf( rotatePointDirection );
    }
}