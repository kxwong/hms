
package Model;

import java.util.Date;

public class RegistrationReqSearchingCriteria {
    
    private String previousTerm;
    private Date requestSTime;
    private Date requestETime;
    private String requestNo;
    private String tenantName;
    private Date updateSTime;
    private Date updateETime;
    private String status;
    private String orderParameter;
    private String order;

    public RegistrationReqSearchingCriteria() {
    }

    public RegistrationReqSearchingCriteria(String previousTerm,Date requestSTime, Date requestETime, String requestNo, String tenantName, Date updateSTime, Date updateETime,String status,String orderParameter,String order) {
        this.previousTerm = previousTerm;
        this.requestSTime = requestSTime;
        this.requestETime = requestETime;
        this.requestNo = requestNo;
        this.tenantName = tenantName;
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
    
    public Date getRequestSTime() {
        return requestSTime;
    }

    public void setRequestSTime(Date requestSTime) {
        this.requestSTime = requestSTime;
    }

    public Date getRequestETime() {
        return requestETime;
    }

    public void setRequestETime(Date requestETime) {
        this.requestETime = requestETime;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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
