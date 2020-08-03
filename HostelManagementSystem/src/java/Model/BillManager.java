package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class BillManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public BillManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addBill(Bill bill) {
        mgr.persist(bill);
        return true;
    }

    public Bill findBillByID(String id) {
        Bill bill = mgr.find(Bill.class, id);
        return bill;
    }

    public List<Bill> findAll() {
        List<Bill> list = mgr.createNamedQuery("Bill.findAll").getResultList();
        return list;
    }
    
    public List<Bill> findAllByDateRange(Date startDate, Date endDate) {
        List<Bill> list = mgr.createNamedQuery("Bill.findAllByDateRange").setParameter("startDate", startDate).setParameter("enddate", endDate).getResultList();
        return list;
    }

    public List<Bill> findByStatus(String status) {
        List<Bill> list = mgr.createNamedQuery("Bill.findByStatus").setParameter("status", status).getResultList();
        return list;
    }
    
    public Bill findByRoomBooking(String requestNo) {
        Bill bill = (Bill) mgr.createNamedQuery("Bill.findByRoomBooking").setParameter("requestNo", requestNo).getSingleResult();
        return bill;
    }

    public List<Bill> findAllWithParameter(String billNo, String desc, String issueTo, Date issueDateStart, Date issueDateEnd, Date dueDateStart, Date dueDateEnd, String status, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT b FROM Bill b WHERE b.billNo LIKE :billNo AND concat(b.issueTo.firstName,' ',b.issueTo.middleName,' ',b.issueTo.lastName) like :issueTo AND b.description LIKE :desc "
                + "AND b.issueDate BETWEEN :issueDateStart AND :issueDateEnd AND b.dueDate BETWEEN :dueDateStart AND :dueDateEnd "
                + "AND b.status LIKE :status ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Bill.findAllWithParameter", query);
        List<Bill> list = mgr.createNamedQuery("Bill.findAllWithParameter").setParameter("billNo", "%" + billNo + "%").setParameter("issueTo", "%" + issueTo + "%").setParameter("desc", "%" + desc + "%")
                .setParameter("issueDateStart", issueDateStart).setParameter("issueDateEnd", issueDateEnd)
                .setParameter("dueDateStart", dueDateStart).setParameter("dueDateEnd", dueDateEnd)
                .setParameter("status", "%" + status + "%")
                .getResultList();
        return list;
    }

    public boolean deleteBill(String id) {
        Bill bill = findBillByID(id);
        if (bill != null) {
            mgr.remove(bill);
            return true;
        }
        return false;
    }

    public boolean updateBillStatus(Bill bill) throws Exception {
        Bill tempoBill = findBillByID(bill.getBillNo());
        if (tempoBill != null) {
            tempoBill.setReceiptNo(bill.getReceiptNo());
            tempoBill.setStatus(bill.getStatus());
            return true;
        }
        return false;
    }

    public boolean updateBill(Bill bill) throws Exception {
        Bill tempoBill = findBillByID(bill.getBillNo());
        if (tempoBill != null) {;
            tempoBill.setPaidDate(bill.getPaidDate());
            tempoBill.setStatus(bill.getStatus());
            tempoBill.setTotalAmount(bill.getTotalAmount());
            return true;
        }
        return false;
    }

    public List<Bill> findAllByHosteller(Hosteller curHosteller) {
        List<Bill> billList = mgr.createNamedQuery("Bill.findAllByHosteller").setParameter("issueTo", curHosteller).getResultList();
        return billList;
    }

    public List<Bill> findAllExceptStatusByHosteller(Hosteller curHosteller, String status) {
        List<Bill> billList = mgr.createNamedQuery("Bill.findAllByStatusByHosteller").setParameter("issueTo", curHosteller).setParameter("status", status).getResultList();
        return billList;
    }

    public List<Date> findAllDateMonth(Hosteller curHosteller) {
        List<Date> dateList = mgr.createNamedQuery("Bill.findAllMonthYear").setParameter("issueTo", curHosteller).getResultList();
        return dateList;
    }

    public String generateID(Date date) {
        List<Bill> previousList = mgr.createNamedQuery("Bill.findAllOrderDesc").getResultList();
        List<Bill> list = new ArrayList<>();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        for (int i = 0; i < previousList.size(); i++) {
            int referYear = Integer.parseInt(previousList.get(i).getBillNo().substring(0, 4));
            int referMonth = Integer.parseInt(previousList.get(i).getBillNo().substring(4, 6));
            if (year == referYear & month == referMonth) {
                list.add(previousList.get(i));
            }
        }
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getBillNo();
            serialNumber = Integer.parseInt(previousID.substring(7)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String monthRef = "";
        if (month < 10) {
            monthRef = "0" + String.valueOf(month);
        } else {
            monthRef = String.valueOf(month);
        }
        String newID = String.valueOf(year) + monthRef + "B";
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

    public List<Date> findIssueDate() {
        List<Date> list = mgr.createNamedQuery("Bill.findIssueDate").getResultList();
        return list;
    }

    public List<Date> findDueDate() {
        List<Date> list = mgr.createNamedQuery("Bill.findDueDate").getResultList();
        return list;
    }
}
