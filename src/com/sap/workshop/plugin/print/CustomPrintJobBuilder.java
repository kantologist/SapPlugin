package com.sap.workshop.plugin.print;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.entity.PrintTemplateEntity;
import com.sap.scco.ap.pos.printer.SelectableDataStructure;
import com.sap.scco.ap.pos.printer.printjobbuilder.BasePrintJobBuilder;
import com.sap.scco.util.exception.WrongConfigurationException;

import freemarker.template.TemplateException;

/**
 * Custom PrintJob Builder used to printout the CustomPrintDTO object.
 * @author D060523
 *
 */
public class CustomPrintJobBuilder extends BasePrintJobBuilder {

	@Override
	public List<String> getPossibleAdditionalOptions() {
		return Collections.emptyList();
	}

	@Override
	protected void addSpecificSelectableDataStructure(SelectableDataStructure root) {
		//This data structure will represent the structure of the dto. It is used to show a user the available field, which he can use in the print template.
		SelectableDataStructure customRoot = new SelectableDataStructure("customPrintObject", SelectableDataStructure.TYPE_OBJECT, "Custom print object");
		customRoot.addSubelement("someText", new SelectableDataStructure("someText", SelectableDataStructure.TYPE_STRING, "Some text"));
		customRoot.addSubelement("someNumber", new SelectableDataStructure("someNumber", SelectableDataStructure.TYPE_INT, "Some number"));
		
		root.addSubelement("customPrintObject", customRoot);
	}

	@Override
	public String getDescription() {
		return "Custom Printjob Builder";
	}

	@Override
	public boolean isResponsibleForDataSource(Object o) {	
		//This PrintJobBuilder is only responsible for printing CustomPrintDTO
		return (o instanceof CustomPrintDTO);
	}

	@Override
	protected boolean isTemplateEnabledForSpecificBuilder(PrintTemplateEntity template, Object dataSource, CDBSession session,
			boolean isReprint) {
		//We only have one custom print template. So we can always return true here.
		return true;
	}

	@Override
	protected void mergeTemplateWithSpecificData(Map<String, Object> root,
			PrintTemplateEntity template, Object dataSource, CDBSession session, boolean isReprint) throws IOException, TemplateException {

		if(!(dataSource instanceof CustomPrintDTO)){
			//Nothing to do, im dataSource is not an CustomPrintDTO 
			return;
		}

		root.put("customPrintObject", (CustomPrintDTO)dataSource);

	}

	@Override
	protected boolean needsMerchantCopy(PrintTemplateEntity template, Object dataSource, CDBSession session, boolean isReprint)
			throws WrongConfigurationException {
		//If true was returned, the printout will be triggered twice
		return false;
	}

}
