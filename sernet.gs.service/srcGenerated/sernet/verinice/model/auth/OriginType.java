//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.22 at 10:31:54 AM CET 
//


package sernet.verinice.model.auth;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for originType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="originType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="default"/>
 *     &lt;enumeration value="modification"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "originType")
@XmlEnum
public enum OriginType {

    @XmlEnumValue("default")
    DEFAULT("default"),
    @XmlEnumValue("modification")
    MODIFICATION("modification");
    private final String value;

    OriginType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OriginType fromValue(String v) {
        for (OriginType c: OriginType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
