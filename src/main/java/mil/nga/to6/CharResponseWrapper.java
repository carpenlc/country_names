/*
 * CharResponseWrapper
 * 
 * This class extends the HttpServletResponseWrapper.
 * This Class is used by OutputFormatFilter and intercepts 
 * the Servlet's output sending it to a CharArrayWriter
 * so that OutputFormatFilter can apply an xslt's.
 * 
 */

package mil.nga.to6;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author jenningd
 */
public class CharResponseWrapper extends HttpServletResponseWrapper {
   
    private CharArrayWriter output;

    
   /*
     * If we could use a PipedStream this might be better, since
     * this method required the entire message to be loaded into 
     * memory.  http://ostermiller.org/convert_java_outputstream_inputstream.html
     * I wasn't able to figure out how to use PipedStream.
     */
   
    public CharResponseWrapper(HttpServletResponse response){     
        super(response);      
        output = new CharArrayWriter();
    }

    public String getString() {            
        /*
         * This function allows me to get the CharArrayWriter as a String
         */
        return new String(output.toCharArray());
    }
       
    @Override
    public PrintWriter getWriter(){      
        /*
         * This overrides the typical output and sends the output
         * to my "output" variable which is a CharArrayWriter
         */
        return new PrintWriter(output);           
    }
    

    /**
    * @deprecated
    */
    @Override
    public String encodeRedirectUrl(String arg0) {
    return super.encodeRedirectUrl(arg0);
    }

    /**
    * @deprecated
    */
    @Override
    public String encodeUrl(String arg0) {
    return super.encodeUrl(arg0);
    }

    /**
    * @deprecated
    */
    @Override
    public void setStatus(int arg0, String arg1) {
    super.setStatus(arg0, arg1);
    }

    
}
