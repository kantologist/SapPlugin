
package com.sap.workshop.plugin.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sap.workshop.plugin.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ContactPrsnBP_QNAME = new QName("", "ContactPrsnBP");
    private final static QName _ContactPrsnBPResponse_QNAME = new QName("", "ContactPrsnBPResponse");
    private final static QName _FetchCntPrsnList_QNAME = new QName("", "FetchCntPrsnList");
    private final static QName _FetchCntPrsnListResponse_QNAME = new QName("", "FetchCntPrsnListResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sap.workshop.plugin.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FetchCntPrsnListResponseType }
     * 
     */
    public FetchCntPrsnListResponseType createFetchCntPrsnListResponseType() {
        return new FetchCntPrsnListResponseType();
    }

    /**
     * Create an instance of {@link ContactPrsnBPResponseType }
     * 
     */
    public ContactPrsnBPResponseType createContactPrsnBPResponseType() {
        return new ContactPrsnBPResponseType();
    }

    /**
     * Create an instance of {@link ContactPrsnBPResponseType.ContactPersonForBPList }
     * 
     */
    public ContactPrsnBPResponseType.ContactPersonForBPList createContactPrsnBPResponseTypeContactPersonForBPList() {
        return new ContactPrsnBPResponseType.ContactPersonForBPList();
    }

    /**
     * Create an instance of {@link ContactPrsnBPType }
     * 
     */
    public ContactPrsnBPType createContactPrsnBPType() {
        return new ContactPrsnBPType();
    }

    /**
     * Create an instance of {@link FetchCntPrsnListType }
     * 
     */
    public FetchCntPrsnListType createFetchCntPrsnListType() {
        return new FetchCntPrsnListType();
    }

    /**
     * Create an instance of {@link FetchCntPrsnListResponseType.ContactPersonDetails }
     * 
     */
    public FetchCntPrsnListResponseType.ContactPersonDetails createFetchCntPrsnListResponseTypeContactPersonDetails() {
        return new FetchCntPrsnListResponseType.ContactPersonDetails();
    }

    /**
     * Create an instance of {@link ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails }
     * 
     */
    public ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails createContactPrsnBPResponseTypeContactPersonForBPListContactPersonDetails() {
        return new ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContactPrsnBPType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ContactPrsnBP")
    public JAXBElement<ContactPrsnBPType> createContactPrsnBP(ContactPrsnBPType value) {
        return new JAXBElement<ContactPrsnBPType>(_ContactPrsnBP_QNAME, ContactPrsnBPType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContactPrsnBPResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ContactPrsnBPResponse")
    public JAXBElement<ContactPrsnBPResponseType> createContactPrsnBPResponse(ContactPrsnBPResponseType value) {
        return new JAXBElement<ContactPrsnBPResponseType>(_ContactPrsnBPResponse_QNAME, ContactPrsnBPResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FetchCntPrsnListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FetchCntPrsnList")
    public JAXBElement<FetchCntPrsnListType> createFetchCntPrsnList(FetchCntPrsnListType value) {
        return new JAXBElement<FetchCntPrsnListType>(_FetchCntPrsnList_QNAME, FetchCntPrsnListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FetchCntPrsnListResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FetchCntPrsnListResponse")
    public JAXBElement<FetchCntPrsnListResponseType> createFetchCntPrsnListResponse(FetchCntPrsnListResponseType value) {
        return new JAXBElement<FetchCntPrsnListResponseType>(_FetchCntPrsnListResponse_QNAME, FetchCntPrsnListResponseType.class, null, value);
    }

}
