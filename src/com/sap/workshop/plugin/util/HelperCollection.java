package com.sap.workshop.plugin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.workshop.plugin.WorkshopPlugin;

/**
 * Some helper functions
 * @author D060523
 *
 */
public class HelperCollection {

	/**
	 * This will show a popup in the CCO frontent
	 * @param msg message to show
	 * @param type info, error, warning
	 */
	public static void showMessageToUI(String msg, String type){
		Map<String, String> dialogOptions = new HashMap<>();
		dialogOptions.put("message",msg);
		dialogOptions.put("id", WorkshopPlugin.class.getSimpleName());
		dialogOptions.put("type",type);
		dialogOptions.put("maxLifeTime","30");
		
		//A SHOW_MESSAGE_DIALOG event is fired, which will show the message box popup
		UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null,
				dialogOptions);
	}

	/**
	 * Extract resources to a path. This will extract resources (like a print template) to a specified path in the CCO folder.
	 * @param resName
	 * @param path
	 * @param overwrite
	 * @throws WrongUsageException
	 */
	public static void extractResource(String resName, String path, boolean overwrite) throws WrongUsageException{

		InputStream oIn = WorkshopPlugin.class.getResourceAsStream(resName);
		if(null != oIn){
			try{
				String outName = resName.substring(resName.lastIndexOf("/")); 
				String fileName = path + File.separator + outName;
				File oFl = new File(fileName);
				if(!oFl.exists() || overwrite){
					OutputStream oOut = new FileOutputStream(oFl);
					IOUtils.copy(oIn,oOut);
					oOut.close();
				}
				oIn.close();
			}
			catch(IOException oX){
				throw new WrongUsageException("Error extracting: " + oX.getMessage());
			}
		} else {
			throw new WrongUsageException("Could not find resource '" + resName + "'.");
		}
	}
}
