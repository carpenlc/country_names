/*
 * This class intercepts the Output from the servlet and applies 
 * xslt style sheets to convert output to other format HTML, JSON, or KML.
 * 
 * 
 */

package mil.nga.to6;



import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Category;

/**
 *
 * @author sysdlj
 */
public class OutputFormatFilter implements Filter {

    // log based on log4j properties 
    static final Category log = Category.getInstance(OutputFormatFilter.class);    
    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public OutputFormatFilter() {
    } 

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
	throws IOException, ServletException {
	if (debug) log("OutputFormatFilter:DoBeforeProcessing");
	//
	// Write code here to process the request and/or response before
	// the rest of the filter chain is invoked.
	//

	//
	// For example, a logging filter might log items on the request object,
	// such as the parameters.
	//
	/*
	 for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	     String name = (String)en.nextElement();
	     String values[] = request.getParameterValues(name);
	     int n = values.length;
	     StringBuffer buf = new StringBuffer();
	     buf.append(name);
	     buf.append("=");
	     for(int i=0; i < n; i++) {
	         buf.append(values[i]);
	         if (i < n-1)
	             buf.append(",");
	     }
	     log(buf.toString());
	 }
	*/
	
    } 

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
	throws IOException, ServletException {
	if (debug) log("OutputFormatFilter:DoAfterProcessing");
	//
	// Write code here to process the request and/or response after
	// the rest of the filter chain is invoked.
	//
	
	//
	// For example, a logging filter might log the attributes on the
	// request object after the request has been processed. 
	//
	/*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
	*/
	//

	//
	// For example, a filter might append something to the response.
	//
	/*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
	*/
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
	throws IOException, ServletException {

	    if (debug) log("OutputFormatFilter:doFilter()");

	doBeforeProcessing(request, response);
	
	Throwable problem = null;

        String strOutputFormat = "xml";

        try {
          //  Start Here's where I put my code 
            response.setContentType("text/xml;charset=UTF-8");
            
            //System.out.println("1: response->ContentType = " + response.getContentType());
            
            // Based on Attribute outputFormat set contentType and styleSheet
            String contentType;   
            String styleSheet;
            String callback = null;


            
            // Following code looks for OutputFormat caseinsensitive
            strOutputFormat = "xml";  //default outputFormat is XML
            String outputFields = null;
 
 
            
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();                
                if (paramName.equalsIgnoreCase("outputFormat")) {
                    strOutputFormat = request.getParameter(paramName);
                } else if (paramName.equalsIgnoreCase("callback")) {
                    callback = request.getParameter(paramName);                    
                }
                
            }            
            
            if (callback != null ) {
                // check for invalid characters.
                String regexStr = "[\\._0-9A-Za-z]{0,100}";

                if (!callback.matches(regexStr)) {
                    throw new InvalidParameterException("Invalid callback argument: " + callback +
                            " Callback may use up to 100 upper and lowercase alphabetic characters (A-Z,a-z),numbers (0-9)," +
                            " period (.), and underscore (_).");
                }                
            }            
            
            if (outputFields == null) {
                outputFields = "FEW";
            }

            
            
            if (strOutputFormat == null || strOutputFormat.equals("")) {
                // Default is XML
                contentType = "text/xml;charset=UTF-8";
                styleSheet = null;
            } else {
                
                
                
                if (strOutputFormat.equalsIgnoreCase("json")) {
                    contentType = "text/plain";
                    styleSheet = "/xslt/xml-to-json.xslt";
                } else {
                    // default to straight pass
                    contentType = "text/xml;charset=UTF-8";
                    styleSheet = null;                    
                }
            }


            
            // This is where we're going to send the output to 
            PrintWriter out = response.getWriter();                        

            
            // Uses ResponseWrapper to capture the response 
            /*
            ByteResponseWrapper responseWrapper = 
                new ByteResponseWrapper((HttpServletResponse)response);
            */
            CharResponseWrapper responseWrapper = 
                new CharResponseWrapper((HttpServletResponse)response);
            
            // This will set the output in responseWrapper
            chain.doFilter(request, responseWrapper);            
            
            
            if (styleSheet == null) {
                // Pass without any transformations 
                response.setContentType(contentType);
                //System.out.println("2a: response->ContentType = " + response.getContentType());
                
                out.write(responseWrapper.getString());
            } else {
                
                log.debug(styleSheet);
                // Get the xsl that will be applied as an Stream
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(styleSheet);            
                Source xsltSource = new StreamSource(is);
                
                response.setContentType(contentType);

                // Turn the response string into a Stream
                StringReader sr = new StringReader(responseWrapper.getString());            
                Source xmlSource = new StreamSource(sr);

                TransformerFactory transFact = TransformerFactory.newInstance();                
                Transformer trans = transFact.newTransformer(xsltSource);                       

                CharArrayWriter caw = new CharArrayWriter();
                StreamResult result  = new StreamResult(caw);                
                trans.transform(xmlSource, result);  
                
                response.setContentLength(caw.toString().length());
                if (callback != null && strOutputFormat.equalsIgnoreCase("JSON")) {
                    out.write(callback + "(" + caw.toString() + ")");
                } else {
                    out.write(caw.toString());                
                    
                }               
              
            }                        
            //  End Here's where I put my code 
        } catch(Throwable t) {
	    //
	    // If an exception is thrown somewhere down the filter chain,
	    // we still want to execute our after processing, and then
	    // rethrow the problem after that.
	    //
	    problem = t;
	    log.error(getStackTrace(t));
            
	}

	doAfterProcessing(request, response);

	//
	// If there was a problem, we want to rethrow it if it is
	// a known type, otherwise log it.
	//
	if (problem != null) {
	    if (problem instanceof ServletException) throw (ServletException)problem;
	    if (problem instanceof IOException) throw (IOException)problem;
	    sendProcessingError(problem, response, strOutputFormat);
	}
    }

    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
	return (this.filterConfig);
    }


    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {

	this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     *
     */
    public void destroy() { 
    }


    /**
     * Init method for this filter 
     *
     */
    public void init(FilterConfig filterConfig) { 

	this.filterConfig = filterConfig;
	if (filterConfig != null) {
	    if (debug) { 
		log("OutputFormatFilter:Initializing filter");
	    }
	}
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {

	if (filterConfig == null) return ("OutputFormatFilter()");
	StringBuffer sb = new StringBuffer("OutputFormatFilter(");
	sb.append(filterConfig);
	sb.append(")");
	return (sb.toString());

    }



    private void sendProcessingError(Throwable t, ServletResponse response, String strOutputFormat) {
	
        try {
            PrintWriter out = response.getWriter();
            String contentType;   
            String strOutput = "";
            
            if (strOutputFormat.equalsIgnoreCase("json")) {
                contentType = "text/plain";
                strOutput = "{\"error\":\"" + t.getMessage() +  "\\n\"}";                
            } else if (strOutputFormat.equalsIgnoreCase("html")) {
                contentType = "text/html";
                strOutput = "<html xmlns:ns1=\"mil:nga:country_name:v2\">\n";
                strOutput += "<head>\n";
                strOutput += "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n";
                strOutput += "<title>CountryNameService</title>\n";
                strOutput += "</head>\n";
                strOutput += "<body>\n";
                strOutput += "<p>\n";
                strOutput += "<b>Error</b>\n";
                strOutput += "</p>\n";
                strOutput += "<p>" + t.getMessage() + "\n";
                strOutput += "</p>\n";
                strOutput += "</body>\n";
                strOutput += "</html>\n";                
            } else if (strOutputFormat.equalsIgnoreCase("kml")) {
                contentType = "application/vnd.google-earth.kml+xml; encoding=utf-8";
                strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                strOutput += "<kml xsi:schemaLocation=\"http://www.opengis.net/kml/2.2 ogckml22.xsd\" xmlns:ns1=\"mil:nga:country_name:v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/kml/2.2\">";
                strOutput += "<Document><name>Error</name>";
		strOutput += "<description>" + t.getMessage() + "</description>";
                strOutput += "</Document>";
                strOutput += "</kml>";                
            } else {
                // default to straight pass
                contentType = "text/xml;charset=UTF-8";
                strOutput = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
                strOutput += "<error>";
                strOutput += t.getMessage();
                strOutput += "\n</error>";
            }                        
            response.setContentType(contentType);
            out.print(strOutput);              
            
        } catch (Exception e) {
            // Error processing the error ARGH!
            log.error(getStackTrace(e));
        }
        
    }

    public static String getStackTrace(Throwable t) {

	String stackTrace = null;
	    
	try {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    t.printStackTrace(pw);
	    pw.close();
	    sw.close();
	    stackTrace = sw.getBuffer().toString();
	}
	catch(Exception ex) {}
	return stackTrace;
    }

    public void log(String msg) {
	filterConfig.getServletContext().log(msg); 
    }

    private static final boolean debug = false;
}