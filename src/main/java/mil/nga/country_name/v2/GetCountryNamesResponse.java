
package mil.nga.country_name.v2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetCountryNamesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCountryNamesResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="security" type="{mil:nga:security}SecurityElement"/&gt;
 *         &lt;element name="boundaryLayer" type="{mil:nga:country_name:v2}BoundaryInfoBean"/&gt;
 *         &lt;element name="countryNames" type="{mil:nga:country_name:v2}CountryNameBean" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCountryNamesResponse", propOrder = {
    "security",
    "boundaryLayer",
    "countryNames"
})
public class GetCountryNamesResponse {

    @XmlElement(required = true)
    protected SecurityElement security;
    @XmlElement(required = true, nillable = true)
    protected BoundaryInfoBean boundaryLayer;
    @XmlElement(nillable = true)
    protected List<CountryNameBean> countryNames;

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityElement }
     *     
     */
    public SecurityElement getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityElement }
     *     
     */
    public void setSecurity(SecurityElement value) {
        this.security = value;
    }

    /**
     * Gets the value of the boundaryLayer property.
     * 
     * @return
     *     possible object is
     *     {@link BoundaryInfoBean }
     *     
     */
    public BoundaryInfoBean getBoundaryLayer() {
        return boundaryLayer;
    }

    /**
     * Sets the value of the boundaryLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundaryInfoBean }
     *     
     */
    public void setBoundaryLayer(BoundaryInfoBean value) {
        this.boundaryLayer = value;
    }

    /**
     * Gets the value of the countryNames property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countryNames property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountryNames().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CountryNameBean }
     * 
     * 
     */
    public List<CountryNameBean> getCountryNames() {
        if (countryNames == null) {
            countryNames = new ArrayList<CountryNameBean>();
        }
        return this.countryNames;
    }

}
