//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.09.18 um 11:52:53 PM CEST 
//


package ch.supertomcat.videopreview.settingsconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="directorySettings" type="{}DirectorySettings"/>
 *         &lt;element name="guiSettings" type="{}GUISettings"/>
 *         &lt;element name="logLevel" type="{}LogLevelSetting"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "directorySettings",
    "guiSettings",
    "logLevel"
})
@XmlRootElement(name = "settings")
public class Settings {

    @XmlElement(required = true)
    protected DirectorySettings directorySettings;
    @XmlElement(required = true)
    protected GUISettings guiSettings;
    @XmlElement(required = true, defaultValue = "INFO")
    @XmlSchemaType(name = "string")
    protected LogLevelSetting logLevel;

    /**
     * Ruft den Wert der directorySettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DirectorySettings }
     *     
     */
    public DirectorySettings getDirectorySettings() {
        return directorySettings;
    }

    /**
     * Legt den Wert der directorySettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectorySettings }
     *     
     */
    public void setDirectorySettings(DirectorySettings value) {
        this.directorySettings = value;
    }

    /**
     * Ruft den Wert der guiSettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GUISettings }
     *     
     */
    public GUISettings getGuiSettings() {
        return guiSettings;
    }

    /**
     * Legt den Wert der guiSettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GUISettings }
     *     
     */
    public void setGuiSettings(GUISettings value) {
        this.guiSettings = value;
    }

    /**
     * Ruft den Wert der logLevel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogLevelSetting }
     *     
     */
    public LogLevelSetting getLogLevel() {
        return logLevel;
    }

    /**
     * Legt den Wert der logLevel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogLevelSetting }
     *     
     */
    public void setLogLevel(LogLevelSetting value) {
        this.logLevel = value;
    }

}
