package Model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class FacilityManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public FacilityManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addFacility(Facility facility) {
        mgr.persist(facility);
        return true;
    }

    public Facility findFacilityByID(String id) {
        Facility facility = mgr.find(Facility.class, id);
        return facility;
    }

    public boolean deleteFacility(String id) {
        Facility facility = findFacilityByID(id);
        if (facility != null) {
            mgr.remove(facility);
            return true;
        }
        return false;
    }

    public boolean updateFacility(Facility facility) throws Exception {
        Facility tempoFacility = findFacilityByID(facility.getFacilityID());
        if (tempoFacility != null) {
            tempoFacility.setDescription(facility.getDescription());
            tempoFacility.setCapacity(facility.getCapacity());
            tempoFacility.setCategory(facility.getCategory());
            tempoFacility.setReminder(facility.getReminder());
            tempoFacility.setOperatingDay(facility.getOperatingDay());
            tempoFacility.setStartHour(facility.getStartHour());
            tempoFacility.setEndHour(facility.getEndHour());
            tempoFacility.setImage(facility.getImage());
            tempoFacility.setStatus(facility.getStatus());
            tempoFacility.setHostelID(facility.getHostelID());
            return true;
        }
        return false;
    }

    public List<Facility> findAll() {
        List buildingList = mgr.createNamedQuery("Facility.findAllOrderDescriptionAsc").getResultList();
        return buildingList;
    }

    public List<String> findAllLocation() {
        List locationList = mgr.createNamedQuery("Facility.findLocation").getResultList();
        return locationList;
    }

    public List<String> findAllBuilding(String location) {
        List buildingList = mgr.createNamedQuery("Facility.findBuidling").setParameter("location", location).getResultList();
        return buildingList;
    }

    public List<Facility> findByHostelID(Hostel curHostel) {

        List<Facility> facilityList = mgr.createNamedQuery("Facility.findAllByHostelID").setParameter("hostelID", curHostel).getResultList();

        return facilityList;
    }

    public List<Facility> findByDescription(String desc) {
        List<Facility> facilityList = mgr.createNamedQuery("Facility.findByDescription").setParameter("description", desc).getResultList();
        return facilityList;
    }
    
    public List<Facility> findLikeDescription(String desc, String location) {
        List<Facility> facilityList = new ArrayList();
        List<Facility> tempfacilityList = mgr.createNamedQuery("Facility.findLikeDescription").setParameter("description", desc + "%").getResultList();
        
        for(int i=0;i<tempfacilityList.size();i++){
            if(tempfacilityList.get(i).getHostelID().getLocation().equals(location)){
                facilityList.add(tempfacilityList.get(i));
            }
        }
        
        return facilityList;
    }   

    public List<Facility> findDistinctFacility() {
        List<Facility> facilityList = mgr.createNamedQuery("Facility.findDistinctFacility").getResultList();

        return facilityList;
    }

    public String generateID() {
        List<Facility> list = mgr.createNamedQuery("Facility.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getFacilityID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "FA";
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
