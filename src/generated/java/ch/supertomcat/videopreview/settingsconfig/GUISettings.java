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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GUISettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GUISettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lookAndFeel" type="{}LookAndFeelSetting"/>
 *         &lt;element name="selectedMainTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="selectedFooterTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mainWindow" type="{}WindowSettings"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GUISettings", propOrder = {
    "lookAndFeel",
    "selectedMainTemplate",
    "selectedFooterTemplate",
    "mainWindow"
})
public class GUISettings {

    @XmlElement(required = true, defaultValue = "LAF_OS")
    @XmlSchemaType(name = "string")
    protected LookAndFeelSetting lookAndFeel;
    @XmlElement(required = true, nillable = true)
    protected String selectedMainTemplate;
    @XmlElement(required = true, nillable = true)
    protected String selectedFooterTemplate;
    @XmlElement(required = true)
    protected WindowSettings mainWindow;

    /**
     * Ruft den Wert der lookAndFeel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LookAndFeelSetting }
     *     
     */
    public LookAndFeelSetting getLookAndFeel() {
        return lookAndFeel;
    }

    /**
     * Legt den Wert der lookAndFeel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LookAndFeelSetting }
     *     
     */
    public void setLookAndFeel(LookAndFeelSetting value) {
        this.lookAndFeel = value;
    }

    /**
     * Ruft den Wert der selectedMainTemplate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedMainTemplate() {
        return selectedMainTemplate;
    }

    /**
     * Legt den Wert der selectedMainTemplate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedMainTemplate(String value) {
        this.selectedMainTemplate = value;
    }

    /**
     * Ruft den Wert der selectedFooterTemplate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedFooterTemplate() {
        return selectedFooterTemplate;
    }

    /**
     * Legt den Wert der selectedFooterTemplate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedFooterTemplate(String value) {
        this.selectedFooterTemplate = value;
    }

    /**
     * Ruft den Wert der mainWindow-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WindowSettings }
     *     
     */
    public WindowSettings getMainWindow() {
        return mainWindow;
    }

    /**
     * Legt den Wert der mainWindow-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WindowSettings }
     *     
     */
    public void setMainWindow(WindowSettings value) {
        this.mainWindow = value;
    }

}
