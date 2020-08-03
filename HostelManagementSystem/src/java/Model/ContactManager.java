
package Model;

import javax.annotation.Resource;
import javax.persistence.*;

public class ContactManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public ContactManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addContact (Contact contact){
        mgr.persist(contact);
        return true;
    }
    
    public Contact findContactByID(String id){
        Contact contact = mgr.find(Contact.class, id);
        return contact;
    }
    
    public boolean deleteContact(String id) {
        Contact contact = findContactByID(id);
        if (contact != null) {
            mgr.remove(contact);
            return true;
        }
        return false;
    }
    
    public boolean updateContact(Contact contact) throws Exception {
        Contact tempoContact = findContactByID(contact.getHostellerID());
        if (tempoContact != null) {
            tempoContact.setAddress(contact.getAddress());
            tempoContact.setCity(contact.getCity());
            tempoContact.setCountry(contact.getCountry());
            tempoContact.setEmail(contact.getEmail());
            tempoContact.setHomePhone(contact.getHomePhone());
            tempoContact.setMobilePhone(contact.getMobilePhone());
            tempoContact.setPostcode(contact.getPostcode());
            tempoContact.setState(contact.getState());
            return true;
        }
        return false;
    }
    
}
