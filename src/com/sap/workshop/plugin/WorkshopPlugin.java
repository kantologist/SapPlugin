package com.sap.workshop.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.BreakExecutionException;
import com.sap.scco.ap.plugin.annotation.ListenToExit;
import com.sap.scco.ap.plugin.annotation.ManualTriggerFunction;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.PluginAt.POSITION;
import com.sap.scco.ap.plugin.annotation.Schedulable;
import com.sap.scco.ap.plugin.annotation.TriggerParam;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.ap.pos.dao.EntityFactory;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.dao.PrintTemplateManager;
import com.sap.scco.ap.pos.dto.ReceiptDTO;
import com.sap.scco.ap.pos.entity.PrintTemplateEntity;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.i14y.util.context.I14YContext;
import com.sap.scco.ap.pos.job.PluginJob;
import com.sap.scco.ap.pos.printer.PrintEngine;
import com.sap.scco.ap.pos.printer.PrintJob;
import com.sap.scco.ap.pos.printer.PrintJobManager;
import com.sap.scco.ap.pos.printer.PrintTemplateType;
import com.sap.scco.ap.pos.util.TriggerParameter;
import com.sap.scco.util.CLogUtils;
import com.sap.scco.util.conf.CConfig;
import com.sap.scco.util.exception.WrongConfigurationException;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.exception.XState;
import com.sap.scco.util.types.LogGDT;
import com.sap.scco.util.types.LogItemSeverityCode;
import com.sap.workshop.plugin.b1i.ContactPersonBatchFetchCallback;
import com.sap.workshop.plugin.b1i.ContactPersonWrapper;
import com.sap.workshop.plugin.b1i.ReceiptPostingLogic;
import com.sap.workshop.plugin.print.CustomPrintDTO;
import com.sap.workshop.plugin.print.CustomPrintJobBuilder;
import com.sap.workshop.plugin.util.HelperCollection;
import com.sap.workshop.plugin.util.WebserviceException;

import freemarker.template.TemplateException;
import generated.PostInvoiceType;

/**
 * Base class for the Plugin. Every Plugin needs at least one class which extends BasePlugin.
 * @author D060523
 */
public class WorkshopPlugin extends BasePlugin implements ConfigDAO{

	private static final Logger logger = Logger.getLogger(WorkshopPlugin.class);
	
	private ContactPersonDAO contactPersonDAO;
	
	private RequestDelegate requestDelegate;
	
	@Override
	public String getId() {
		//Plugin unique ID
		return "WORKSHOP_PLUGIN";
	}

	@Override
	public String getName() {
		//Plugin name
		return "Workshop Plugin";
	}

	@Override
	public String getVersion() {
		//Plugin version
		return "1.0";
	}
		
	@Override
	public boolean persistPropertiesToDB() {
		//If this method return true, the PluginProperties will be stored in the CCO Database automatically.
		//If this is false or not overwritten by the plugin class, the Plugin itself has to take care about storing the properties.
		return true;
	}
	
	@Override
	public Map<String, String> getPluginPropertyConfig() {
		//The method can return a Map, which contains the possible properties for the plugin.
		//The key is the name of the property and the value is the datatype.
		//Based on the datatype the input fields will be created. A datatype "boolean" will, for example, create a checkbox.
		Map<String, String> propertyConfig = new HashMap<>();
		
		propertyConfig.put("DEBITOR_PAYMENT_MIN_RATING", "int");
		
		return propertyConfig;
	}
	
	@Override
	public void startup() {		
		//This method is executed if the plugin is started. This happens during the startup phase of CCO.
		super.startup();
		contactPersonDAO = new ContactPersonDAO();
		contactPersonDAO.setupTable();
		
		requestDelegate = new RequestDelegate(contactPersonDAO);
		
		ReceiptValidationLogic.INSTANCE.setConfigDAO(this);
		
		//Deploy the custom print templates.
		deployPrintTemplates();
		PrintJobManager.INSTANCE.addPrintJobBuilders(new CustomPrintJobBuilder());
	}

