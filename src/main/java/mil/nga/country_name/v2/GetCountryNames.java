
package mil.nga.country_name.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import mil.nga.security.SecurityElement;


/**
 * <p>Java class for GetCountryNames complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCountryNames"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="security" type="{mil:nga:security}SecurityElement"/&gt;
 *         &lt;element name="countryBoundaryLayer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="oceanBoundaryLayer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="codeList" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="geometryWKT" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nearest" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="buffer" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCountryNames", propOrder = {
    "security",
    "countryBoundaryLayer",
    "oceanBoundaryLayer",
    "codeList",
    "geometryWKT",
    "nearest",
    "buffer"
})
public class GetCountryNames {

    @XmlElement(required = true, nillable = true)
    protected SecurityElement security;
    @XmlElement(required = true, nillable = true)
    protected String countryBoundaryLayer;
    @XmlElement(required = true, nillable = true)
    protected String oceanBoundaryLayer;
    @XmlElement(required = true, nillable = true)
    protected String codeList;
    @XmlElement(required = true, nillable = true)
    protected String geometryWKT;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean nearest;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double buffer;

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
     * Gets the value of the countryBoundaryLayer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryBoundaryLayer() {
        return countryBoundaryLayer;
    }

    /**
     * Sets the value of the countryBoundaryLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryBoundaryLayer(String value) {
        this.countryBoundaryLayer = value;
    }

    /**
     * Gets the value of the oceanBoundaryLayer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOceanBoundaryLayer() {
        return oceanBoundaryLayer;
    }

    /**
     * Sets the value of the oceanBoundaryLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOceanBoundaryLayer(String value) {
        this.oceanBoundaryLayer = value;
    }

    /**
     * Gets the value of the codeList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeList() {
        return codeList;
    }

    /**
     * Sets the value of the codeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeList(String value) {
        this.codeList = value;
    }

    /**
     * Gets the value of the geometryWKT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeometryWKT() {
        return geometryWKT;
    }

    /**
     * Sets the value of the geometryWKT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeometryWKT(String value) {
        this.geometryWKT = value;
    }

    /**
     * Gets the value of the nearest property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNearest() {
        return nearest;
    }

    /**
     * Sets the value of the nearest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNearest(Boolean value) {
        this.nearest = value;
    }

    /**
     * Gets the value of the buffer property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getBuffer() {
        return buffer;
    }

    /**
     * Sets the value of the buffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setBuffer(Double value) {
        this.buffer = value;
    }

}
