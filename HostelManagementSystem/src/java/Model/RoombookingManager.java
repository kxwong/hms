package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class RoombookingManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public RoombookingManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addRoombooking(Roombooking roombooking) {
        mgr.persist(roombooking);
        return true;
    }

    public Roombooking findRoombookingByID(String id) {
        Roombooking roombooking = mgr.find(Roombooking.class, id);
        return roombooking;
    }

    public Roombooking findByBillNo(String billNo) {
        try{
        Roombooking obj = (Roombooking) mgr.createNamedQuery("Roombooking.findByBillNo").setParameter("billNo", billNo).getSingleResult();
        return obj;
        }catch(Exception ex){
            return null;
        }
    }
    
    public List<Roombooking> findAllWithParameter(String requestNo, String requestType, Date requestDateStart, Date requestDateEnd, Date updateDateStart, Date updateDateEnd, String roomNo, String billNo, String status, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT r FROM Roombooking r WHERE r.requestNo LIKE :requestNo AND r.requestType LIKE :requestType "
                + "AND r.requestDate BETWEEN :requestDateStart AND :requestDateEnd AND r.updateDate BETWEEN :updateDateStart AND :updateDateEnd "
                + "AND r.requestRoom.roomNo LIKE :roomNo AND r.status LIKE :status ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Roombooking.findAllWithParameter", query);
        List<Roombooking> list = mgr.createNamedQuery("Roombooking.findAllWithParameter").setParameter("requestNo", "%" + requestNo + "%").setParameter("requestType", "%" + requestType + "%")
                .setParameter("requestDateStart", requestDateStart).setParameter("requestDateEnd", requestDateEnd)
                .setParameter("updateDateStart", updateDateStart).setParameter("updateDateEnd", updateDateEnd)
                .setParameter("roomNo", "%" + roomNo + "%").setParameter("status", "%" + status + "%")
                .getResultList();
        return findAllWithBill(list, billNo);
    }

    public List<Date> findRequestDate() {
        List<Date> list = mgr.createNamedQuery("Roombooking.findRequestDate").getResultList();
        return list;
    }
    

    public List<Date> findUpdateDate() {
        List<Date> list = mgr.createNamedQuery("Roombooking.findUpdateDate").getResultList();
        return list;
    }

    public boolean deleteRoombooking(String id) {
        Roombooking roombooking = findRoombookingByID(id);
        if (roombooking != null) {
            mgr.remove(roombooking);
            return true;
        }
        return false;
    }

    public boolean updateRoombookingDetails(Roombooking roombooking) throws Exception {
        Roombooking tempoRoombooking = findRoombookingByID(roombooking.getRequestNo());
        if (tempoRoombooking != null) {
            try {
                tempoRoombooking.setBillNo(roombooking.getBillNo());
            } catch (Exception ex) {
            }
            tempoRoombooking.setStatus(roombooking.getStatus());
            tempoRoombooking.setRemark(roombooking.getRemark());
            tempoRoombooking.setUpdateDate(roombooking.getUpdateDate());
        }
        return false;
    }

    private List<Roombooking> findAllWithBill(List<Roombooking> list, String billNo) {
        List<Integer> indexArray = new ArrayList<>();
        if (!billNo.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getBillNo() == null) {
                    indexArray.add(i);
                } else {
                    if (!list.get(i).getBillNo().getBillNo().matches(billNo)) {
                        indexArray.add(i);
                    }
                }
            }
        }
        for (int i = indexArray.size() - 1; i >= 0; i--) {
            list.remove(list.get(Integer.parseInt(String.valueOf(indexArray.get(i)))));
        }
        return list;
    }

    public boolean updateRoombooking(Roombooking roombooking) throws Exception {
        Roombooking tempoRoombooking = findRoombookingByID(roombooking.getRequestNo());
        if (tempoRoombooking != null) {
            tempoRoombooking.setBillNo(roombooking.getBillNo());
            tempoRoombooking.setRemark(roombooking.getRemark());
            tempoRoombooking.setRequestBy(roombooking.getRequestBy());
            tempoRoombooking.setRequestDate(roombooking.getRequestDate());
            tempoRoombooking.setRequestNo(roombooking.getRequestNo());
            tempoRoombooking.setRequestRoom(roombooking.getRequestRoom());
            tempoRoombooking.setRequestType(roombooking.getRequestType());
            tempoRoombooking.setStatus(roombooking.getStatus());
            tempoRoombooking.setUpdateDate(roombooking.getUpdateDate());            
            return true;           
        }
        return false;
    }
    
    public List<Roombooking> retrievePendingBooking(String status, Hosteller requestBy) {
        List<Roombooking> pendingBookingList = mgr.createNamedQuery("Roombooking.findAllByStatus").setParameter("status", status).setParameter("requestBy", requestBy).getResultList();
        
        return pendingBookingList;       
    }
    
    public List<Roombooking> retrieveAllBookingByHosteller(Hosteller requestBy) {
        List<Roombooking> bookingList = mgr.createNamedQuery("Roombooking.findAllOrderByHosteller").setParameter("requestBy", requestBy).getResultList();
        
        return bookingList;       
    }

    public String generateID() {
        List<Roombooking> currentRoomBookingList = mgr.createNamedQuery("Roombooking.findAllOrderByDESC").getResultList();
        String currentLastID = new String();
        String newRoomBookingID = "BR";

        if (currentRoomBookingList.size() == 0) {
            return newRoomBookingID + "0001";
        } else {
            currentLastID = currentRoomBookingList.get(0).getRequestNo();
            String tempoSerial = currentLastID.substring(2);
            int currentSerial = Integer.parseInt(tempoSerial) + 1;

            tempoSerial = String.valueOf(currentSerial);
            
            if (tempoSerial.length() == 1) {
                newRoomBookingID += "000" + tempoSerial;
            } else if (tempoSerial.length() == 2) {
                newRoomBookingID += "00" + tempoSerial;
            } else if (tempoSerial.length() == 3) {
                newRoomBookingID += "0" + currentSerial;
            } else if (tempoSerial.length() == 4) {
                newRoomBookingID += tempoSerial;
            }
            return newRoomBookingID;
        }
    }
}
