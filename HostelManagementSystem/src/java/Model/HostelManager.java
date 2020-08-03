package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class HostelManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public HostelManager(EntityManager mgr) {
        this.mgr = mgr;
    }
    
    public boolean addHostel(Hostel hostel) {
        mgr.persist(hostel);
        return true;
    }

    public Hostel findHostelByID(String id) {
        Hostel hostel = mgr.find(Hostel.class, id);
        return hostel;
    }

    public boolean deleteHostel(String id) {
        Hostel hostel = findHostelByID(id);
        if (hostel != null) {
            mgr.remove(hostel);
            return true;
        }
        return false;
    }

    public boolean updateHostel(Hostel hostel) throws Exception {
        Hostel tempoHostel = findHostelByID(hostel.getHostelID());
        if (tempoHostel != null) {
        }
        return false;
    }

    public List<Hostel> findAll() {
        List hostelList = mgr.createNamedQuery("Hostel.findAll").getResultList();
        return hostelList;
    }
    
    public List<String> finaAllLocation(){
        List locationList = mgr.createNamedQuery("Hostel.findAllLocation").getResultList();
        return locationList;
    }

    public String generateID() {
        List<Hostel> list = mgr.createNamedQuery("Hostel.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getHostelID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "HO";
        if (serialNumber < 10) {
            newID +="000" + newSerial;
        } else if (serialNumber < 100) {
            newID += "00" + newSerial;
        } else if (serialNumber < 1000) {
            newID += "0" + newSerial;
        } else if (serialNumber < 10000) {
            newID += newSerial;
        }
        return newID;
    }
    
    public boolean hostelExisted(String location, String building) {
        boolean result = false;
        List<Hostel> hostelList = mgr.createNamedQuery("Hostel.findHostelDuplicate").setParameter("building", building).setParameter("location", location).getResultList();
        if(hostelList.size()>=1){
            result = true;
        }
        return result;
    }
    
    public Hostel getExistedHostel(String location, String building) {
        Hostel hostel = (Hostel)mgr.createNamedQuery("Hostel.findHostelDuplicate").setParameter("building", building).setParameter("location", location).getSingleResult();
        return hostel;
    }
}
