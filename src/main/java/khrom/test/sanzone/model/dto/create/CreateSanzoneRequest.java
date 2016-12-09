package khrom.test.sanzone.model.dto.create;

import khrom.test.sanzone.validation.annotation.ValidCollection;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static khrom.test.sanzone.model.error.ErrorCode.FIELD_INVALID;
import static khrom.test.sanzone.model.error.ErrorCode.FIELD_MISSING;

/**
 * Created by DEV on 10/3/2016.
 */
public class CreateSanzoneRequest {

    @Valid
    @ValidCollection
    private List< CreateSectorDTO > sectors;

    @NotNull( message = FIELD_MISSING )
    @Min( value = 0, message = FIELD_INVALID )
    private Double height;

    public List<CreateSectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<CreateSectorDTO> sectors) {
        this.sectors = sectors;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}