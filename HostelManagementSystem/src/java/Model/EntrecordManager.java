package Model;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class EntrecordManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public EntrecordManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addEntrecord(Entrecord entrecord) {
        mgr.persist(entrecord);
        return true;
    }

    public Entrecord findEntrecordByID(String id) {
        Entrecord entrecord = mgr.find(Entrecord.class, id);
        return entrecord;
    }

    public List<Entrecord> findByEntCard(String entCardNo) {
        List<Entrecord> list = mgr.createNamedQuery("Entrecord.findByEntCard").setParameter("entCardNo", entCardNo).getResultList();
        return list;
    }

    public List<Entrecord> findAllWithParameter(String entHolder, String entRecordID, String name, String location, String building, Date startDate, Date endDate, String gate, String orderParameter, String order) {
        String entHolderPara = "concat(e.entCardNo.hosteller.firstName,' ',e.entCardNo.hosteller.middleName,' ',e.entCardNo.hosteller.lastName)";
        if (entHolder.equals("V")) {
            entHolderPara = "e.entCardNo.visitor.name";
        }
        Query query = this.mgr.createQuery("SELECT e FROM Entrecord e WHERE e.entRecordID LIKE :entRecordID "
                + "AND " + entHolderPara + " like :name "
                + "AND e.hostel.location like :location AND e.hostel.building like :building "
                + "AND e.accessTime BETWEEN :startDate AND :endDate "
                + "AND e.gate LIKE :gate "
                + "ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Entrecord.findAllWithParameter", query);
        List<Entrecord> list = mgr.createNamedQuery("Entrecord.findAllWithParameter")
                .setParameter("entRecordID", "%" + entRecordID + "%")
                .setParameter("name", "%" + name + "%").setParameter("location", "%" + location + "%").setParameter("building", "%" + building + "%")
                .setParameter("startDate", startDate).setParameter("endDate", endDate)
                .setParameter("gate", "%" + gate + "%")
                .getResultList();
        return list;
    }

    public List<Date> findDate() {
        List<Date> list = mgr.createNamedQuery("Entrecord.findDate").getResultList();
        return list;
    }

    public List<Date> findAll() {
        List<Date> list = mgr.createNamedQuery("Entrecord.findDate").getResultList();
        return list;
    }
    
    public List<Entrecord> findAllByDateRange(Date startDate, Date endDate) {
        List<Entrecord> list = mgr.createNamedQuery("Entrecord.findAllByDateRange").setParameter("startDate", startDate).setParameter("enddate", endDate).getResultList();
        return list;
    }

    public boolean deleteEntrecord(String id) {
        Entrecord entrecord = findEntrecordByID(id);
        if (entrecord != null) {
            mgr.remove(entrecord);
            return true;
        }
        return false;
    }

    public boolean updateEntrecord(Entrecord entrecord) throws Exception {
        Entrecord tempoEntrecord = findEntrecordByID(entrecord.getEntRecordID());
        if (tempoEntrecord != null) {
            tempoEntrecord.setEntCardNo(entrecord.getEntCardNo());
            return true;
        }
        return false;
    }

}
