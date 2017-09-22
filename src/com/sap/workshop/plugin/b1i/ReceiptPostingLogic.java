package com.sap.workshop.plugin.b1i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.scco.ap.pos.dto.MaterialDTO;
import com.sap.scco.ap.pos.dto.ReceiptDTO;
import com.sap.scco.ap.pos.dto.SalesItemDTO;

import generated.GenericValues;
import generated.GenericValues.KeyValPair;
import generated.PostInvoiceType;
import generated.PostInvoiceType.Sale.DocumentLines.Row;

/**
 * Logic to add fields to the receipt on posting
 * @author D060523
 *
 */
public enum ReceiptPostingLogic {
	
	INSTANCE;
	
	/**
	 * Will add a field "ShipType" to the GenericValues. This depends on the UDF "ShipType" which comes from B1.
	 * @param receipt
	 * @param request
	 */
	public void enrichtReceiptPostRequest(ReceiptDTO receipt, PostInvoiceType request){		
		Map<String, String> materialShipTypeMap = new HashMap<>();
		
		//Loop over the salesitem and find the ShipType field for each material
		for(SalesItemDTO salesItem : receipt.getSalesItems()){
			
			MaterialDTO material = salesItem.getMaterial();
			
			if(material != null){
				String matId = material.getExternalID();
				if(!materialShipTypeMap.containsKey(matId)){
					materialShipTypeMap.put(matId, material.getUdfStringXL2());
				}
			}
		}
			
		if(request.getSale() != null && request.getSale().getDocumentLines() != null && request.getSale().getDocumentLines().getRow() != null){		
			List<Row> rows = request.getSale().getDocumentLines().getRow();
			
			for(Row row : rows){
				String itemCode = row.getItemCode();
				if(materialShipTypeMap.containsKey(itemCode)){
					//Add the shiptype as generic value
					row.setGenericValues(createGenericValue("ShipType", materialShipTypeMap.get(itemCode)));
				}
			}
		}
	}
	
	private GenericValues createGenericValue(String key, String value){
		GenericValues genericValues = new GenericValues();
		KeyValPair pair = new KeyValPair();
		pair.setKey(key);
		pair.setValue(value);
		
		genericValues.getKeyValPair().add(pair);
		
		return genericValues;
	}
}
