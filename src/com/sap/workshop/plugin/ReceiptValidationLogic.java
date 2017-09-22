package com.sap.workshop.plugin;

import org.apache.commons.lang3.StringUtils;

import com.sap.scco.ap.pos.entity.BusinessPartnerEntity;
import com.sap.scco.ap.pos.entity.ReceiptEntity;

/**
 * Validates the receipt before it is posted. This will check some conditions, like the rating of the BusinessPartner
 * @author D060523
 *
 */
public enum ReceiptValidationLogic {
	
	INSTANCE;
	
	private static final int FALLBACK_MIN_RATING = 5;
	private ConfigDAO configDAO;
	
	public void setConfigDAO(ConfigDAO configDAO) {
		this.configDAO = configDAO;
	}

	/**
	 * Check if payment on credit is allowed for the businesspartner in the receipt
	 * @param receipt
	 * @return
	 */
	public boolean isPaymentOnCreditAllowed(ReceiptEntity receipt){

		if(receipt.getBusinessPartner() != null && receipt.getDebtorItem() != null){
			//Only perform the check if we have a businesspartner and a debtoritem
			BusinessPartnerEntity businessPartner = receipt.getBusinessPartner();
			
			if(!isGoldCustomer(businessPartner)){
				return false;		
			}
		}
		
		return true;
	}
	
	/**
	 * Check if a business partner has a gold rating
	 * @param businessPartner
	 * @return
	 */
	public boolean isGoldCustomer(BusinessPartnerEntity businessPartner){
		String ratingUdf = businessPartner.getUdfString2();
		
		if(StringUtils.isEmpty(ratingUdf)){
			return false;
		}
		
		try{	
			double rating = Double.parseDouble(ratingUdf);
			if(rating < getMinRating()){
				return false;
			}
			return true;
		}catch(Exception e){
			//Intentionally empty
		}
		return false;
	}
	
	private int getMinRating(){
		//Get the rating from the properties. If this fails, we use the fallback rating
		return configDAO == null ? FALLBACK_MIN_RATING : configDAO.getProperty("DEBITOR_PAYMENT_MIN_RATING", FALLBACK_MIN_RATING);
	}
}
