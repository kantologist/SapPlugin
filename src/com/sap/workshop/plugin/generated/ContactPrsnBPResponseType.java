
package com.sap.workshop.plugin.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Automatically generated complex type comprising the whole document for the purpose of schema re-use
 * 
 * <p>Java class for ContactPrsnBPResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactPrsnBPResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" form="unqualified"/&gt;
 *         &lt;element name="ContactPersonForBPList" form="unqualified"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ContactPersonDetails" maxOccurs="unbounded" minOccurs="0" form="unqualified"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="BusinessPartnerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                             &lt;element name="ContactPersonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                             &lt;element name="ContactPersonName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                             &lt;element name="ContactPersonAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactPrsnBPResponseType", propOrder = {
    "message",
    "contactPersonForBPList"
})
public class ContactPrsnBPResponseType {

    @XmlElement(name = "Message", required = true)
    protected String message;
    @XmlElement(name = "ContactPersonForBPList", required = true)
    protected ContactPrsnBPResponseType.ContactPersonForBPList contactPersonForBPList;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the contactPersonForBPList property.
     * 
     * @return
     *     possible object is
     *     {@link ContactPrsnBPResponseType.ContactPersonForBPList }
     *     
     */
    public ContactPrsnBPResponseType.ContactPersonForBPList getContactPersonForBPList() {
        return contactPersonForBPList;
    }

    /**
     * Sets the value of the contactPersonForBPList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactPrsnBPResponseType.ContactPersonForBPList }
     *     
     */
    public void setContactPersonForBPList(ContactPrsnBPResponseType.ContactPersonForBPList value) {
        this.contactPersonForBPList = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ContactPersonDetails" maxOccurs="unbounded" minOccurs="0" form="unqualified"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="BusinessPartnerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                   &lt;element name="ContactPersonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                   &lt;element name="ContactPersonName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                   &lt;element name="ContactPersonAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "contactPersonDetails"
    })
    public static class ContactPersonForBPList {

        @XmlElement(name = "ContactPersonDetails")
        protected List<ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails> contactPersonDetails;

        /**
         * Gets the value of the contactPersonDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the contactPersonDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContactPersonDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails }
         * 
         * 
         */
        public List<ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails> getContactPersonDetails() {
            if (contactPersonDetails == null) {
                contactPersonDetails = new ArrayList<ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails>();
            }
            return this.contactPersonDetails;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="BusinessPartnerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *         &lt;element name="ContactPersonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *         &lt;element name="ContactPersonName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *         &lt;element name="ContactPersonAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "businessPartnerID",
            "contactPersonCode",
            "contactPersonName",
            "contactPersonAddress"
        })
        public static class ContactPersonDetails {

            @XmlElement(name = "BusinessPartnerID")
            protected String businessPartnerID;
            @XmlElement(name = "ContactPersonCode")
            protected String contactPersonCode;
            @XmlElement(name = "ContactPersonName")
            protected String contactPersonName;
            @XmlElement(name = "ContactPersonAddress")
            protected String contactPersonAddress;

            /**
             * Gets the value of the businessPartnerID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getBusinessPartnerID() {
                return businessPartnerID;
            }

            /**
             * Sets the value of the businessPartnerID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setBusinessPartnerID(String value) {
                this.businessPartnerID = value;
            }

            /**
             * Gets the value of the contactPersonCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContactPersonCode() {
                return contactPersonCode;
            }

            /**
             * Sets the value of the contactPersonCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContactPersonCode(String value) {
                this.contactPersonCode = value;
            }

            /**
             * Gets the value of the contactPersonName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContactPersonName() {
                return contactPersonName;
            }

            /**
             * Sets the value of the contactPersonName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContactPersonName(String value) {
                this.contactPersonName = value;
            }

            /**
             * Gets the value of the contactPersonAddress property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContactPersonAddress() {
                return contactPersonAddress;
            }

            /**
             * Sets the value of the contactPersonAddress property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContactPersonAddress(String value) {
                this.contactPersonAddress = value;
            }

        }

    }

}
