package khrom.test.sanzone.service;

import khrom.test.sanzone.config.GoogleStaticMapConfig;
import khrom.test.sanzone.model.dto.create.CreateSectorDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by DEV on 10/5/2016.
 */
@Service
public class ReportGeneratorService {

    private static final String PATH_TO_SANZONE_FILE_PATTERN = "/sanzone/%s/%s_sanzone.%s";
    private static final String PATH_TO_SANZONE_PDF_REPORT_PATTERN = "/sanzone/%s/%s_sanzone.pdf";
    private static final String PATH_TO_SANZONE_HTML_REPORT_PATTERN = "/sanzone/%s/%s_sanzone.html";

    //@Value( value = "classpath:templates/sanzone_initial_hidden.jrxml" )
    @Value( value = "classpath:templates/sanzone_initial.jrxml" )
    private Resource initialTemplate;

    @Autowired
    private GoogleStaticMapConfig googleStaticMapConfig;

    public void generateReport( String session, List< CreateSectorDTO > sectors ) throws JRException, IOException {

        JRBeanCollectionDataSource collectionDataSource = new JRBeanCollectionDataSource( sectors );

        Map< String, Object > parameters = new HashMap<>();
        parameters.put( "sectorsDataSource", collectionDataSource );
        parameters.put( "summarySanzone", format( PATH_TO_SANZONE_FILE_PATTERN, session, session, googleStaticMapConfig.getFormat() ) );

        JasperReport jasperReport = JasperCompileManager.compileReport( initialTemplate.getInputStream() );
        JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, new JREmptyDataSource() );
        JasperExportManager.exportReportToPdfFile( jasperPrint, format( PATH_TO_SANZONE_PDF_REPORT_PATTERN, session, session ) );
        //JasperExportManager.exportReportToHtmlFile( jasperPrint, format( PATH_TO_SANZONE_HTML_REPORT_PATTERN, session, session ) );
    }
}