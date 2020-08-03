package Model;

import java.util.Date;

public class BillSearchingCriteria {

    private String previousTerm;
    private String billNo;
    private String desc;
    private String issueTo;
    private Date issueSTime;
    private Date issueETime;
    private Date dueSTime;
    private Date dueETime;
    private String status;
    private String orderParameter;
    private String order;

    public BillSearchingCriteria() {
    }

    public BillSearchingCriteria(String previousTerm, String billNo, String desc, String issueTo, Date issueSTime, Date issueETime, Date dueSTime, Date dueETime, String status, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.billNo = billNo;
        this.desc = desc;
        this.issueTo = issueTo;
        this.issueSTime = issueSTime;
        this.issueETime = issueETime;
        this.dueSTime = dueSTime;
        this.dueETime = dueETime;
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(String issueTo) {
        this.issueTo = issueTo;
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

    public Date getDueSTime() {
        return dueSTime;
    }

    public void setDueSTime(Date dueSTime) {
        this.dueSTime = dueSTime;
    }

    public Date getDueETime() {
        return dueETime;
    }

    public void setDueETime(Date dueETime) {
        this.dueETime = dueETime;
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
