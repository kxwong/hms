package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class AnnouncementManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public AnnouncementManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addAnnouncement(Announcement announcement) {
        mgr.persist(announcement);
        return true;
    }

    public Announcement findAnnouncementByID(String id) {
        Announcement announcement = mgr.find(Announcement.class, id);
        return announcement;
    }

    public List<Announcement> findAllOrderByDateDesc() {
        List<Announcement> list = mgr.createNamedQuery("Announcement.findAllOrderByDateDesc").getResultList();
        return list;
    }

    public boolean deleteAnnouncement(String id) {
        Announcement announcement = findAnnouncementByID(id);
        if (announcement != null) {
            mgr.remove(announcement);
            return true;
        }
        return false;
    }

    public List<Announcement> retrieveAllAnnouncement() {
        List<Announcement> announcementList = mgr.createNamedQuery("Announcement.findAll").getResultList();
        return announcementList;
    }

    public List<Announcement> retrieveAllAnnouncementDesc() {
        List<Announcement> announcementList = mgr.createNamedQuery("Announcement.findAllByDesc").getResultList();
        return announcementList;
    }
    
    public boolean updateAnnouncement(Announcement announcement) throws Exception {
        Announcement tempoAnnouncement = findAnnouncementByID(announcement.getAnnounceID());
        if (tempoAnnouncement != null) {
            tempoAnnouncement.setContent(announcement.getContent());
            tempoAnnouncement.setTitle(announcement.getTitle());
            tempoAnnouncement.setAttachmentList(announcement.getAttachmentList());
            return true;
        }
        return false;
    }

    public String generateID() {
        List<Announcement> list = mgr.createNamedQuery("Announcement.findAllOrderDesc").getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getAnnounceID();
            serialNumber = Integer.parseInt(previousID.substring(2)) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        String newID = "AN";
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
