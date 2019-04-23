/*
 * country_name_servlet.java
 * This servlet allows access to the functionality of the web serivce via an HTTP Get request.
 * The parameter operation is required and is on the operations defined in the WSDL
 * The other parameters are specified in the Schema for the operation request and response.  
 * (e.g. GetCountryNames and GetCountryNamesResponse)
 *
 *
 * Created on July 14, 2008, 4:02 PM
 */

package mil.nga.to6;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import mil.nga.country_name.v2.GetCountryNames;
import mil.nga.country_name.v2.GetCountryNamesResponse;
import mil.nga.country_name.v2.GetParameterInfo;
import mil.nga.country_name.v2.GetParameterInfoResponse;
import mil.nga.security.SecurityElement;
import us.gov.ic.ism.v2.ClassificationType;

/**
 *
 * @author David Jennings
 * @version 2.0.1
 */
public class country_name_servlet extends HttpServlet {
  
    
    private String getStackTrace(Throwable t) {
      // returns stack trace as a String
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.flush();
      return sw.toString();
    }    
    
    
    /**
     * 
     */
    private boolean isKnownFormat(String strOutputFormat) {
        boolean known_format = false;
        if (strOutputFormat == null) {
            return false;
        }        

        strOutputFormat = strOutputFormat.trim();
        
        if (strOutputFormat.equalsIgnoreCase("xml")) {
            known_format = true;
        }
        if (strOutputFormat.equalsIgnoreCase("json")) {
            known_format = true;
        }
        return known_format;
    }    
    
  /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
    // Returning XML with a SOAP call this XML would be in the SOAP Body  
    response.setContentType("text/xml;charset=UTF-8");
    PrintWriter out = response.getWriter();

    // strOutput will be used to hold the XML message before sending it back.
    String strOutput = "";
    // OutputFormat for JSON added at 2.0.1
    String strOutputFormat = "xml";
    
    
    
    try {      
    
        String strOperation = null;
        // The parameters for GetCountryNames
        String strCountryBoundaryLayer = null;
        String strOceanBoundaryLayer = null;
        String strCodeList = null;
        String strGeometryWKT = null;
        String strNearest = null;
        String strBuffer = null;
        // The Parameter for GetParameterInfo
        String strParameter = null;
        

        String strUnrecognizedParams = "";
        boolean hasUnrecognized = false;
        
        // Populate the parameters ignoring case
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (paramName.equalsIgnoreCase("operation")) {
                strOperation = request.getParameter(paramName);
            } else if (paramName.equalsIgnoreCase("countryBoundaryLayer")) {
                strCountryBoundaryLayer = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("oceanBoundaryLayer")) {
                strOceanBoundaryLayer = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("codeList")) {
                strCodeList = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("geometryWKT")) {
                strGeometryWKT = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("nearest")) {
                strNearest = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("buffer")) {
                strBuffer = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("parameter")) {
                strParameter = request.getParameter(paramName);                
            } else if (paramName.equalsIgnoreCase("outputFormat")) {
                strOutputFormat = request.getParameter(paramName);                                
            } else {
                if (hasUnrecognized) strUnrecognizedParams += ", ";
                strUnrecognizedParams += paramName;
                hasUnrecognized = true;
                
            }                        
        }
            

