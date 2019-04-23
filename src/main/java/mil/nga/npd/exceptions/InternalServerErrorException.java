package mil.nga.npd.exceptions;

public class InternalServerErrorException extends Exception {

    /**
     * Eclipse-generated serialVersionUID
     */
    private static final long serialVersionUID = -1167540643494826638L;

    /**
     * Default constructor requiring an input message string.
     * @param msg Description of the error encountered.
     */
    public InternalServerErrorException(String msg) {
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
