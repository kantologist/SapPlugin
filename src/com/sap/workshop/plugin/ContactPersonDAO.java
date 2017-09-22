package com.sap.workshop.plugin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;

/**
 * This Data Access Object will handle the DB CRUD Operations
 * @author D060523
 *
 */
public class ContactPersonDAO {
	
	private static final Logger logger = Logger.getLogger(ContactPersonDAO.class);
	private static final String CONTACT_PERSON_TABLE_NAME = "PL_CONTACTPERSONS";
	
	/**
	 * Create the new DB table, if not existing
	 */
	public void setupTable(){		
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			//Try to create the new table. If this fails, the table is most likely already existing
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("CREATE TABLE " + CONTACT_PERSON_TABLE_NAME + " ("
						+ "BUSINESSPARTNERID varchar(255) not null,"
						+ "CONTACTPERSONID varchar(255) not null,"
						+ "NAME varchar(255),"
						+ "ADDRESS varchar(500)"
					+ ")");
			
			q.executeUpdate();
			
			q = em.createNativeQuery("CREATE UNIQUE INDEX " + CONTACT_PERSON_TABLE_NAME + "_UNQ " +
									 "ON " + CONTACT_PERSON_TABLE_NAME + " (BUSINESSPARTNERID, CONTACTPERSONID)");
			q.executeUpdate();
			session.commitTransaction();
			logger.info("Created table for Contactpersons.");
		}catch(Exception e){
			session.rollbackDBSession();
			logger.info("Contact person table already existing.");
		}finally{
			session.closeDBSession();
		}
	}
	
	public ContactPerson addOrUpdateContactPerson(ContactPerson contactPerson){
		ContactPerson contactPersonFromDB = getContactPerson(contactPerson.getContactPersonId(), contactPerson.getBusinessPartnerId());
		
		if(contactPersonFromDB != null){
			return updateContactPerson(contactPerson);
		}else{
			return createContactPerson(contactPerson);
		}
	}
	
	public ContactPerson updateContactPerson(ContactPerson contactPerson){
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("UPDATE " + CONTACT_PERSON_TABLE_NAME + " SET name=?, address=? where BUSINESSPARTNERID=? and CONTACTPERSONID=?");
			q.setParameter(1, contactPerson.getName());
			q.setParameter(2, contactPerson.getAddress());
			q.setParameter(3, contactPerson.getBusinessPartnerId());
			q.setParameter(4, contactPerson.getContactPersonId());
			
			q.executeUpdate();
			session.commitTransaction();
		}catch(RuntimeException e){
			session.rollbackDBSession();
			throw e;
		}finally{
			session.closeDBSession();
		}
		
		return contactPerson;
	}
	
	public ContactPerson createContactPerson(ContactPerson contactPerson){
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("INSERT INTO " + CONTACT_PERSON_TABLE_NAME + " VALUES(?,?,?,?)");
			q.setParameter(1, contactPerson.getBusinessPartnerId());
			q.setParameter(2, contactPerson.getContactPersonId());
			q.setParameter(3, contactPerson.getName());
			q.setParameter(4, contactPerson.getAddress());
			
			q.executeUpdate();
			session.commitTransaction();
		}catch(RuntimeException e){
			session.rollbackDBSession();
			throw e;
		}finally{
			session.closeDBSession();
		}
		
		return contactPerson;
	}
	
	public ContactPerson getContactPerson(String contactPersonId, String businessPartnerId){
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("SELECT * FROM " + CONTACT_PERSON_TABLE_NAME + " where BUSINESSPARTNERID=? and CONTACTPERSONID=?");
			q.setParameter(1, businessPartnerId);
			q.setParameter(2, contactPersonId);
			
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			
			if(results.isEmpty()){
				return null;
			}
			
			return mapResultRow(results.get(0));
		}finally{
			session.closeDBSession();
		}
	}
	
	public List<ContactPerson> getContactPersonsForBusinessPartner(String businessPartnerId){
		List<ContactPerson> resultList = new ArrayList<>();
		
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("SELECT * FROM " + CONTACT_PERSON_TABLE_NAME + " where BUSINESSPARTNERID=?");
			q.setParameter(1, businessPartnerId);
			
			@SuppressWarnings("unchecked")
			List<Object[]> results = q.getResultList();
			for(Object[] result : results){
				resultList.add(mapResultRow(result));
			}
		}finally{
			session.closeDBSession();
		}
		
		return resultList;
	}
	
	public void dropAll(){
		CDBSession session = CDBSessionFactory.instance.createSession();
		try{
			session.beginTransaction();
			EntityManager em = session.getEM();
			Query q = em.createNativeQuery("DELETE FROM " + CONTACT_PERSON_TABLE_NAME );			
			q.executeUpdate();
			session.commitTransaction();
		}catch(RuntimeException e){
			session.rollbackDBSession();
			throw e;
		}finally{
			session.closeDBSession();
		}
	}
	
	private ContactPerson mapResultRow(Object[] result){
		ContactPerson contactPerson = new ContactPerson();
		
		contactPerson.setBusinessPartnerId((String)result[0]);
		contactPerson.setContactPersonId((String)result[1]);
		contactPerson.setName((String)result[2]);
		contactPerson.setAddress((String)result[3]);
		
		return contactPerson;
	}
}
