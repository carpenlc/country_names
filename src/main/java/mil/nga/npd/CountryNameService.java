package mil.nga.npd;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mil.nga.country_name.v2.CountryName;
import mil.nga.country_name.v2.GetCountryNames;
import mil.nga.country_name.v2.GetCountryNamesResponse;
import mil.nga.country_name.v2.GetParameterInfo;
import mil.nga.country_name.v2.GetParameterInfoResponse;

@WebService(
		serviceName = "country_name_service", 
		portName = "CountryNameServicePort", 
		endpointInterface = "mil.nga.country_name.v2.CountryName", 
		targetNamespace = "mil:nga:country_name:v2", 
		wsdlLocation = "WEB-INF/wsdl/country_name_service.wsdl")
public class CountryNameService implements CountryName {

    /**
     * Set up the Logback system for use throughout the class.
     */
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(CountryNameService.class);
    
	@Override
	public GetCountryNamesResponse getCountryNames(GetCountryNames parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetParameterInfoResponse getParameterInfo(GetParameterInfo parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
