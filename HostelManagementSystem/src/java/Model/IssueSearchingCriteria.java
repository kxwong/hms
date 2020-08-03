
package Model;

import java.util.Date;

public class IssueSearchingCriteria {
    
    private String previousTerm;
    private String caseno;
    private String title;
    private String category;
    private String issue;
    private Date issueSTime;
    private Date issueETime;
    private Date updateSTime;
    private Date updateETime;
    private String status;
    private String orderParameter;
    private String order;

    public IssueSearchingCriteria() {
    }

    public IssueSearchingCriteria(String previousTerm, String caseno, String title, String category, String issue, Date issueSTime, Date issueETime, Date updateSTime, Date updateETime, String status, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.caseno = caseno;
        this.title = title;
        this.category = category;
        this.issue = issue;
        this.issueSTime = issueSTime;
        this.issueETime = issueETime;
        this.updateSTime = updateSTime;
        this.updateETime = updateETime;
        this.status = status;
        this.orderParameter = orderParameter;
        this.order = order;
    }

    public String getPreviousTerm() {
        return previousTerm;
    }

    public void setPreviousTerm(String previousTerm) {
        this.previousTerm = previousTerm;
    }

    public String getCaseno() {
        return caseno;
    }

    public void setCaseno(String caseno) {
        this.caseno = caseno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Date getIssueSTime() {
        return issueSTime;
    }

    public void setIssueSTime(Date issueSTime) {
        this.issueSTime = issueSTime;
    }

    public Date getIssueETime() {
        return issueETime;
    }

    public void setIssueETime(Date issueETime) {
        this.issueETime = issueETime;
    }

    public Date getUpdateSTime() {
        return updateSTime;
    }

    public void setUpdateSTime(Date updateSTime) {
        this.updateSTime = updateSTime;
    }

    public Date getUpdateETime() {
        return updateETime;
    }

    public void setUpdateETime(Date updateETime) {
        this.updateETime = updateETime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderParameter() {
        return orderParameter;
    }

    public void setOrderParameter(String orderParameter) {
        this.orderParameter = orderParameter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
