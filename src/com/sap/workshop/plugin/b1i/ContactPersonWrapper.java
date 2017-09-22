package com.sap.workshop.plugin.b1i;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.configuration.security.AuthorizationPolicy;

import com.sap.scco.ap.configuration.ConfigurationHelper;
import com.sap.scco.ap.pos.entity.destination.HttpDestinationEntity;
import com.sap.scco.ap.pos.util.DateUtils;
import com.sap.scco.integration.BaseWebServiceWrapper;
import com.sap.scco.util.utilities.CCrypt;
import com.sap.workshop.plugin.ContactPerson;
import com.sap.workshop.plugin.generated.ContactPersonService;
import com.sap.workshop.plugin.generated.ContactPrsnBPResponseType;
import com.sap.workshop.plugin.generated.ContactPrsnBPResponseType.ContactPersonForBPList;
import com.sap.workshop.plugin.generated.ContactPrsnBPResponseType.ContactPersonForBPList.ContactPersonDetails;
import com.sap.workshop.plugin.generated.ContactPrsnBPType;
import com.sap.workshop.plugin.generated.FetchCntPrsnListResponseType;
import com.sap.workshop.plugin.generated.FetchCntPrsnListType;
import com.sap.workshop.plugin.generated.IpostepVP001Sap0011InWCSXComSapB1IVplatformRuntimeINBWSCALLSYNCXPTINBWSCALLSYNCXPTIpoProc;
import com.sap.workshop.plugin.util.WebserviceException;

/**
 * Wrapper for the Webservice call to B1i
 * @author D060523
 *
 */
public class ContactPersonWrapper extends BaseWebServiceWrapper<ContactPersonService> {

	private static ContactPersonWrapper INSTANCE;
	
	private ContactPersonWrapper() throws MalformedURLException {
		super( null,
	           null,
	           "urn:ipostep_vP.001sap0011.in_WCSX_com.sap.b1i.vplatform.runtime_INB_WS_CALL_SYNC_XPT_INB_WS_CALL_SYNC_XPT.ipo_proc", //xmlns:tns, namespace from wsdl 
	           "ContactPersonService", //<wsdl:service name="ContactPersonService"> from wsdl
	           ContactPersonWrapper.class.getResource("/resources/ContactPersonService.wsdl").toString(), //wsdl location
	           "getIpostepVP001Sap0011InWCSXComSapB1IVplatformRuntimeINBWSCALLSYNCXPTINBWSCALLSYNCXPTIpoProcSoapBindingHTTP", //endpoint method. Take from from ContactPersonService class
	           ContactPersonService.class); //Webservice stub class
		
		
	}

	public static synchronized final ContactPersonWrapper getInstance() throws MalformedURLException{
		if(INSTANCE == null){
			INSTANCE = new ContactPersonWrapper();
		}
		return INSTANCE;
	}
	
    @Override
    protected AuthorizationPolicy getAuthorizationPolicy() {
    	//Return the auth credentials for the webservice here.
    	HttpDestinationEntity destination = ConfigurationHelper.INSTANCE.getB1ServiceDestination();
		AuthorizationPolicy policy = new AuthorizationPolicy();
		policy.setUserName(destination.getUserName());
		policy.setPassword(new String(new CCrypt().decrypt(destination.getPassword())));
		return policy;
    }

	@Override
	protected String getEndpointAddress() {
		//Return the webservice endpoint url here.
		return ConfigurationHelper.INSTANCE.getB1ServiceDestination().getUrl();
	}
	
	/**
	 * Fetches a list of contact persons for a certain BusinessPartner ID
	 * @param businessPartnerId
	 * @return
	 */
	public List<ContactPerson> fetchContactPersonsForBusinessPartnerId(String businessPartnerId){
		ContactPrsnBPType requestPayload = new ContactPrsnBPType();
		requestPayload.setBusinessPartnerID(businessPartnerId);
		requestPayload.setSysId(ConfigurationHelper.INSTANCE.getB1IntegrationSettings().getSystemId());
		
		//Actual webservice call
		ContactPrsnBPResponseType response = ((IpostepVP001Sap0011InWCSXComSapB1IVplatformRuntimeINBWSCALLSYNCXPTINBWSCALLSYNCXPTIpoProc) m_oPort).sapCCOContactPrsnBP(requestPayload);
		
		ContactPersonForBPList list = response.getContactPersonForBPList();
		
		List<ContactPerson> contactPersons = new ArrayList<>();
		for(ContactPersonDetails details : list.getContactPersonDetails()){
			contactPersons.add(mapContactPerson(details));
		}
		
		return contactPersons;
	}

	/**
	 * Fetches all contact persons from B1
	 * @param businessPartnerId
	 * @return
	 */
	public void fetchContactPersons(Date changedSince, ContactPersonBatchFetchCallback callback) throws WebserviceException{
		FetchCntPrsnListType requestPayload = new FetchCntPrsnListType();
		requestPayload.setLastModifiedDate(DateUtils.toIsoString(DateUtils.removeTime(changedSince)));
		requestPayload.setQueryHitsMaximumNumberValue("100");
		requestPayload.setSysId(ConfigurationHelper.INSTANCE.getB1IntegrationSettings().getSystemId());
		requestPayload.setQueryHitsUnlimitedIndicator("false");
		
		String lastReturnedObjectId = null;
		
		do{
			requestPayload.setLastReturnedObjectId(lastReturnedObjectId);
						
			//Actual webservice call
			FetchCntPrsnListResponseType response = ((IpostepVP001Sap0011InWCSXComSapB1IVplatformRuntimeINBWSCALLSYNCXPTINBWSCALLSYNCXPTIpoProc) m_oPort).sapCCOFetchCntPrsnList(requestPayload);
			
			lastReturnedObjectId = response.getLastReturnedObjectId();
	
			if(response.getContactPersonDetails() == null || response.getContactPersonDetails().size() == 0){
				break;
			}else{
				List<ContactPerson> contactPersons = new ArrayList<>();
				for(com.sap.workshop.plugin.generated.FetchCntPrsnListResponseType.ContactPersonDetails details : response.getContactPersonDetails()){
					contactPersons.add(mapContactPerson(details));
				}
				//Pass the contact persons to the callback. This call will be non blocking, because the callback works with a queue, which is processed in a parallel thread
				callback.processBatch(contactPersons);
			}
			
			if(!StringUtils.isEmpty(response.getMessage())){
				throw new WebserviceException(response.getMessage());
			}
		}while(true);
		
		callback.done();
	}
	
	private ContactPerson mapContactPerson(ContactPersonDetails details) {
		ContactPerson person = new ContactPerson();
		
		person.setBusinessPartnerId(details.getBusinessPartnerID());
		person.setContactPersonId(details.getContactPersonCode());
		person.setAddress(details.getContactPersonAddress());
		person.setName(details.getContactPersonName());
		return person;
	}
	
	private ContactPerson mapContactPerson(com.sap.workshop.plugin.generated.FetchCntPrsnListResponseType.ContactPersonDetails details) {
		ContactPerson person = new ContactPerson();
		
		person.setBusinessPartnerId(details.getBusinessPartnerID());
		person.setContactPersonId(details.getContactPersonCode());
		person.setAddress(details.getContactPersonAddress());
		person.setName(details.getContactPersonName());
		return person;
	}
}
