package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class EntcardManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public EntcardManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addEntcard(Entcard entcard) {
        mgr.persist(entcard);
        return true;
    }

    public Entcard findEntcardByID(String id) {
        Entcard entcard = mgr.find(Entcard.class, id);
        return entcard;
    }

    public List<Entcard> findAll() {
        List<Entcard> list = mgr.createNamedQuery("Entcard.findAll").getResultList();
        return list;
    }

    public boolean deleteEntcard(String id) {
        Entcard entcard = findEntcardByID(id);
        if (entcard != null) {
            List<Entrecord> list = mgr.createNamedQuery("Entrecord.findByEntCard").setParameter("entCardNo", id).getResultList();
            for (Entrecord entRecord : list) {
                mgr.remove(entRecord);
            }
            mgr.remove(entcard);
            return true;
        }
        return false;
    }

    public boolean updateEntcard(Entcard entcard) throws Exception {
        Entcard tempoEntcard = findEntcardByID(entcard.getEntCardNo());
        if (tempoEntcard != null) {
        }
        return false;
    }

}
