package Model;

import java.util.Date;

public class RoombookingSearchingCriteria {

    private String previousTerm;
    private String requestNo;
    private String requestType;
    private Date requestSTime;
    private Date requestETime;
    private String requestRoom;
    private String requestBillNo;
    private Date updateSTime;
    private Date updateETime;
    private String status;
    private String orderParameter;
    private String order;

    public RoombookingSearchingCriteria() {
    }

    public RoombookingSearchingCriteria(String previousTerm, String requestNo, String requestType, Date requestSTime, Date requestETime, String requestRoom, String requestBillNo, Date updateSTime, Date updateETime, String status, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.requestNo = requestNo;
        this.requestType = requestType;
        this.requestSTime = requestSTime;
        this.requestETime = requestETime;
        this.requestRoom = requestRoom;
        this.requestBillNo = requestBillNo;
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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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

    public String getRequestRoom() {
        return requestRoom;
    }

    public void setRequestRoom(String requestRoom) {
        this.requestRoom = requestRoom;
    }

    public String getRequestBillNo() {
        return requestBillNo;
    }

    public void setRequestBillNo(String requestBillNo) {
        this.requestBillNo = requestBillNo;
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
