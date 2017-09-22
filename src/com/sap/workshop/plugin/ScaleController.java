package com.sap.workshop.plugin;

import java.math.BigDecimal;

import javax.swing.SwingUtilities;

import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.ap.pos.dao.EntityFactory;
import com.sap.scco.ap.pos.dao.MaterialManager;
import com.sap.scco.ap.pos.dao.ReceiptManager;
import com.sap.scco.ap.pos.entity.MaterialEntity;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.entity.SalesItemEntity;
import com.sap.scco.ap.registry.UserRegistry;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.CConst;
import com.sap.workshop.plugin.ui.ScaleWindow;

/**
 * Controller for the Scale Window
 * @author D060523
 *
 */
public class ScaleController {
	
	private ScaleWindow scaleWindow;
	private String materialId;
	
	public ScaleController(ScaleWindow scaleWindow) {
		this.scaleWindow = scaleWindow;
		this.scaleWindow.setScaleController(this);
	}
	
	/**
	 * Called by the UI when the scale process is finished
	 * @param quantity
	 */
	public void scaleInputDone(BigDecimal quantity){
		CDBSession session = CDBSessionFactory.instance.createSession();
		
		//Hide the Scale UI
		scaleWindow.setVisible(false);
		
		try{
			ReceiptManager receiptManager = new ReceiptManager(session);
			//Get the current receipt or create a new one
			ReceiptEntity receipt = receiptManager.findOrCreate(UserRegistry.INSTANCE.getCurrentUser(), null, false);
			
			if(receipt != null){
				MaterialManager materialManager = new MaterialManager(session);
				//Get the material from the DB
				MaterialEntity material = materialManager.readMaterialByExternalID(materialId);
				
				if(material != null){
					BigDecimal amount = material.getPriceDiscounts().get(0).getGrossPrice();
					
					//Now a new salesItem is created and the weight which was obtained from the scale is set as quantity.
					SalesItemEntity salesItem = EntityFactory.INSTANCE.createSalesItemEntity(material.getTaxRateTypeCode(), material.getDescription(), "1", materialId, quantity, SalesItemEntity.SalesItemTypeCode.MATERIAL, amount, false, false, material.getDefaultQuantityTypeCode(), BigDecimal.ZERO, false);
					
					salesItem.setMaterial(material);
                    salesItem.setDiscountable(false);
                    salesItem.setUnitPriceChanged(true);
                    salesItem.setMarkChanged(true);
                    salesItem.setQuantityTypeCode(material.getDefaultQuantityTypeCode());
                    salesItem.setQuantityTypeCodeName(material.getDefaultQuantityTypeCodeName());
                    
                    receiptManager.addSalesItems(receipt, salesItem);
                    
                    //We fire a RECEIPT_REFRESH event. This will trigger a refresh of the receipt in the CCO sales screen
                    UIEventDispatcher.INSTANCE.dispatchAction(CConst.UIEventsIds.RECEIPT_REFRESH, null, receipt);                    
				}
			}
		}catch(Exception e){
			System.err.println(e);
		}finally{
			session.closeDBSession();
		}
	}
	
	/**
	 * Get the weight from a scale for the material
	 * @param materialId
	 */
	public void getScaleInput(String materialId){
		this.materialId = materialId;
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				//Show the scale UI
				scaleWindow.setVisible(true);
			}
		});
	}
	
	/**
	 * Scale input was cancelled
	 */
	public void cancelScaleInput(){
		//Send HIDE_IN_PROGRESS_DIALOG event to the CCO frontent. This will hide the in progress dialog
		UIEventDispatcher.INSTANCE.dispatchAction("HIDE_IN_PROGRESS_DIALOG", null, null);
	}
}
