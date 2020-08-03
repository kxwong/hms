package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class ConversationManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public ConversationManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addConversation(Conversation conversation) {
        mgr.persist(conversation);
        return true;
    }

    public Conversation findConversationByID(String id) {
        Conversation conversation = mgr.find(Conversation.class, id);
        return conversation;
    }

    public boolean deleteConversation(String id) {
        Conversation conversation = findConversationByID(id);
        if (conversation != null) {
            mgr.remove(conversation);
            return true;
        }
        return false;
    }

    public Account getIssueAccount(String id) throws Exception {
        return getConversationList(id).get(0).getReplyBy();
    }

    public Account getLastReply(String id) throws Exception {
        if (getConversationList(id).size() - 1 <= 0) {
            return getConversationList(id).get(0).getReplyBy();
        } else {
            return getConversationList(id).get(getConversationList(id).size() - 1).getReplyBy();
        }
    }

    public List<Conversation> getConversationList(String id) throws Exception {
        List<Conversation> coversationList = mgr.createNamedQuery("Conversation.findByCaseOrderByTime").setParameter("caseNumber", id).getResultList();
        return coversationList;
    }

    public List findAllConversation(Issue issue) {
        List<Conversation> conversationList = mgr.createNamedQuery("Conversation.findAllByCaseID").setParameter("caseNumber", issue).getResultList();
        return conversationList;
    }

    public String generateID(String id) throws Exception {
        List<Conversation> list = mgr.createNamedQuery("Conversation.findByCaseOrderIDDesc").setParameter("caseNumber", id).getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getContentID();
            serialNumber = Integer.parseInt(previousID.substring(previousID.length() - 4, previousID.length())) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        if (serialNumber < 10) {
            newSerial = "000" + newSerial;
        } else if (serialNumber < 100) {
            newSerial = "00" + newSerial;
        } else if (serialNumber < 1000) {
            newSerial = "0" + newSerial;
        }
        return id.substring(2) + "C" + newSerial;
    }

}
