//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.09.18 um 11:52:53 PM CEST 
//


package ch.supertomcat.videopreview.settingsconfig;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für LookAndFeelSetting.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="LookAndFeelSetting">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LAF_DEFAULT"/>
 *     &lt;enumeration value="LAF_OS"/>
 *     &lt;enumeration value="LAF_METAL"/>
 *     &lt;enumeration value="LAF_WINDOWS"/>
 *     &lt;enumeration value="LAF_WINDOWS_CLASSIC"/>
 *     &lt;enumeration value="LAF_MOTIF"/>
 *     &lt;enumeration value="LAF_GTK"/>
 *     &lt;enumeration value="LAF_MACOS"/>
 *     &lt;enumeration value="LAF_NIMBUS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LookAndFeelSetting")
@XmlEnum
public enum LookAndFeelSetting {

    LAF_DEFAULT,
    LAF_OS,
    LAF_METAL,
    LAF_WINDOWS,
    LAF_WINDOWS_CLASSIC,
    LAF_MOTIF,
    LAF_GTK,
    LAF_MACOS,
    LAF_NIMBUS;

    public String value() {
        return name();
    }

    public static LookAndFeelSetting fromValue(String v) {
        return valueOf(v);
    }

}
