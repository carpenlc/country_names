package mil.nga.npd.exceptions;

/**
 * Exception raised when clients provide invalid input parameters.
 * 
 * @author L. Craig Carpenter
 */
public class InvalidParameterException extends Exception {

    /**
     * Eclipse-generated serialVersionUID
     */
    private static final long serialVersionUID = -7510600870514147L;

    /**
     * Default constructor requiring an input message string.
     * @param msg Description of the error encountered.
     */
    public InvalidParameterException(String msg) {
        super(msg);
    }
    
    /**
     * Simple method to convert the error message to an XML String.
     * @return Error message as XML document.
     */
    public String toXML() {
        StringBuilder sb = new StringBuilder();
        
        return sb.toString();
    }
}
