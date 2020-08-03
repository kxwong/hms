
package Model;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class RegistrationreqManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public RegistrationreqManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addRegistrationreq (Registrationreq registrationreq){
        mgr.persist(registrationreq);
        return true;
    }
    
    public Registrationreq findRegistrationreqByID(String id){
        Registrationreq registrationreq = mgr.find(Registrationreq.class, id);
        return registrationreq;
    }
    
    public List<Registrationreq> findAll(){
        List<Registrationreq> list = mgr.createNamedQuery("Registrationreq.findAll")
                .getResultList();
        return list;
    }
   
    public List<Registrationreq> findAllWithParameter(String requestNo, Date requestDateStart, Date requestDateEnd, Date updateDateStart, Date updateDateEnd,String name,String status, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT r FROM Registrationreq r WHERE r.requestNo LIKE :requestNo AND concat(r.hosteller.firstName,' ',r.hosteller.middleName,' ',r.hosteller.lastName) like :name "
                + "AND r.requestDate BETWEEN :requestDateStart AND :requestDateEnd AND r.updateDate BETWEEN :updateDateStart AND :updateDateEnd "
                + "AND r.status LIKE :status ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Registrationreq.findAllWithParameter", query);
        List<Registrationreq> list = mgr.createNamedQuery("Registrationreq.findAllWithParameter").setParameter("requestNo", "%" + requestNo + "%").setParameter("name", "%" + name + "%")
                .setParameter("requestDateStart", requestDateStart).setParameter("requestDateEnd", requestDateEnd)
                .setParameter("updateDateStart", updateDateStart).setParameter("updateDateEnd", updateDateEnd)
                .setParameter("status", "%" + status + "%")
                .getResultList();
        return list;
    }
    
    public List<Date> findRequestDate() {
        List<Date> list = mgr.createNamedQuery("Registrationreq.findRequestDate").getResultList();
        return list;
    }

    public List<Date> findUpdateDate() {
        List<Date> list = mgr.createNamedQuery("Registrationreq.findUpdateDate").getResultList();
        return list;
    }
    
    public boolean deleteRegistrationreq(String id) {
        Registrationreq registrationreq = findRegistrationreqByID(id);
        if (registrationreq != null) {
            mgr.remove(registrationreq);
            return true;
        }
        return false;
    }
    
    public boolean updateRegistrationreq(Registrationreq registrationreq) throws Exception {
        Registrationreq tempoRegistrationreq = findRegistrationreqByID(registrationreq.getRequestNo());
        if (tempoRegistrationreq != null) {
            tempoRegistrationreq.setStatus(registrationreq.getStatus());
            return true;
        }
        return false;
    }
    
}
