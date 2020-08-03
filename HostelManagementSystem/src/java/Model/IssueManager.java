package Model;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class IssueManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public IssueManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addIssue(Issue issue) {
        mgr.persist(issue);
        return true;
    }

    public Issue findIssueByID(String id) {
        Issue issue = mgr.find(Issue.class, id);
        return issue;
    }

    public List<Issue> findAllWithParameter(String caseNo, String title, String category, String issueType, Date issueDateStart, Date issueDateEnd, Date updateDateStart, Date updateDateEnd, String status, String orderParameter, String order) {
        Query query = this.mgr.createQuery("SELECT i FROM Issue i WHERE i.caseNo LIKE :caseNo AND i.title LIKE :title AND i.category LIKE :category AND i.issueType LIKE :issueType "
                + "AND i.issueDate BETWEEN :issueDateStart AND :issueDateEnd AND i.updateDate BETWEEN :updateDateStart AND :updateDateEnd "
                + "AND i.status LIKE :status ORDER BY " + orderParameter + " " + order);
        this.mgr.getEntityManagerFactory().addNamedQuery("Issue.findAllWithParameter", query);
        List<Issue> list = mgr.createNamedQuery("Issue.findAllWithParameter").setParameter("caseNo", "%" + caseNo + "%").setParameter("title", "%" + title + "%").setParameter("category", "%" + category + "%").setParameter("issueType", "%" + issueType + "%")
                .setParameter("issueDateStart", issueDateStart).setParameter("issueDateEnd", issueDateEnd)
                .setParameter("updateDateStart", updateDateStart).setParameter("updateDateEnd", updateDateEnd)
                .setParameter("status", "%" + status + "%")
                .getResultList();
        return list;
    }

    public boolean deleteIssue(String id) {
        Issue issue = findIssueByID(id);
        if (issue != null) {
            mgr.remove(issue);
            return true;
        }
        return false;
    }

    public boolean updateIssue(Issue issue) throws Exception {
        Issue tempoIssue = findIssueByID(issue.getCaseNo());
        if (tempoIssue != null) {
            tempoIssue.setAttachmentList(issue.getAttachmentList());
            tempoIssue.setCaseNo(issue.getCaseNo());
            tempoIssue.setCategory(issue.getCategory());
            tempoIssue.setConversationList(issue.getConversationList());
            tempoIssue.setIssueBy(issue.getIssueBy());
            tempoIssue.setIssueDate(issue.getIssueDate());
            tempoIssue.setUpdateDate(issue.getUpdateDate());
            tempoIssue.setIssueType(issue.getIssueType());
            tempoIssue.setStatus(issue.getStatus());
            tempoIssue.setTitle(issue.getTitle());

            return true;
        }
        return false;
    }

    public boolean updateIssueStatus(Issue issue) throws Exception {
        Issue tempoIssue = findIssueByID(issue.getCaseNo());
        if (tempoIssue != null) {
            tempoIssue.setStatus(issue.getStatus());
            tempoIssue.setUpdateDate(issue.getUpdateDate());
            return true;
        }
        return false;
    }

    public List<Date> findIssueDate() {
        List<Date> list = mgr.createNamedQuery("Issue.findIssueDate").getResultList();
        return list;
    }

    public List<Date> findUpdateDate() {
        List<Date> list = mgr.createNamedQuery("Issue.findUpdateDate").getResultList();
        return list;
    }

    public List<String> findCategory() {
        List<String> list = mgr.createNamedQuery("Issue.findCategory").getResultList();
        return list;
    }

    public List<Issue> findAllByStatus(String status) {
        List<Issue> issueList = mgr.createNamedQuery("Issue.findAllByStatus").setParameter("status", status).getResultList();
        return issueList;
    }

    public List<Issue> findAllByHosteller(Hosteller hosteller) {
        List<Issue> issueList = mgr.createNamedQuery("Issue.findAllByHosteller").setParameter("issueBy", hosteller).getResultList();
        return issueList;
    }

    public List<Issue> findAllByDateRange(Date startDate, Date endDate) {
        List<Issue> list = mgr.createNamedQuery("Issue.findAllByDateRange").setParameter("startDate", startDate).setParameter("enddate", endDate).getResultList();
        return list;
    }

    public List<Issue> findAllByDateRangeCategory(Date startDate, Date endDate, String category) {
        List<Issue> list = mgr.createNamedQuery("Issue.findAllByDateRangeCategory").setParameter("startDate", startDate).setParameter("enddate", endDate).setParameter("category", category).getResultList();
        return list;
    }

    public String generateID() {
        List<Issue> currentIssueList = mgr.createNamedQuery("Issue.findAll").getResultList();
        String currentLastID = new String();
        String newIssueID = "CA";

        if (currentIssueList.size() == 0) {
            return newIssueID + "0001";
        } else {
            currentLastID = currentIssueList.get(currentIssueList.size() - 1).getCaseNo();
            String tempoSerial = currentLastID.substring(2);
            int currentSerial = Integer.parseInt(tempoSerial) + 1;

            tempoSerial = String.valueOf(currentSerial);

            if (tempoSerial.length() == 1) {
                newIssueID += "000" + tempoSerial;
            } else if (tempoSerial.length() == 2) {
                newIssueID += "00" + tempoSerial;
            } else if (tempoSerial.length() == 3) {
                newIssueID += "0" + currentSerial;
            } else if (tempoSerial.length() == 4) {
                newIssueID += tempoSerial;
            }
            return newIssueID;
        }
    }

}
