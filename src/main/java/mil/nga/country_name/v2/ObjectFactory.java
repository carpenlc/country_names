
package mil.nga.country_name.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mil.nga.country_name.v2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _NgaResource_QNAME = new QName("mil:nga:country_name:v2", "nga_resource");
    private final static QName _DescriptionDocument_QNAME = new QName("mil:nga:country_name:v2", "description_document");
    private final static QName _GetCountryNames_QNAME = new QName("mil:nga:country_name:v2", "GetCountryNames");
    private final static QName _GetCountryNamesResponse_QNAME = new QName("mil:nga:country_name:v2", "GetCountryNamesResponse");
    private final static QName _GetParameterInfo_QNAME = new QName("mil:nga:country_name:v2", "GetParameterInfo");
    private final static QName _GetParameterInfoResponse_QNAME = new QName("mil:nga:country_name:v2", "GetParameterInfoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mil.nga.country_name.v2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCountryNames }
     * 
     */
    public GetCountryNames createGetCountryNames() {
        return new GetCountryNames();
    }

    /**
     * Create an instance of {@link GetCountryNamesResponse }
     * 
     */
    public GetCountryNamesResponse createGetCountryNamesResponse() {
        return new GetCountryNamesResponse();
    }

    /**
     * Create an instance of {@link GetParameterInfo }
     * 
     */
    public GetParameterInfo createGetParameterInfo() {
        return new GetParameterInfo();
    }

    /**
     * Create an instance of {@link GetParameterInfoResponse }
     * 
     */
    public GetParameterInfoResponse createGetParameterInfoResponse() {
        return new GetParameterInfoResponse();
    }

    /**
     * Create an instance of {@link BoundaryInfoBean }
     * 
     */
    public BoundaryInfoBean createBoundaryInfoBean() {
        return new BoundaryInfoBean();
    }

    /**
     * Create an instance of {@link CountryNameBean }
     * 
     */
    public CountryNameBean createCountryNameBean() {
        return new CountryNameBean();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "nga_resource")
    public JAXBElement<String> createNgaResource(String value) {
        return new JAXBElement<String>(_NgaResource_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "description_document")
    public JAXBElement<String> createDescriptionDocument(String value) {
        return new JAXBElement<String>(_DescriptionDocument_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountryNames }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCountryNames }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "GetCountryNames")
    public JAXBElement<GetCountryNames> createGetCountryNames(GetCountryNames value) {
        return new JAXBElement<GetCountryNames>(_GetCountryNames_QNAME, GetCountryNames.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountryNamesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCountryNamesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "GetCountryNamesResponse")
    public JAXBElement<GetCountryNamesResponse> createGetCountryNamesResponse(GetCountryNamesResponse value) {
        return new JAXBElement<GetCountryNamesResponse>(_GetCountryNamesResponse_QNAME, GetCountryNamesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetParameterInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetParameterInfo }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "GetParameterInfo")
    public JAXBElement<GetParameterInfo> createGetParameterInfo(GetParameterInfo value) {
        return new JAXBElement<GetParameterInfo>(_GetParameterInfo_QNAME, GetParameterInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetParameterInfoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetParameterInfoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "mil:nga:country_name:v2", name = "GetParameterInfoResponse")
    public JAXBElement<GetParameterInfoResponse> createGetParameterInfoResponse(GetParameterInfoResponse value) {
        return new JAXBElement<GetParameterInfoResponse>(_GetParameterInfoResponse_QNAME, GetParameterInfoResponse.class, null, value);
    }

}
