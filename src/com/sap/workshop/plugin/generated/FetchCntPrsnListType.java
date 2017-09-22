
package com.sap.workshop.plugin.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Automatically generated complex type comprising the whole document for the purpose of schema re-use
 * 
 * <p>Java class for FetchCntPrsnListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FetchCntPrsnListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SysId" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *         &lt;element name="QueryHitsMaximumNumberValue" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *         &lt;element name="QueryHitsUnlimitedIndicator" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *         &lt;element name="LastReturnedObjectId" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *         &lt;element name="LastModifiedDate" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FetchCntPrsnListType", propOrder = {
    "sysId",
    "queryHitsMaximumNumberValue",
    "queryHitsUnlimitedIndicator",
    "lastReturnedObjectId",
    "lastModifiedDate"
})
public class FetchCntPrsnListType {

    @XmlElement(name = "SysId", required = true)
    protected String sysId;
    @XmlElement(name = "QueryHitsMaximumNumberValue", required = true)
    protected String queryHitsMaximumNumberValue;
    @XmlElement(name = "QueryHitsUnlimitedIndicator", required = true)
    protected String queryHitsUnlimitedIndicator;
    @XmlElement(name = "LastReturnedObjectId", required = true)
    protected String lastReturnedObjectId;
    @XmlElement(name = "LastModifiedDate", required = true)
    protected String lastModifiedDate;

    /**
     * Gets the value of the sysId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysId() {
        return sysId;
    }

    /**
     * Sets the value of the sysId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysId(String value) {
        this.sysId = value;
    }

    /**
     * Gets the value of the queryHitsMaximumNumberValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryHitsMaximumNumberValue() {
        return queryHitsMaximumNumberValue;
    }

    /**
     * Sets the value of the queryHitsMaximumNumberValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryHitsMaximumNumberValue(String value) {
        this.queryHitsMaximumNumberValue = value;
    }

    /**
     * Gets the value of the queryHitsUnlimitedIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryHitsUnlimitedIndicator() {
        return queryHitsUnlimitedIndicator;
    }

    /**
     * Sets the value of the queryHitsUnlimitedIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryHitsUnlimitedIndicator(String value) {
        this.queryHitsUnlimitedIndicator = value;
    }

    /**
     * Gets the value of the lastReturnedObjectId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastReturnedObjectId() {
        return lastReturnedObjectId;
    }

    /**
     * Sets the value of the lastReturnedObjectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastReturnedObjectId(String value) {
        this.lastReturnedObjectId = value;
    }

    /**
     * Gets the value of the lastModifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Sets the value of the lastModifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastModifiedDate(String value) {
        this.lastModifiedDate = value;
    }

}
