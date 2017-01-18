package khrom.test.sanzone.model.dto.create;

import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static khrom.test.sanzone.model.error.ErrorCode.FIELD_INVALID;
import static khrom.test.sanzone.model.error.ErrorCode.FIELD_MISSING;
import static khrom.test.sanzone.model.error.ErrorCode.FIELD_RANGE;

/**
 * Created by DEV on 9/29/2016.
 */
public class CreateSectorDTO {

    @NotNull( message = FIELD_MISSING )
    @Range( min = -90, max = 90, message = FIELD_RANGE )
    private Double latitude;

    @NotNull( message = FIELD_MISSING )
    @Range( min = -180, max = 180, message = FIELD_RANGE )
    private Double longitude;

    @NotNull( message = FIELD_MISSING )
    @Range( min = 0, max = 360, message = FIELD_RANGE )
    private Double azimuth;

    @NotNull( message = FIELD_MISSING )
    @Range( min = -90, max = 90, message = FIELD_RANGE )
    private Double downTilt;

    @NotNull( message = FIELD_MISSING )
    @Min( value = 0, message = FIELD_INVALID )
    private Double height;

    @Valid
    @NotNull( message = FIELD_MISSING )
    private CreateAntennaDTO antenna;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Double azimuth) {
        this.azimuth = azimuth;
    }

    public Double getDownTilt() {
        return downTilt;
    }

    public void setDownTilt(Double downTilt) {
        this.downTilt = downTilt;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public CreateAntennaDTO getAntenna() {
        return antenna;
    }

    public void setAntenna(CreateAntennaDTO antenna) {
        this.antenna = antenna;
    }
}