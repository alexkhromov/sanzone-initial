package khrom.test.sanzone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import khrom.test.sanzone.model.response.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by DEV on 1/10/2017.
 */
@RestController
public class TestController {

    @RequestMapping( value = "/v1/test", method = GET )
    public ResponseEntity< ? > test() throws IOException {

        //@formatter:off
        String testData =
                "{" +
                    "\"heightM\": \"13.5\"," +
                    "\"azimuthM\": \"50\"," +
                    "\"sectors\": [" +
                        "{" +
                            "\"latitude\": \"53.870166\"," +
                            "\"longitude\": \"27.567111\"," +
                            "\"azimuth\": \"50\"," +
                            "\"downTilt\": \"4\"," +
                            "\"height\": \"15\"," +
                            "\"antenna\": {" +
                                "\"power\": \"60\"," +
                                "\"gain\": \"15.21\"," +
                                "\"tractLost\": \"-1.6\"," +
                                "\"earthFactor\": \"1\"," +
                                "\"diagramWidthHalfPowerH\": \"65\"," +
                                "\"diagramWidthHalfPowerV\": \"15\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"latitude\": \"53.870166\"," +
                            "\"longitude\": \"27.567111\"," +
                            "\"azimuth\": \"175\"," +
                            "\"downTilt\": \"4\"," +
                            "\"height\": \"15\"," +
                            "\"antenna\": {" +
                                "\"power\": \"60\"," +
                                "\"gain\": \"15.21\"," +
                                "\"tractLost\": \"-1.6\"," +
                                "\"earthFactor\": \"1\"," +
                                "\"diagramWidthHalfPowerH\": \"65\"," +
                                "\"diagramWidthHalfPowerV\": \"15\"" +
                            "}" +
                        "}," +
                        "{" +
                            "\"latitude\": \"53.870166\"," +
                            "\"longitude\": \"27.567111\"," +
                            "\"azimuth\": \"310\"," +
                            "\"downTilt\": \"4\"," +
                            "\"height\": \"15\"," +
                            "\"antenna\": {" +
                                "\"power\": \"60\"," +
                                "\"gain\": \"15.21\"," +
                                "\"tractLost\": \"-1.6\"," +
                                "\"earthFactor\": \"1\"," +
                                "\"diagramWidthHalfPowerH\": \"65\"," +
                                "\"diagramWidthHalfPowerV\": \"15\"" +
                            "}" +
                        "}" +
                    "]" +
                "}";
         //@formatter:on

        ObjectMapper mapper = new ObjectMapper();

        CreateSanzoneRequest data = mapper.readValue( testData, CreateSanzoneRequest.class );

        return ResponseBuilder.success( data ).code( OK ).buildResponseEntity();
    }
}