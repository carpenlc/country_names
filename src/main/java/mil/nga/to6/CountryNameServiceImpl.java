/*
 * CountryNameServiceImpl.java
 *
 * Created on August 2, 2008, 11:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mil.nga.to6;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Properties;
import javax.jws.WebService;
import mil.nga.country_name.v2.CountryName;
import mil.nga.country_name.v2.CountryNameBean;
import mil.nga.country_name.v2.GetCountryNamesResponse;
import mil.nga.country_name.v2.GetParameterInfoResponse;
import mil.nga.security.SecurityElement;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import us.gov.ic.ism.v2.ClassificationType;

/**
 *
 * @author jenningd
 */
@WebService(
		serviceName = "country_name_service", 
		portName = "CountryNameServicePort", 
		endpointInterface = "mil.nga.country_name.v2.CountryName", 
		targetNamespace = "mil:nga:country_name:v2", 
		wsdlLocation = "WEB-INF/wsdl/CountryNameServiceImpl/country_name_service.wsdl")
public class CountryNameServiceImpl implements CountryName {

    static final Category log = Category.getInstance(CountryNameServiceImpl.class);
    
    private String getStackTrace(Throwable t) {
      // returns stack trace as a String
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.flush();
      return sw.toString();
    }    
  
  
    public mil.nga.country_name.v2.GetCountryNamesResponse getCountryNames(mil.nga.country_name.v2.GetCountryNames parameters) {
      
        // Implements the getCountryNames operation
        
        // Return object
        GetCountryNamesResponse _retVal = new GetCountryNamesResponse();
              
        // QueryWorldSDO object is performs the actual queries 
        QueryWorldSDO qws = new QueryWorldSDO();
      
      
        try {
            // Setup the logger
            InputStream logger_is = getClass().getResourceAsStream("/log4j.properties");
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();

            log.info("getCountryNames Begin");

            // List of CountryNameBeans
            ArrayList<CountryNameBean> rslt = new ArrayList<CountryNameBean>();        

            log.info("getCountryNames 2");

            // Check inputs (if no coords specified return an Exception).
            if (parameters == null) {
              // null parameters just return all the country names and FIPS codes
                throw new UnsupportedOperationException("Parameters cannot be null.");
            } else {
               
                // read the parameters from the user input (null values OK)
                Double buffer = parameters.getBuffer();
                String codeList = parameters.getCodeList();
                String countryLayer = parameters.getCountryBoundaryLayer();
                String oceanLayer = parameters.getOceanBoundaryLayer();
                String wkt = parameters.getGeometryWKT();
                Boolean nearest = parameters.isNearest();
                log.info("getCountryNames 4");
                //log.info(nearest.toString());

                // Call the code to do the query
                _retVal = qws.getCountryNames(countryLayer, oceanLayer, codeList, wkt, nearest, buffer);                    

            }
            log.info("getCountryNames 5");


        } catch (UnsupportedOperationException e) {
            log.debug(getStackTrace(e));
            throw new UnsupportedOperationException(e.getMessage());

        } catch (Exception e) {
            // Unexpected Error
            log.error(getStackTrace(e));
            throw new UnsupportedOperationException("Unexpected Service Error. If problem persists contact service provider");
        }
        log.info("getCountryNames 7");


        log.info("getCountryNames End");

        return _retVal;
      
    }

    public mil.nga.country_name.v2.GetParameterInfoResponse getParameterInfo(mil.nga.country_name.v2.GetParameterInfo parameters) {
        
        // Implements the getParameterInfo operation
        // Create a return object
        GetParameterInfoResponse _retVal = new GetParameterInfoResponse();

        // Need QueryWorldSDO to implement the query
        QueryWorldSDO qws = new QueryWorldSDO();

        try {
            // Setup the logger
            InputStream logger_is = getClass().getResourceAsStream("/log4j.properties");
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();

            log.info("getParameterInfo Begin");


            // Check inputs (if no coords specified return an Exception).
            if (parameters == null) {
                throw new UnsupportedOperationException("You need to specify a parameter.");
            }  

            if (parameters.getParameter() == null) {
                throw new UnsupportedOperationException("You need to specify a parameter.");
            }            
            
            // Execute the query
            _retVal = qws.getParameterInfo(parameters.getParameter());

        } catch (UnsupportedOperationException e) {
            log.debug(getStackTrace(e));
            throw new UnsupportedOperationException(e.getMessage());
        } catch (Exception e) {
            // Unexpected Error
            log.error(getStackTrace(e));
            throw new UnsupportedOperationException("Unexpected Service Error. If problem persists contact service provider");
        }
        log.info("GetParameterInfo End");
        return _retVal;
    }

}
