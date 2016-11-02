package khrom.test.sanzone.controller;

import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.response.ResponseBuilder;
import khrom.test.sanzone.service.SanzoneImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping( "/" )
public class SanzoneImageController {

    @Autowired
    private SanzoneImageService sanzoneImageService;

    @RequestMapping( value = "/v1/sector", method = POST, consumes = JSON )
    public ResponseEntity< ? > createSectorSanzoneImage( @Validated @RequestBody CreateSanzoneRequest dto ) {

        sanzoneImageService.createSectorSanzoneImage( dto );

        ResponseEntity response = ResponseBuilder.success().code( CREATED ).buildResponseEntity();

        return response;
    }

    @RequestMapping( value = "/v1/summary/{filter}", method = POST, consumes = JSON )
    public ResponseEntity< ? > createSummarySanzoneImage( @PathVariable( value = "filter" ) Boolean filter,
                                                          @Validated @RequestBody CreateSanzoneRequest dto ) throws IOException {

        sanzoneImageService.createSummarySanzoneImage( dto, filter );

        ResponseEntity response = ResponseBuilder.success().code( CREATED ).buildResponseEntity();

        return response;
    }

    @RequestMapping( value = "/v1/summary/blurGaussian", method = POST, consumes = JSON )
    public ResponseEntity< ? > createSummarySanzoneImageWithColorProcessing( @Validated @RequestBody CreateSanzoneRequest dto ) throws IOException {

        sanzoneImageService.createSummarySanzoneImageWithColorProcessing(dto);

        ResponseEntity response = ResponseBuilder.success().code( CREATED ).buildResponseEntity();

        return response;
    }
}