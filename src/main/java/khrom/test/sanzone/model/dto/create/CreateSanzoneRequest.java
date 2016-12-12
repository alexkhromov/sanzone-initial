package khrom.test.sanzone.model.dto.create;

import khrom.test.sanzone.validation.annotation.ValidCollection;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static khrom.test.sanzone.model.error.ErrorCode.*;

/**
 * Created by DEV on 10/3/2016.
 */
public class CreateSanzoneRequest {

    @NotNull( message = FIELD_MISSING )
    @Min( value = 0, message = FIELD_INVALID )
    private Double heightM;

    @NotNull( message = FIELD_MISSING )
    @Range( min = 0, max = 360, message = FIELD_RANGE )
    private Double azimuthM;

    @Valid
    @ValidCollection
    private List< CreateSectorDTO > sectors;

    public Double getHeightM() {
        return heightM;
    }

    public void setHeightM(Double heightM) {
        this.heightM = heightM;
    }

    public Double getAzimuthM() {
        return azimuthM;
    }

    public void setAzimuthM(Double azimuthM) {
        this.azimuthM = azimuthM;
    }

    public List<CreateSectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<CreateSectorDTO> sectors) {
        this.sectors = sectors;
    }
}