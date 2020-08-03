package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class ReceiptManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public ReceiptManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addReceipt(Receipt receipt) {
        mgr.persist(receipt);
        return true;
    }

    public List<Receipt> findAll() {
        List<Receipt> receiptList = mgr.createNamedQuery("Receipt.findAll").getResultList();
        return receiptList;
    }

    public Receipt findReceiptByID(String id) {
        Receipt receipt = mgr.find(Receipt.class, id);
        return receipt;
    }

    public boolean deleteReceipt(String id) {
        Receipt receipt = findReceiptByID(id);
        if (receipt != null) {
            mgr.remove(receipt);
            return true;
        }
        return false;
    }

    public boolean updateReceipt(Receipt receipt) throws Exception {
        Receipt tempoReceipt = findReceiptByID(receipt.getReceiptNo());
        if (tempoReceipt != null) {
        }
        return false;
    }

    public String generateID(Date date) {
        List<Receipt> previousList = mgr.createNamedQuery("Receipt.findAllOrderDesc").getResultList();
        List<Receipt> list = new ArrayList<>();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        for (int i = 0; i < previousList.size(); i++) {
            int referYear = Integer.parseInt(previousList.get(i).getReceiptNo().substring(0, 4));
            int referMonth = Integer.parseInt(previousList.get(i).getReceiptNo().substring(4, 6));
            if (year == referYear & month == referMonth) {
                list.add(previousList.get(i));
            }
        }
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getReceiptNo();
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
        String newID = String.valueOf(year) + monthRef + "R";
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