	@Override
	public void shutdown() {	
		//This method is called during the shutdown phase. Release all resources here.
		super.shutdown();
	}
	
	@JSInject(targetScreen="sales")
	//It is also possible that this method returns an array of InputStream[] or a String.
	public InputStream injectJs(){
		//This will inject some custom javascript code in the sales screen.
		//The javascript file is located in the resources folder.
		return this.getClass().getResourceAsStream("/resources/workshop.js");
	}

	@ListenToExit(exitName="PluginServlet.callback.get")
	//If you need post requests, then you can also use @ListenToExit(exitName="PluginServlet.callback.post")
	public void pluginServletGet(Object caller, Object[] args){
		//This will be called if there are any GET requests to the PluginServlet
		//In the workshop.js you can see ajax calls to the PluginServlet. This requests will end here.
		HttpServletRequest request = (HttpServletRequest)args[0];
		HttpServletResponse response = (HttpServletResponse)args[1];
		
		requestDelegate.handleRequest(request, response);
	}
	
	@ListenToExit(exitName="BusinessOneServiceWrapper.beforePostInvoiceRequest")
	public void enrichtReceiptPostRequest(Object caller, Object[] args){		
		//This exit is called, before the request to B1i is taking place. It can be used to manipulate the payload, which will be sent to B1i
		ReceiptDTO receipt = (ReceiptDTO)args[0]; 
		PostInvoiceType request = (PostInvoiceType)args[1];
		
		ReceiptPostingLogic.INSTANCE.enrichtReceiptPostRequest(receipt, request);
	}
	
	@ListenToExit(exitName="BasePrintJobBuilder.mergeTemplateWithData")
	//root, printTemplate, dataSource, dbSession, isReprint
	public void mergePrintTemplateData(Object caller, Object[] args){
		//This method will add some custom values to the printout by 
		Map<String, Object> root = (Map<String, Object>)args[0];
		
		if(args[2] instanceof ReceiptDTO){
			ReceiptDTO receipt = (ReceiptDTO)args[2];
			//This field can be accessed in the PrintTemplate by using the variable ${CUSTOM_PRINT_FIELD}
			root.put("CUSTOM_PRINT_FIELD", "This is a customer text for receipt " + receipt.getReceiptID());			
		}
	}
	
	@PluginAt(pluginClass=IReceiptManager.class, method="dirtyStore", where=POSITION.BEFORE)
	public void performReceiptValidation(Object proxy, Object[] args, StackTraceElement callStack) throws BreakExecutionException{
		//This method is called when the receipt is posted (When to users presses the "OK" button on the sales screen) 
		
		ReceiptEntity receipt = (ReceiptEntity)args[0];

		//check if payment on credit is allowed
		if(!ReceiptValidationLogic.INSTANCE.isPaymentOnCreditAllowed(receipt)){
			
			//It is not allowed. HelperCollection.showMessageToUI will trigger an event, which will show a popup in the UI. 
			HelperCollection.showMessageToUI("This customer is not allowed to pay on credit! Customer must have gold status", "error");
						
			LogGDT log = new LogGDT();
			CLogUtils.addMessageToLog(log, LogItemSeverityCode.Error, "", "This customer is not allowed to pay on credit!");
			//Additionally we throw a BreakExecutionException. This will prevent the call of the original dirtyStore in the ReceiptManager.
			//By throwing this, we interrupt the execution of the default code and so we block the receipt creation.
			throw new BreakExecutionException(log);
		}
	}
	
