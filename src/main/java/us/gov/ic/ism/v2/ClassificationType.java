
package us.gov.ic.ism.v2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClassificationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ClassificationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="U"/&gt;
 *     &lt;enumeration value="C"/&gt;
 *     &lt;enumeration value="S"/&gt;
 *     &lt;enumeration value="TS"/&gt;
 *     &lt;enumeration value="R"/&gt;
 *     &lt;enumeration value="CTS"/&gt;
 *     &lt;enumeration value="CTS-B"/&gt;
 *     &lt;enumeration value="CTS-BALK"/&gt;
 *     &lt;enumeration value="NU"/&gt;
 *     &lt;enumeration value="NR"/&gt;
 *     &lt;enumeration value="NC"/&gt;
 *     &lt;enumeration value="NS"/&gt;
 *     &lt;enumeration value="NS-S"/&gt;
 *     &lt;enumeration value="NS-A"/&gt;
 *     &lt;enumeration value="CTSA"/&gt;
 *     &lt;enumeration value="NSAT"/&gt;
 *     &lt;enumeration value="NCA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ClassificationType", namespace = "urn:us:gov:ic:ism:v2")
@XmlEnum
public enum ClassificationType {


    /**
     * 
     * US, non-US or joint UNCLASSIFIED.
     *               
     * 
     */
    U("U"),
    C("C"),
    S("S"),
    TS("TS"),
    R("R"),
    CTS("CTS"),
    @XmlEnumValue("CTS-B")
    CTS_B("CTS-B"),
    @XmlEnumValue("CTS-BALK")
    CTS_BALK("CTS-BALK"),
    NU("NU"),
    NR("NR"),
    NC("NC"),
    NS("NS"),
    @XmlEnumValue("NS-S")
    NS_S("NS-S"),
    @XmlEnumValue("NS-A")
    NS_A("NS-A"),
    CTSA("CTSA"),
    NSAT("NSAT"),
    NCA("NCA");
    private final String value;

    ClassificationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClassificationType fromValue(String v) {
        for (ClassificationType c: ClassificationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
