package com.sap.workshop.plugin;

/**
 * POJO for contact person data
 * @author D060523
 */
public class ContactPerson {

	private String businessPartnerId;
	private String contactPersonId;
	private String name;
	private String address;
	
	public String getBusinessPartnerId() {
		return businessPartnerId;
	}
	public void setBusinessPartnerId(String businessPartnerId) {
		this.businessPartnerId = businessPartnerId;
	}
	public String getContactPersonId() {
		return contactPersonId;
	}
	public void setContactPersonId(String contactPersonId) {
		this.contactPersonId = contactPersonId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}

