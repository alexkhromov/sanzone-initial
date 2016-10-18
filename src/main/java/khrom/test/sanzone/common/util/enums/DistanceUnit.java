package khrom.test.sanzone.common.util.enums;

/**
 * Created by DEV on 9/16/2016.
 */
public enum DistanceUnit {

    METER( 1000D ),
    KILOMETER( 1D );

    private double ratio;

    DistanceUnit( double ratio ) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }
}