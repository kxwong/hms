package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class FloorplanManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public FloorplanManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addFloorplan(Floorplan floorplan) {
        mgr.persist(floorplan);
        return true;
    }

    public Floorplan findFloorplanByID(String id) {
        Floorplan floorplan = mgr.find(Floorplan.class, id);
        return floorplan;
    }

    public boolean updateFloorplanStatusInactive(Floorplan floorplan) {
        Floorplan tempoFloorplan = findFloorplanByID(floorplan.getFloorplanID());
        if (tempoFloorplan != null) {
            tempoFloorplan.setStatus("Inactive");
            return true;
        }
        return false;
    }
    
    public boolean updateFloorplanStatusActive(Floorplan floorplan) {
        Floorplan tempoFloorplan = findFloorplanByID(floorplan.getFloorplanID());
        if (tempoFloorplan != null) {
            tempoFloorplan.setStatus("Active");
            return true;
        }
        return false;
    }

    public boolean updateFloorplan(Floorplan floorplan, boolean imageChange) throws Exception {
        Floorplan tempoFloorplan = findFloorplanByID(floorplan.getFloorplanID());
        if (tempoFloorplan != null) {
            tempoFloorplan.setPeopleCapacity(floorplan.getPeopleCapacity());
            tempoFloorplan.setRoomCapacity(floorplan.getRoomCapacity());
            tempoFloorplan.setFloor(floorplan.getFloor());
            tempoFloorplan.setImage(floorplan.getImage());
            if (imageChange) {
                for (int i = 0; i < floorplan.getRoomList().size(); i++) {
                    RoomManager roomManager = new RoomManager(mgr);
                    Room room = roomManager.findRoomByID(floorplan.getRoomList().get(i).getRoomNo());
                    if (room != null) {
                        room.setStatus("Unavailable");
                    }
                }
            }
            return true;
        }
        return false;
    }

    public List<Floorplan> findAll() {
        List floorplanList = mgr.createNamedQuery("Floorplan.findAll").getResultList();
        return floorplanList;
    }

    public List<Floorplan> findAllOrderByFloorAsc() {
        List floorplanList = mgr.createNamedQuery("Floorplan.findAllByFloorAsc").getResultList();
        return floorplanList;
    }

    public String generateID() {
        List<Floorplan> list = mgr.createNamedQuery("Floorplan.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getFloorplanID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "FP";
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

    public boolean floorExisted(Hostel hostelID, int floor) {
        boolean result = false;
        List<Floorplan> floorplanList = mgr.createNamedQuery("Floorplan.findHostelFloorDuplicate").setParameter("floor", floor).setParameter("hostelID", hostelID).getResultList();
        if (floorplanList.size() >= 1) {
            result = true;
        }
        return result;
    }

    public List<String> findAllLocation() {
        List locationList = mgr.createNamedQuery("Floorplan.findLocation").getResultList();
        return locationList;
    }

    public List<String> findAllBuilding(String location) {
        List buildingList = mgr.createNamedQuery("Floorplan.findBuidling").setParameter("location", location).getResultList();
        return buildingList;
    }

    public List<Floorplan> findByLocation(String location){
        List<Floorplan> floorplanList = mgr.createNamedQuery("Floorplan.findByHostelLocation").setParameter("location", location).getResultList();
        
        if(floorplanList.size()>=1){
            return floorplanList;
        }
        return floorplanList;
    }
}
