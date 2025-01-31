//
// This file was generated by the Eclipse Implementation of JAXB, v4.0.5 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//


package ch.supertomcat.videopreview.settingsconfig;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="directorySettings" type="{}DirectorySettings"/>
 *         <element name="guiSettings" type="{}GUISettings"/>
 *         <element name="logLevel" type="{}LogLevelSetting"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the directorySettings property.
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
     * Sets the value of the directorySettings property.
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
     * Gets the value of the guiSettings property.
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
     * Sets the value of the guiSettings property.
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
     * Gets the value of the logLevel property.
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
     * Sets the value of the logLevel property.
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
