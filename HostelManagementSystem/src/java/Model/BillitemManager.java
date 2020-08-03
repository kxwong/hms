
package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class BillitemManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public BillitemManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addBillitem (Billitem billitem){
        mgr.persist(billitem);
        return true;
    }
    
    public Billitem findBillitemByID(String id){
        Billitem billitem = mgr.find(Billitem.class, id);
        return billitem;
    }
    
    public boolean deleteBillitem(String id) {
        Billitem billitem = findBillitemByID(id);
        if (billitem != null) {
            mgr.remove(billitem);
            return true;
        }
        return false;
    }
    
    public boolean updateBillStatusitem(Billitem billitem) throws Exception {
        Billitem tempoBillitem = findBillitemByID(billitem.getBillItemNo());
        if (tempoBillitem != null) {
        }
        return false;
    }
    
    public List<Billitem> findItemListByBillNo (String billNo){
        List<Billitem> billItemList = mgr.createNamedQuery("Billitem.findItemListByBillNo").setParameter("billNo", billNo).getResultList();
        return billItemList;
    }
    
    public String generateID() {
        List<Billitem> list = mgr.createNamedQuery("Billitem.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getBillItemNo();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "BI";
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
    
}
