package com.sap.workshop.plugin.util;

/**
 * Custom Exception for Webservice errors
 * @author D060523
 *
 */
public class WebserviceException extends Exception {

	private static final long serialVersionUID = -4559306636295659325L;

	public WebserviceException(String message){
		super(message);
	}
}
