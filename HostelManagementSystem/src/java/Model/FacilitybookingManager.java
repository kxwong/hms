package Model;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class FacilitybookingManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public FacilitybookingManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addFacilitybooking(Facilitybooking facilitybooking) {
        mgr.persist(facilitybooking);
        return true;
    }

    public Facilitybooking findFacilitybookingByID(String id) {
        Facilitybooking facilitybooking = mgr.find(Facilitybooking.class, id);
        return facilitybooking;
    }

    public List<Facilitybooking> findAllWithParameter(String bookingID, String facilityID, String bookedBy, Date bookTimeStart, Date bookTimeEnd, Date updateDateStart, Date updateDateEnd, String status, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT f FROM Facilitybooking f WHERE f.bookingID LIKE :bookingID AND f.bookFacility.facilityID LIKE :facilityID AND concat(f.bookBy.firstName,' ',f.bookBy.middleName,' ',f.bookBy.lastName) like :bookedBy "
                + "AND f.bookTime BETWEEN :bookTimeStart AND :bookTimeEnd AND f.updateTime BETWEEN :updateDateStart AND :updateDateEnd "
                + "AND f.status LIKE :status ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Facilitybooking.findAllWithParameter", query);
        List<Facilitybooking> list = mgr.createNamedQuery("Facilitybooking.findAllWithParameter").setParameter("bookingID", "%" + bookingID + "%").setParameter("facilityID", "%" + facilityID + "%").setParameter("bookedBy", "%" + bookedBy + "%")
                .setParameter("bookTimeStart", bookTimeStart).setParameter("bookTimeEnd", bookTimeEnd)
                .setParameter("updateDateStart", updateDateStart).setParameter("updateDateEnd", updateDateEnd)
                .setParameter("status", "%" + status + "%")
                .getResultList();
        return list;
    }

    public boolean deleteFacilitybooking(String id) {
        Facilitybooking facilitybooking = findFacilitybookingByID(id);
        if (facilitybooking != null) {
            mgr.remove(facilitybooking);
            return true;
        }
        return false;
    }

    public boolean updateFacilitybooking(Facilitybooking facilitybooking) throws Exception {
        Facilitybooking tempoFacilitybooking = findFacilitybookingByID(facilitybooking.getBookingID());
        if (tempoFacilitybooking != null) {
            tempoFacilitybooking.setBookBy(facilitybooking.getBookBy());
            tempoFacilitybooking.setBookFacility(facilitybooking.getBookFacility());
            tempoFacilitybooking.setBookQuantity(facilitybooking.getBookQuantity());
            tempoFacilitybooking.setBookTime(facilitybooking.getBookTime());
            tempoFacilitybooking.setBookingID(facilitybooking.getBookingID());
            tempoFacilitybooking.setRemark(facilitybooking.getRemark());
            tempoFacilitybooking.setRequestTime(facilitybooking.getRequestTime());
            tempoFacilitybooking.setStatus(facilitybooking.getStatus());
            tempoFacilitybooking.setUpdateTime(facilitybooking.getUpdateTime());
            return true;
        }
        return false;
    }

    public boolean updateFacilitybookingStatus(Facilitybooking facilitybooking) throws Exception {
        Facilitybooking tempoFacilitybooking = findFacilitybookingByID(facilitybooking.getBookingID());
        if (tempoFacilitybooking != null) {
            tempoFacilitybooking.setStatus(facilitybooking.getStatus());
            try {
                if (facilitybooking.getRemark() != null) {
                    tempoFacilitybooking.setRemark(facilitybooking.getRemark());
                }
            } catch (Exception ex) {
            }
            return true;
        }
        return false;
    }

    public List<Facilitybooking> findBookingByFacility(Facility facility) {
        List<Facilitybooking> list = mgr.createNamedQuery("Facilitybooking.findByFacility").setParameter("bookFacility", facility).getResultList();
        return list;
    }

    public List<Facilitybooking> findAllByDateRange(Date startDate, Date endDate) {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findAllByDateRange").setParameter("startDate", startDate).setParameter("enddate", endDate).getResultList();
        return currentFacilitybookingList;
    }
    
    public List<Facilitybooking> findAllByDateRangeFacility(Date startDate, Date endDate,String id) {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findAllByDateRangeFacility").setParameter("startDate", startDate).setParameter("enddate", endDate).setParameter("id", id).getResultList();
        return currentFacilitybookingList;
    }
    public List<Facilitybooking> findAllByDateRangeFacilityStatus(Date startDate, Date endDate,String id,String status) {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findAllByDateRangeFacilityStatus").setParameter("startDate", startDate).setParameter("enddate", endDate).setParameter("id", id).setParameter("status", status).getResultList();
        return currentFacilitybookingList;
    }
    
    public List<Date> findBookDate() {
        List<Date> list = mgr.createNamedQuery("Facilitybooking.findBookDate").getResultList();
        return list;
    }

    public List<Date> findUpdateDate() {
        List<Date> list = mgr.createNamedQuery("Facilitybooking.findUpdateDate").getResultList();
        return list;
    }

    public List<Facilitybooking> findAllByHosteller(Hosteller curHosteller) {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findAllByHostellerID").setParameter("bookBy", curHosteller).getResultList();

        return currentFacilitybookingList;
    }
    
    public List<Facilitybooking> findAllByTimeAndFacility(Facility facility, Date date) {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findByBookTimeAndFacility").setParameter("bookFacility", facility).setParameter("bookTime", date).getResultList();

        return currentFacilitybookingList;
    }
    
    public String generateID() {
        List<Facilitybooking> currentFacilitybookingList = mgr.createNamedQuery("Facilitybooking.findAllOrderByDESC").getResultList();
        String currentLastID = new String();
        String newFacilityBookingID = "BF";

        if (currentFacilitybookingList.size() == 0) {
            return newFacilityBookingID + "0001";
        } else {
            currentLastID = currentFacilitybookingList.get(0).getBookingID();
            String tempoSerial = currentLastID.substring(2);
            int currentSerial = Integer.parseInt(tempoSerial) + 1;

            tempoSerial = String.valueOf(currentSerial);

            if (tempoSerial.length() == 1) {
                newFacilityBookingID += "000" + tempoSerial;
            } else if (tempoSerial.length() == 2) {
                newFacilityBookingID += "00" + tempoSerial;
            } else if (tempoSerial.length() == 3) {
                newFacilityBookingID += "0" + currentSerial;
            } else if (tempoSerial.length() == 4) {
                newFacilityBookingID += tempoSerial;
            }
            return newFacilityBookingID;
        }
    }

}
