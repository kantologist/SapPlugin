package com.sap.workshop.plugin;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.ap.pos.dao.MaterialManager;
import com.sap.scco.ap.pos.entity.MaterialEntity;
import com.sap.workshop.plugin.b1i.ContactPersonWrapper;
import com.sap.workshop.plugin.ui.ScaleWindow;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * This class handles the requests to the Plugin Servlet
 * @author D060523
 *
 */
public class RequestDelegate {
	
	private static final Logger logger = Logger.getLogger(RequestDelegate.class);
	
	private ContactPersonDAO contactPersonDAO;
	private ScaleController scaleController;
	
	public RequestDelegate(ContactPersonDAO contactPersonDAO){
		this.contactPersonDAO = contactPersonDAO;
		scaleController = new ScaleController(new ScaleWindow());
	}
	
	/**
	 * This method will dispatch the requests
	 * @param request
	 * @param response
	 */
	public void handleRequest(HttpServletRequest request, HttpServletResponse response){		
		
		//Get the action query parameter from the request
		String requestAction = request.getParameter("action");
		
		JSONObject responseObj = null;
		
		//Switch for requested actions
		switch(requestAction){
		case "getContactPersons":		
			//Get Contact persons
			String businessPartnerId = request.getParameter("businessPartnerId");
			responseObj = getContactPersons(businessPartnerId);
			break;
		case "getMaterialInfo":
			//Get Material Info
			String materialId = request.getParameter("materialId");
			responseObj = getMaterialInfo(materialId);
			break;
		default:
			responseObj = new JSONObject();
		}

		try(OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream())){
			//Write the answer to the client
			osw.write(responseObj.toString());
		}catch(IOException e){
			logger.error("Error while processing request",e);
		}		
	}

	/**
	 * Read the contact persons for a business partner
	 * @param businessPartnerId
	 * @return
	 */
	private JSONObject getContactPersons(String businessPartnerId){
		logger.info("Getting contactpersons fur businessPartnerId " + businessPartnerId);
		
		JSONObject response = new JSONObject();
		
		JSONArray contactPersonList = new JSONArray();
		
		//Get the contacts from the db
		List<ContactPerson> contactPersons = contactPersonDAO.getContactPersonsForBusinessPartner(businessPartnerId);
		
		if(contactPersons.isEmpty()){
			try {
				//Contact persons in local DB are empty. Now call B1i Webservice to see if there are any new contacts in the ERP. 
				contactPersons = ContactPersonWrapper.getInstance().fetchContactPersonsForBusinessPartnerId(businessPartnerId);	
				
				if(contactPersons != null){
					//We got some contacts. This means our DB is out of date. Update the DB with the contact persons which were returned by the webservice. 
					for(ContactPerson person : contactPersons){
						contactPersonDAO.addOrUpdateContactPerson(person);
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch(RuntimeException e){
				//Something went wrong. Write the error in the response object.
				response.put("status", "error");
				response.put("message", "Could not connect to backend system: " + e.getMessage());
				
				return response;
			}
		}
		
		//Build the response object
		contactPersonList.addAll(contactPersons);	
	
		response.put("status", "success");
		response.put("payload", contactPersonList);
		
		return response;
	}
	
	/**
	 * Get information about a material
	 * @param materialId
	 * @return
	 */
	private JSONObject getMaterialInfo(String materialId) {
		CDBSession session = CDBSessionFactory.instance.createSession();
		JSONObject response = new JSONObject();
		
		try{
			MaterialManager matMgr = new MaterialManager(session);
			MaterialEntity material = matMgr.readMaterialByExternalID(materialId);
			
			if(material != null){
				if(!StringUtils.isEmpty(material.getUdfStringXL1())){
					//Material was found and UdfStringXL1 is filled. UdfStringXL1 contains a message which should be shown to the cashier
					response.put("status", "success");
					response.put("message", material.getUdfStringXL1());
				}else if("y".equalsIgnoreCase(material.getUdfStringXL3())){
					//Material was found and UdfStringXL3 is filled. if UdfStringXL3 contains "y" the weight of the product has to be obtained by a scale. 
					response.put("status", "success");
					response.put("action", "scale");
					
					scaleController.getScaleInput(materialId);
				}
			}
		}finally{
			session.closeDBSession();
		}
		return response;
	}
	
}