	@Schedulable("Kontaktpersonen Sync")
	public void contactPersonSync(PluginJob job, TriggerParameter triggerParam, I14YContext context, Object[] params){
		//This will add a sync job in the settings screen.
		
		//The callback will write the contact persons to the DB asynchronously.
		ContactPersonBatchFetchCallback callback = new ContactPersonBatchFetchCallback(contactPersonDAO);
		try {
			//Start the callback thread
			callback.start();		
			
			//Fetch contact persons. This will call the B1i webservice.
			ContactPersonWrapper.getInstance().fetchContactPersons(new Date(0), callback);
			
			synchronized (callback) {
				//Wait for the callback thread. The thread will be finished as soon as all contact persons are persisted to the db.  
				callback.join();
			}
			
		} catch (MalformedURLException e) {
			throw new XState(e);
		} catch (WebserviceException e) {
			throw new XState("Error executing webservice: " + e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new XState("Wait was interrupted: " + e.getMessage(), e);
		}finally{
			//Trigger a done in the callback. This will tell the thread to finish.
			callback.done();
		}
	}
	
	@ManualTriggerFunction(name="Drop CPs", description="Drop CPs")
	public String dropContactPersons(){
		//This will add a button in the plugin properties screen, which can be used to execute some admin functions.
		contactPersonDAO.dropAll();
		return "OK";
	}
	
	@ManualTriggerFunction(name="Print", description="Print")
	//@TriggerParam annotation will automatically add an input field in the plugin config. The value provided in the field will be stored in the parameter.
	public String customPrint(@TriggerParam("Some Text") String someText, @TriggerParam("Some Number") String someNumber){
		//This will add a button in the plugin properties. This will trigger the custom print to be executed. 
		
		//Create the Custom Print DTO and add the values provided in the UI
		CustomPrintDTO dto = new CustomPrintDTO();
		dto.setSomeText(someText);
		
		try{
			dto.setSomeNumber(Integer.parseInt(someNumber));
		}catch(Exception e){
			return "Invalid input!";
		}
		
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			//Build the print job
			List<PrintJob<?>> printJobs = PrintJobManager.INSTANCE.createPrintJobs(dto, session, false);
			
			//Trigger the print
			PrintEngine.INSTANCE.print(printJobs, false);
		} catch (WrongConfigurationException | FOPException | WrongUsageException | IOException | TemplateException
				| TransformerException | InterruptedException e) {
			logger.error("Print failed!", e);
		}finally{
			session.closeDBSession();
		}
		
		return "OK";
	}
	
	private void deployPrintTemplates(){
		//This will deploy the CusomtPrintTemplate file to the printTemplates folder of CCO.
		//Also it will add the CustomPrintJobBuilder to the print config list in the hardware tab.
		try {
			String templateName = "CusomtPrintTemplate";
			String templateFileName = templateName + ".ftl";
			HelperCollection.extractResource("/resources/" + templateFileName, 
					CConfig.getPrintTemplatePath(), false);
			
			CDBSession session = null;
			try{
				session = CDBSessionFactory.instance.createSession();
				PrintTemplateManager templateMgr = new PrintTemplateManager(session);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("name", templateName);
				
				//Check if element with same name already exists
				List<PrintTemplateEntity> templates = templateMgr.queryByElements(params);
				if(templates == null || templates.size() == 0){
					//Was not found. Create it. Will create a new entry in the hardware tab which represents the print template config.
					PrintTemplateEntity printTemplate = EntityFactory.INSTANCE.getNewInstance(PrintTemplateEntity.class);
					printTemplate.setTemplateSrc(templateFileName);
					printTemplate.setCopies(1);
					printTemplate.setName(templateName);
					printTemplate.setPrintJobBuilder(CustomPrintJobBuilder.class.getSimpleName());
					printTemplate.setPrintTemplateType(PrintTemplateType.JPOS.getValue());
					printTemplate.setActive(true);
					
					templateMgr.create(printTemplate);
				}
			}catch(Exception e){
				logger.error(e);				
			}finally{
				if(session != null){
					session.closeDBSession();
				}
			}
		} catch (WrongUsageException e) {
			logger.error("Could not deploy Terminal print templates!", e);
		}
	}
}
