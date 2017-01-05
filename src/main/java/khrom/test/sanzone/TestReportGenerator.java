package khrom.test.sanzone;

import com.fasterxml.jackson.databind.ObjectMapper;
import khrom.test.sanzone.model.dto.create.CreateSanzoneRequest;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by DEV on 1/5/2017.
 */
public class TestReportGenerator {

    private static final String PATH_TO_SANZONE_FILE_PATTERN = "/sanzone/%s/%s_sanzone_H.%s";
    private static final String PATH_TO_SANZONE_PDF_REPORT_PATTERN = "/sanzone/%s/%s_sanzone.pdf";
    private static final String PATH_TO_SANZONE_HTML_REPORT_PATTERN = "/sanzone/%s/%s_sanzone.html";

    public static void main( String[] args ) throws IOException, JRException {

        CreateSanzoneRequest dto = getTestData();

        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource( dto.getSectors() );

        Map< String, Object > parameters = new HashMap<>();
        parameters.put( "sectorsDataSource", collectionDataSource );
        parameters.put( "summarySanzone", format( PATH_TO_SANZONE_FILE_PATTERN, "test_session", "test_session", "jpg" ) );

        JasperReport jasperReport = JasperCompileManager.compileReport( "C:\\DEV\\projects\\sanzone-initial\\src\\main\\resources\\templates\\sanzone_initial.jrxml" );
        JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, new JREmptyDataSource() );
        JasperExportManager.exportReportToPdfFile( jasperPrint, format( PATH_TO_SANZONE_PDF_REPORT_PATTERN, "test_session", "test_session" ) );
        //JasperExportManager.exportReportToHtmlFile( jasperPrint, format( PATH_TO_SANZONE_HTML_REPORT_PATTERN, session, session ) );
    }

    private static CreateSanzoneRequest getTestData()throws IOException {

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

        return mapper.readValue( testData, CreateSanzoneRequest.class );
    }
}