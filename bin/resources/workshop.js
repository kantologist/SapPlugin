/**
 * WorkshopPlugin JavaScript code
 */

$(function(){
	
	//We create a new button which is added to the menu bar below the sales grid
	var contactPersonBtn = $('<button/>').attr("id","btnContactPerson").text("CP");
	var optionDiv = $('<div/>').addClass("optionItem").append(contactPersonBtn);
	
	optionDiv.insertBefore($($('#salesDetails .optionItem')[1]));
	
	//If button is clicked, we fetch the contact persons
	contactPersonBtn.on("click", function(){
		contactPersonBackendCall(_CURRENT_CUSTOMER["customerIDInput"]);
	}).hide();
	
	//The "HIDE_IN_PROGRESS_DIALOG" action is a custom event which is fired from the backend.
	//Is is used in the ScaleController class
	addDynamicActionListener("HIDE_IN_PROGRESS_DIALOG", function(eventObject, eventMeta){
		hideInProgressDialog();
	});
	
});

/**
 * Backend Call to fetch the contacts for a businessPartnerId
 * @param businessPartnerId
 */
function contactPersonBackendCall(businessPartnerId){

	showInProgressDialog();
	
	$.ajax({
		"url": "PluginServlet?action=getContactPersons&businessPartnerId="+businessPartnerId,
		"dataType": "JSON",
		"type": "GET"
	}).success(function(data){
		hideInProgressDialog();
		console.log(data);		
		if(data["status"] === "success"){
			if(data["payload"].length > 0){
				showContactPersonList(data["payload"]);
			}else{
				showMessageDialog("No contacts found for the selected customer!", {"type":"info"});
			}
		}else{
			showMessageDialog(data["message"], {"type":"error"});
		}		
	}).fail(function(xhr){
		hideInProgressDialog();
		showMessageDialog("Error in backend call!", {"type":"error"});
	});	
}

/**
 * Build a popup with a table that contains the contacts
 * @param listObject
 */
function showContactPersonList(listObject){
	var table = $('<table/>').css({"width":"570px","color":"black","font-size":"12pt","border-collapse":"collapse"});
	
	var headerRow = $('<tr/>');
	var nameCellHeader = $('<th/>').text("Name").css({"border":"1px solid #333","margin":"0px","padding":"4px","width":"200px","background-color":"#ccc"});
	var phoneCellHeader = $('<th/>').text("Address").css({"border":"1px solid #333","margin":"0px","padding":"4px","background-color":"#ccc"});
	
	headerRow.append(nameCellHeader).append(phoneCellHeader);
	
	table.append(headerRow);
	
	for(var i=0,n=listObject.length;i<n;i++){
		var rowData = listObject[i];
		
		var tableRow = $('<tr/>');
		
		var nameCell = $('<td/>').text(rowData["name"]).css({"border":"1px solid #333","margin":"0px","padding":"4px","width":"200px"});
		var phoneCell = $('<td/>').text(rowData["address"]).css({"border":"1px solid #333","margin":"0px","padding":"4px"});
		
		tableRow.append(nameCell).append(phoneCell);
		
		table.append(tableRow);
	}
	
	var contentDiv = $('<div/>').css({"height": "380px", "padding":"10px", "overflow": "auto"}).append(table);
	
	showMessageDialog(contentDiv, {"dom":true,"type":"info"});	
}

/*
 * dynamicUpdateReceipt function will always be called, if the backend send a new receipt. 
 * This means that any change to the current receipt will end up in a call to this method.
 *
 */
var dynamicUpdateReceiptOrig = dynamicUpdateReceipt;
dynamicUpdateReceipt = function(data, meta, updateWhole){
	
	for(var i=0,n=data.salesItems.length;i<n;i++){
		
		var salesItem = data.salesItems[i];
		
		if(salesItem.priceElements != null){
			for(var j=0,m=salesItem.priceElements.length;j<m;j++){
				var priceElement = salesItem.priceElements[j];
				
				if(priceElement != null && "TOTN" === priceElement.type){
					salesItem["netPrice"] = addSeparatorsNF(priceElement.amountTotal.toFixed(2));
					break;
				}			
			}
		}
	}
	
	dynamicUpdateReceiptOrig(data, meta, updateWhole);
	
	hideInProgressDialog();
	
	//Only show the contact person button, if a customer is selected
	$('#btnContactPerson').hide();
	if(data["customer"] != null){
		$('#btnContactPerson').show();
	}
}

/*
 * This part will overwrite the response of the backend which is used to build the grid layout.
 * The result is a new column in the sales grid.  
 */

//Remember the original implementation
var executeMultiGridOrig = executeMultiGrid;

executeMultiGrid = function(requestParameter){
	//Call the original function
	var result = executeMultiGridOrig(requestParameter);
	
	if(result != null && result["salesitem"] != null){
		//In the result for the salesitem, we add a new column now
		var namesArray = result["salesitem"]["colNames"];
		
		//New column with name "Net"
		namesArray.push("Net");
		var temp = namesArray[namesArray.length-1];
		namesArray[namesArray.length-1] = namesArray[namesArray.length-2]; 
		namesArray[namesArray.length-2] = temp;
		
		var colArray = result["salesitem"]["colModel"];
		
		//Technical column description
		colArray.push({
				name: 'netPrice', width: 10, align: "right"
		});
		
		temp = colArray[colArray.length-1];
		colArray[colArray.length-1] = colArray[colArray.length-2]; 
		colArray[colArray.length-2] = temp;
	}
	
	return result;
};

/*
 * This function is called when a new sales item should be added. This is the case, for example, if a string was entered in the material input on the sales screen 
 */
var queryForSalesItemOrig = queryForSalesItem;

queryForSalesItem = function(item){
	setInBackendCall(false);
	materialAdditionalInfoBackendCall(item);	
};

/**
 * Backend Call to fetch the contacts for a businessPartnerId
 * @param businessPartnerId
 */
function materialAdditionalInfoBackendCall(item){

	showInProgressDialog();
	
	$.ajax({
		"url": "PluginServlet?action=getMaterialInfo&materialId="+item["id"],
		"dataType": "JSON",
		"type": "GET"
	}).success(function(data){
		hideInProgressDialog();
		console.log(data);		
		
		if(data["status"] === "success"){
			if(data["message"] != null){
				//Show a message dialog with two custom buttons (add and cancel)
				showMessageDialog(data["message"], {"type":"warning", "dialogId":"confirmAddMaterialDlg", "buttons": [
	                                                   {
	                                                	   "id":"add",
	                                                	   "type":"good",
	                                                	   "text": "Add",
	                                                	   "callback": function(){
	                                                		   hideMessageDialog("confirmAddMaterialDlg");
	                                                		   queryForSalesItemOrig(item); 
	                                                	   }
	                                                   },
	                                                   {
	                                                	   "id":"cancel",
	                                                	   "type":"bad",
	                                                	   "text": "Cancel",
	                                                	   "callback": function(){
	                                                		   hideMessageDialog("confirmAddMaterialDlg");			                                                        		
	                                                	   }
	                                                   }			                                                          
                                                  ]});
			}else if(data["action"] === "scale"){
				//Do nothing but showing the in progress dialog. Scale process is in progress
				showInProgressDialog();
			}
		}else{
			queryForSalesItemOrig(item);
		}
	}).fail(function(){
		hideInProgressDialog();
		queryForSalesItemOrig(item);
	});	
}