package com.sap.workshop.plugin.b1i;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sap.workshop.plugin.ContactPerson;
import com.sap.workshop.plugin.ContactPersonDAO;

/**
 * This Thread will handle the updates/insertions of contact persons. It works with a queue. 
 * @author D060523
 */
public class ContactPersonBatchFetchCallback extends Thread{

	private Queue<ContactPerson> contactPersonQueue = new ConcurrentLinkedQueue<>();
	
	private ContactPersonDAO contactPersonDAO;
	
	private AtomicBoolean shotDownFlag = new AtomicBoolean(false);
	
	public ContactPersonBatchFetchCallback(ContactPersonDAO contactPersonDAO){
		this.contactPersonDAO = contactPersonDAO;
		setName("ContactPersonBatchFetchCallback");
	}
	
	/**
	 * Add a list of contact persons to the queue
	 * @param contactPersons
	 */
	public void processBatch(List<ContactPerson> contactPersons){			
		contactPersonQueue.addAll(contactPersons);
		synchronized (this) {
			notify();
		}
	}	
	
	@Override
	public void run() {
		do{
			if(contactPersonQueue.isEmpty() && shotDownFlag.get()){
				//Queue is empty and shutdown flag is set to true. Exit the loop
				break;
			}
			
			while(contactPersonQueue.isEmpty() && !shotDownFlag.get()){
				//Queue is empty but shutdown flag is not true. This means we have to wait for further input
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			//Get the next entry in the queue and write in to the DB.
			ContactPerson person = contactPersonQueue.poll();
			if(person != null){
				contactPersonDAO.addOrUpdateContactPerson(person);
			}
		}while(true);
	}
	
	/**
	 * Set the shutdown flag to true
	 */
	public void done(){
		shotDownFlag.set(true);
		synchronized (this) {
			notify();
		}
	}
}
