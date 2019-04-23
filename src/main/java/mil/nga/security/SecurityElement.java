
package mil.nga.security;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import us.gov.ic.ism.v2.ClassificationType;


/**
 * <p>Java class for SecurityElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecurityElement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attGroup ref="{urn:us:gov:ic:ism:v2}SecurityAttributesGroup"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityElement")
public class SecurityElement {

    @XmlAttribute(name = "classification", namespace = "urn:us:gov:ic:ism:v2", required = true)
    protected ClassificationType classification;
    @XmlAttribute(name = "ownerProducer", namespace = "urn:us:gov:ic:ism:v2", required = true)
    protected List<String> ownerProducer;
    @XmlAttribute(name = "SCIcontrols", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> scIcontrols;
    @XmlAttribute(name = "SARIdentifier", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> sarIdentifier;
    @XmlAttribute(name = "disseminationControls", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> disseminationControls;
    @XmlAttribute(name = "FGIsourceOpen", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> fgIsourceOpen;
    @XmlAttribute(name = "FGIsourceProtected", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> fgIsourceProtected;
    @XmlAttribute(name = "releasableTo", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> releasableTo;
    @XmlAttribute(name = "nonICmarkings", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> nonICmarkings;
    @XmlAttribute(name = "classifiedBy", namespace = "urn:us:gov:ic:ism:v2")
    protected String classifiedBy;
    @XmlAttribute(name = "classificationReason", namespace = "urn:us:gov:ic:ism:v2")
    protected String classificationReason;
    @XmlAttribute(name = "derivedFrom", namespace = "urn:us:gov:ic:ism:v2")
    protected String derivedFrom;
    @XmlAttribute(name = "declassDate", namespace = "urn:us:gov:ic:ism:v2")
    protected XMLGregorianCalendar declassDate;
    @XmlAttribute(name = "declassEvent", namespace = "urn:us:gov:ic:ism:v2")
    protected String declassEvent;
    @XmlAttribute(name = "declassException", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> declassException;
    @XmlAttribute(name = "typeOfExemptedSource", namespace = "urn:us:gov:ic:ism:v2")
    protected List<String> typeOfExemptedSource;
    @XmlAttribute(name = "dateOfExemptedSource", namespace = "urn:us:gov:ic:ism:v2")
    protected XMLGregorianCalendar dateOfExemptedSource;
    @XmlAttribute(name = "declassManualReview", namespace = "urn:us:gov:ic:ism:v2")
    protected Boolean declassManualReview;

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link ClassificationType }
     *     
     */
    public ClassificationType getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassificationType }
     *     
     */
    public void setClassification(ClassificationType value) {
        this.classification = value;
    }

    /**
     * Gets the value of the ownerProducer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ownerProducer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOwnerProducer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOwnerProducer() {
        if (ownerProducer == null) {
            ownerProducer = new ArrayList<String>();
        }
        return this.ownerProducer;
    }

    /**
     * Gets the value of the scIcontrols property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scIcontrols property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSCIcontrols().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSCIcontrols() {
        if (scIcontrols == null) {
            scIcontrols = new ArrayList<String>();
        }
        return this.scIcontrols;
    }

    /**
     * Gets the value of the sarIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sarIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSARIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSARIdentifier() {
        if (sarIdentifier == null) {
            sarIdentifier = new ArrayList<String>();
        }
        return this.sarIdentifier;
    }

    /**
     * Gets the value of the disseminationControls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disseminationControls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisseminationControls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDisseminationControls() {
        if (disseminationControls == null) {
            disseminationControls = new ArrayList<String>();
        }
        return this.disseminationControls;
    }

    /**
     * Gets the value of the fgIsourceOpen property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fgIsourceOpen property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFGIsourceOpen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFGIsourceOpen() {
        if (fgIsourceOpen == null) {
            fgIsourceOpen = new ArrayList<String>();
        }
        return this.fgIsourceOpen;
    }

    /**
     * Gets the value of the fgIsourceProtected property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fgIsourceProtected property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFGIsourceProtected().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFGIsourceProtected() {
        if (fgIsourceProtected == null) {
            fgIsourceProtected = new ArrayList<String>();
        }
        return this.fgIsourceProtected;
    }

    /**
     * Gets the value of the releasableTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the releasableTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReleasableTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReleasableTo() {
        if (releasableTo == null) {
            releasableTo = new ArrayList<String>();
        }
        return this.releasableTo;
    }

    /**
     * Gets the value of the nonICmarkings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonICmarkings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonICmarkings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNonICmarkings() {
        if (nonICmarkings == null) {
            nonICmarkings = new ArrayList<String>();
        }
        return this.nonICmarkings;
    }

    /**
     * Gets the value of the classifiedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassifiedBy() {
        return classifiedBy;
    }

    /**
     * Sets the value of the classifiedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassifiedBy(String value) {
        this.classifiedBy = value;
    }

    /**
     * Gets the value of the classificationReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassificationReason() {
        return classificationReason;
    }

    /**
     * Sets the value of the classificationReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassificationReason(String value) {
        this.classificationReason = value;
    }

    /**
     * Gets the value of the derivedFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * Sets the value of the derivedFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDerivedFrom(String value) {
        this.derivedFrom = value;
    }

    /**
     * Gets the value of the declassDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeclassDate() {
        return declassDate;
    }

    /**
     * Sets the value of the declassDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeclassDate(XMLGregorianCalendar value) {
        this.declassDate = value;
    }

    /**
     * Gets the value of the declassEvent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclassEvent() {
        return declassEvent;
    }

    /**
     * Sets the value of the declassEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclassEvent(String value) {
        this.declassEvent = value;
    }

    /**
     * Gets the value of the declassException property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declassException property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclassException().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDeclassException() {
        if (declassException == null) {
            declassException = new ArrayList<String>();
        }
        return this.declassException;
    }

    /**
     * Gets the value of the typeOfExemptedSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the typeOfExemptedSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTypeOfExemptedSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTypeOfExemptedSource() {
        if (typeOfExemptedSource == null) {
            typeOfExemptedSource = new ArrayList<String>();
        }
        return this.typeOfExemptedSource;
    }

    /**
     * Gets the value of the dateOfExemptedSource property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfExemptedSource() {
        return dateOfExemptedSource;
    }

    /**
     * Sets the value of the dateOfExemptedSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfExemptedSource(XMLGregorianCalendar value) {
        this.dateOfExemptedSource = value;
    }

    /**
     * Gets the value of the declassManualReview property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeclassManualReview() {
        return declassManualReview;
    }

    /**
     * Sets the value of the declassManualReview property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeclassManualReview(Boolean value) {
        this.declassManualReview = value;
    }

}