        if (strOperation == null) {
            // The user did not specify the operation. 
            strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
            strOutput += "<error>";
            strOutput += "You must specify an operation: getCountryNames or getParameterInfo.";
            strOutput += "\n</error>";
            out.print(strOutput);      
//        } else if (hasUnrecognized) {
//            // The user did not specify the operation. 
//            strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
//            strOutput += "<error>";            
//            strOutput += "Unrecognized parameter(s):  " + strUnrecognizedParams + ". "; 
//            
//            if (strOperation.equalsIgnoreCase("getCountryNames")) {
//                strOutput += "Valid Parameters are: countryBoundaryLayer,oceanBoundaryLayer,codeList,geometryWKT,nearest,and buffer";
//            } else if (strOperation.equalsIgnoreCase("getParameterInfo")) {
//                strOutput += "The only valid Parameter is: parameter";
//            }
//            
//            
//            strOutput += "\n</error>";
//            out.print(strOutput);                  
        
        } else if (strOperation.equalsIgnoreCase("getCountryNames")) {

            // The operation is getCountryNames 
            // Check Output Format                 
            if (!isKnownFormat(strOutputFormat)) {
                throw new InvalidParameterException("Unknown Format: " + strOutputFormat + ". Known Formats: xml or json.");                    
            }

            // Initializae the Nearest and Buffer objects
            Boolean blnNearest = null;
            Double dblBuffer = null;

            if (strNearest != null) {
                if (strNearest.equalsIgnoreCase("true")) {
                    blnNearest = new Boolean(true);
                } else if (strNearest.equalsIgnoreCase("false") || strNearest.trim().equalsIgnoreCase("")) {
                    blnNearest = new Boolean(false);
                } else {
                    throw new InvalidParameterException("Invalid input for Nearest: " + strNearest + "." +
                            " Nearest must be either true or false.");
                }
            }

            if (strBuffer != null) {
                if (strBuffer.trim().equalsIgnoreCase("")) {
                    strBuffer = "0";
                }
                
                try {  
                    dblBuffer = new Double(strBuffer);
                    if (dblBuffer == 0.0)  {
                        dblBuffer = null;
                    }
                } catch (Exception e) {              
                    throw new InvalidParameterException("Invalid input fro Buffer: " + strBuffer + "." +
                          " Buffer must be a number.");
                }
            }

            if (strCodeList != null) {
                if (strCodeList.trim().equalsIgnoreCase("")) {
                    strCodeList = null;
                }                
            }
            
            if (strCountryBoundaryLayer != null) {
                if (strCountryBoundaryLayer.trim().equalsIgnoreCase("")) {
                    strCountryBoundaryLayer = null;
                }                
            }
            
            if (strOceanBoundaryLayer != null) {
                if (strOceanBoundaryLayer.trim().equalsIgnoreCase("")) {
                    strOceanBoundaryLayer = null;
                }                
            }
            
            if (strGeometryWKT != null) {
                if (strGeometryWKT.trim().equalsIgnoreCase("")) {
                    strGeometryWKT = null;
                }                
            }
            
            // Here's the magic.  I Instatiate the class that implements the Web Service and use it
            CountryNameServiceImpl t = new CountryNameServiceImpl();

            // Input for the GetCountryNames operation
            GetCountryNames params = new GetCountryNames();

            // Set the parameters for the input
            params.setBuffer(dblBuffer);
            params.setCodeList(strCodeList);
            params.setCountryBoundaryLayer(strCountryBoundaryLayer);
            params.setOceanBoundaryLayer(strOceanBoundaryLayer);
            params.setGeometryWKT(strGeometryWKT);
            params.setNearest(blnNearest);

            // Security Element should be set (Assumed to be Unclassified for inputs)
            SecurityElement sec = new SecurityElement();
            sec.setClassification(ClassificationType.U);
            sec.getOwnerProducer().add("USA");
            params.setSecurity(sec);

            // Call the operation to get the response
            GetCountryNamesResponse resp = t.getCountryNames(params);

            // Now we'll use an ObjectFactory generated by the generate Web Service From WSDL 
            mil.nga.country_name.v2.ObjectFactory ob = new mil.nga.country_name.v2.ObjectFactory();

            // Feed object factory the response
            JAXBElement<GetCountryNamesResponse> jaxbResp = ob.createGetCountryNamesResponse(resp);

            // We'll need a JAXBContext object to turn the class data into XML
            JAXBContext context = JAXBContext.newInstance("mil.nga.country_name.v2");
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();

            // Send the output to the requester
            marshaller.marshal(jaxbResp, out);      

        } else if (strOperation.equalsIgnoreCase("getParameterInfo")) {
            // The user requested getParameterInfo operation

            // Check Output Format                 
            if (!isKnownFormat(strOutputFormat)) {
                throw new InvalidParameterException("Unknown Format: " + strOutputFormat + ". Known Formats: xml or json.");                    
            }            

            if (strParameter == null) {
             
                // The user did not specify the operation. 
                strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
                strOutput += "<error>";
                strOutput += "You must specify a parameter. ";
                strOutput += "Valid Parameters are: countryBoundaryLayer,oceanBoundaryLayer,codeList,geometryWKT,nearest,and buffer";                
                strOutput += "\n</error>";
                out.print(strOutput);      
                
                
            } else {

                // Same as before I used the web service implementation class
                CountryNameServiceImpl t = new CountryNameServiceImpl();

                // Build the paramters for the call
                GetParameterInfo params = new GetParameterInfo();

                // Set the paramters
                params.setParameter(strParameter);

                // Add on the security Element
                SecurityElement sec = new SecurityElement();
                sec.setClassification(ClassificationType.U);
                sec.getOwnerProducer().add("USA");
                params.setSecurity(sec);

                // Call the operation 
                GetParameterInfoResponse resp = t.getParameterInfo(params);

                // Use the object factory to turn resp into XML
                mil.nga.country_name.v2.ObjectFactory ob = new mil.nga.country_name.v2.ObjectFactory();
                JAXBElement<GetParameterInfoResponse> jaxbResp = ob.createGetParameterInfoResponse(resp);
                JAXBContext context = JAXBContext.newInstance("mil.nga.country_name.v2");
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();

                // Send the results to the requester
                marshaller.marshal(jaxbResp, out);      
            }

        } else {
            // Unknown operation send that info to the requester.
            strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
            strOutput += "<error>";
            strOutput += "Invalid operation: " + strOperation;   
            strOutput += ". Valid Operations: GetParameterInfo or GetCountryNames.";
            strOutput += "\n</error>";
            out.print(strOutput);      


        }


    } catch (InvalidParameterException e) {
        // I don't expect the code to get here, but just in case.
        strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        strOutput += "<error>";

        strOutput += e.getMessage();
        strOutput += "\n</error>";
        out.print(strOutput);      
        
        
    } catch (Exception e) {
        // I don't expect the code to get here, but just in case.
        strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        strOutput += "<error>";

        //strOutput +=  getStackTrace(e);
        strOutput +=  e.getMessage();
        strOutput += "\n</error>";
        out.print(strOutput);      

    }    


    out.close();

  }
  
  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    processRequest(request, response);
  }
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    processRequest(request, response);
  }
  
  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Short description";
  }
  // </editor-fold>
}
