package khrom.test.sanzone.model.dto.create;

import khrom.test.sanzone.validation.annotation.ValidCollection;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by DEV on 10/3/2016.
 */
public class CreateSanzoneRequest {

    @Valid
    @ValidCollection
    private List< CreateSectorDTO > sectors;

    public List<CreateSectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<CreateSectorDTO> sectors) {
        this.sectors = sectors;
    }
}