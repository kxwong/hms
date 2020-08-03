package Model;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class HostellerManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public HostellerManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addHosteller(Hosteller hosteller) {
        mgr.persist(hosteller);
        return true;
    }

    public Hosteller findHostellerByID(String id) {
        Hosteller hosteller = mgr.find(Hosteller.class, id);
        return hosteller;
    }

    public Hosteller findByEntCardNo(String entCardNo) {
        try {
            Hosteller hosteller = (Hosteller) mgr.createNamedQuery("Hosteller.findByEntCardNo").setParameter("entCardNo", entCardNo).getSingleResult();
            return hosteller;
        } catch (Exception ex) {
            return null;
        }
    }

    public Hosteller findHostellerByUsername(Account curUsername) {
        Hosteller hosteller = (Hosteller) mgr.createNamedQuery("Hosteller.findByAccount").setParameter("username", curUsername).getSingleResult();
        return hosteller;
    }

    public Hosteller findHostellerByRegreq(String requestNo) {
        Hosteller hosteller = (Hosteller) mgr.createNamedQuery("Hosteller.findHostellerByRegreq").setParameter("requestNo", requestNo).getSingleResult();
        return hosteller;
    }

    public boolean deleteHosteller(String id) {
        Hosteller hosteller = findHostellerByID(id);
        if (hosteller != null) {
            mgr.remove(hosteller);
            return true;
        }
        return false;
    }

    public List<Hosteller> findAllWithParameter(String name, String roomno, String email, String contactno, String requestno, String status, String orderParameter, String order) {
        String queryroom = "";
        if (!roomno.equals("")) {
            queryroom = "AND h.stayRoom.roomNo LIKE :roomno ";
        }
        Query query = this.mgr.createQuery("SELECT h FROM Hosteller h "
                + "WHERE concat(h.firstName,' ',h.middleName,' ',h.lastName) like :name "
                + queryroom
                + "AND h.contact.email LIKE :email "
                + "AND concat(h.contact.mobilePhone,'') LIKE :phone "
                + "AND h.regReqNo.requestNo LIKE :regReqNo "
                + "AND h.status LIKE :status "
                + "ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Hosteller.findAllWithParameter", query);
        Query standardQuery = mgr.createNamedQuery("Hosteller.findAllWithParameter")
                .setParameter("name", "%" + name + "%")
                .setParameter("email", "%" + email + "%")
                .setParameter("phone", "%" + contactno + "%")
                .setParameter("regReqNo", "%" + requestno + "%")
                .setParameter("status", "%" + status + "%");

        if (!roomno.equals("")) {
            standardQuery = standardQuery.setParameter("roomno", "%" + roomno + "%");
        }
        List<Hosteller> list = standardQuery.getResultList();
        return list;
    }

    public boolean updateHosteller(Hosteller hosteller) throws Exception {
        Hosteller tempoHosteller = findHostellerByID(hosteller.getHostellerID());
        if (tempoHosteller != null) {
            tempoHosteller.setBillList(hosteller.getBillList());
            tempoHosteller.setDoB(hosteller.getDoB());
            tempoHosteller.setEntCardNo(hosteller.getEntCardNo());
            tempoHosteller.setFacilitybookingList(hosteller.getFacilitybookingList());
            tempoHosteller.setFirstName(hosteller.getFirstName());
            tempoHosteller.setGender(hosteller.getGender());
            tempoHosteller.setIdentificationNo(hosteller.getIdentificationNo());
            tempoHosteller.setImage(hosteller.getImage());
            tempoHosteller.setIssueList(hosteller.getIssueList());
            tempoHosteller.setLastName(hosteller.getLastName());
            tempoHosteller.setMiddleName(hosteller.getMiddleName());
            tempoHosteller.setNationality(hosteller.getNationality());
            tempoHosteller.setRegReqNo(hosteller.getRegReqNo());
            tempoHosteller.setRemark(hosteller.getRemark());
            tempoHosteller.setRoombookingList(hosteller.getRoombookingList());
            tempoHosteller.setStatus(hosteller.getStatus());
            tempoHosteller.setStayRoom(hosteller.getStayRoom());
            tempoHosteller.setUsername(hosteller.getUsername());
            return true;
        }
        return false;
    }

    public boolean updateHostellerStatus(Hosteller hosteller) throws Exception {
        Hosteller tempoHosteller = findHostellerByID(hosteller.getHostellerID());
        if (tempoHosteller != null) {
            tempoHosteller.setStatus(hosteller.getStatus());
            return true;
        }
        return false;
    }

    public boolean updateHostellerRoom(Hosteller hosteller) throws Exception {
        Hosteller tempoHosteller = findHostellerByID(hosteller.getHostellerID());
        if (tempoHosteller != null) {
            tempoHosteller.setStayRoom(hosteller.getStayRoom());
            return true;
        }
        return false;
    }

    public boolean updateHostellerEntCard(Hosteller hosteller) throws Exception {
        Hosteller tempoHosteller = findHostellerByID(hosteller.getHostellerID());
        if (tempoHosteller != null) {
            tempoHosteller.setEntCardNo(hosteller.getEntCardNo());
            return true;
        }
        return false;
    }

    public List<Hosteller> findAll() {
        List<Hosteller> list = mgr.createNamedQuery("Hosteller.findAll").getResultList();
        return list;
    }

    public List<Hosteller> findByRoom(String roomNo) {
        List<Hosteller> list = mgr.createNamedQuery("Hosteller.findByRoom").setParameter("roomNo", roomNo).getResultList();
        return list;
    }
}
