package khrom.test.sanzone.controller;

import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.response.ResponseBuilder;
import khrom.test.sanzone.service.SanzoneReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static khrom.test.sanzone.common.constant.Constant.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by DEV on 9/12/2016.
 */
@RestController
public class SanzoneReportController {

    @Autowired
    private SanzoneReportService sanzoneReportService;

    @RequestMapping( value = "/v1/sector", method = POST, consumes = JSON )
    public ResponseEntity< ? > createSectorSanzoneImage( @Validated @RequestBody CreateSanzoneRequest dto ) {

        sanzoneReportService.createSectorSanzoneImage( dto );

        ResponseEntity response = ResponseBuilder.success().code( CREATED ).buildResponseEntity();

        return response;
    }

    @RequestMapping( value = "/v1/summary", method = POST, consumes = JSON )
    public ResponseEntity< ? > createSummarySanzone( @Validated @RequestBody CreateSanzoneRequest dto ) throws IOException {

        sanzoneReportService.createSummarySanzone( dto );

        ResponseEntity response = ResponseBuilder.success().code( CREATED ).buildResponseEntity();

        return response;
    }
}