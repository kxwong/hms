
package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class AttachmentManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public AttachmentManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addAttachment (Attachment attachment){
        mgr.persist(attachment);
        return true;
    }
    
    public Attachment findAttachmentByID(String id){
        Attachment attachment = mgr.find(Attachment.class, id);
        return attachment;
    }
    
    public List<Attachment> findAll(){
        List<Attachment> list = mgr.createNamedQuery("Attachment.findAll").getResultList();
        return list;
    }
    
    
    public boolean deleteAttachment(String id) {
        Attachment attachment = findAttachmentByID(id);
        if (attachment != null) {
            mgr.remove(attachment);
            return true;
        }
        return false;
    }
    
    public boolean updateAttachment(Attachment attachment) throws Exception {
        Attachment tempoAttachment = findAttachmentByID(attachment.getAttachID());
        if (tempoAttachment != null) {
        }
        return false;
    }
    
    public String generateID() {
        List<Attachment> list = mgr.createNamedQuery("Attachment.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getAttachID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "AT";
        if (serialNumber < 10) {
            newID += "000" + newSerial;
        } else if (serialNumber < 100) {
            newID += "00" + newSerial;
        } else if (serialNumber < 1000) {
            newID += "0" + newSerial;
        } else if (serialNumber < 10000) {
            newID += newSerial;
        }
        return newID;
    }
    
}
