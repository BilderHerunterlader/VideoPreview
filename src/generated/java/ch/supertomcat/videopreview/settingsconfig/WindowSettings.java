//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.09.18 um 11:52:53 PM CEST 
//


package ch.supertomcat.videopreview.settingsconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für WindowSettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="WindowSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="save" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WindowSettings", propOrder = {
    "width",
    "height",
    "x",
    "y",
    "state"
})
public class WindowSettings {

    @XmlElement(defaultValue = "0")
    protected int width;
    @XmlElement(defaultValue = "0")
    protected int height;
    @XmlElement(defaultValue = "0")
    protected int x;
    @XmlElement(defaultValue = "0")
    protected int y;
    @XmlElement(defaultValue = "0")
    protected int state;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "save", required = true)
    protected boolean save;

    /**
     * Ruft den Wert der width-Eigenschaft ab.
     * 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Legt den Wert der width-Eigenschaft fest.
     * 
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Ruft den Wert der height-Eigenschaft ab.
     * 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Legt den Wert der height-Eigenschaft fest.
     * 
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * Ruft den Wert der x-Eigenschaft ab.
     * 
     */
    public int getX() {
        return x;
    }

    /**
     * Legt den Wert der x-Eigenschaft fest.
     * 
     */
    public void setX(int value) {
        this.x = value;
    }

    /**
     * Ruft den Wert der y-Eigenschaft ab.
     * 
     */
    public int getY() {
        return y;
    }

    /**
     * Legt den Wert der y-Eigenschaft fest.
     * 
     */
    public void setY(int value) {
        this.y = value;
    }

    /**
     * Ruft den Wert der state-Eigenschaft ab.
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * Legt den Wert der state-Eigenschaft fest.
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der save-Eigenschaft ab.
     * 
     */
    public boolean isSave() {
        return save;
    }

    /**
     * Legt den Wert der save-Eigenschaft fest.
     * 
     */
    public void setSave(boolean value) {
        this.save = value;
    }

}
