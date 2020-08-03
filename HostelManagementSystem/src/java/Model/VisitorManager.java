
package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class VisitorManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public VisitorManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addVisitor (Visitor visitor){
        mgr.persist(visitor);
        return true;
    }
    
    public Visitor findVisitorByID(String id){
        Visitor visitor = mgr.find(Visitor.class, id);
        return visitor;
    }
    
    public Visitor findByCard(String card) {
        Visitor visitor = (Visitor)mgr.createNamedQuery("Visitor.findByCard").setParameter("entCardNo", card).getSingleResult();
        return visitor;
    }
    
    public List<Visitor> findAllWithParameter(String visitorID,String visitorName,String visitorIC, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT v FROM Visitor v WHERE v.visitorID LIKE :visitorID "
                + "AND v.name LIKE :visitorName AND v.identificationNo LIKE :visitorIC "
                + "ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Visitor.findAllWithParameter", query);
        List<Visitor> list = mgr.createNamedQuery("Visitor.findAllWithParameter")
                .setParameter("visitorID", "%" + visitorID + "%").setParameter("visitorName", "%" + visitorName + "%")
                .setParameter("visitorIC", "%" + visitorIC + "%")
                .getResultList();
        return list;
    }
    
    public boolean updateVisitor(Visitor visitor) throws Exception {
        Visitor tempoVisitor = findVisitorByID(visitor.getVisitorID());
        if (tempoVisitor != null) {
            tempoVisitor.setEntCardNo(visitor.getEntCardNo());
            return true;
        }
        return false;
    }
    
    public boolean deleteVisitor(String id) {
        Visitor visitor = findVisitorByID(id);
        if (visitor != null) {
            mgr.remove(visitor);
            return true;
        }
        return false;
    }
    
    public String generateID() {
        List<Visitor> list = mgr.createNamedQuery("Visitor.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getVisitorID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "VI";
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
