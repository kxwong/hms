
package Model;

import java.util.Date;

public class EntryRecordSearchingCriteria {
    
    private String previousTerm;
    private String entHolder;
    private String entID;
    private String tenantName;
    private String location;
    private String building;
    private Date entSTime;
    private Date entETime;
    private String gate;
    private String orderParameter;
    private String order;

    public EntryRecordSearchingCriteria() {
    }

    public EntryRecordSearchingCriteria(String previousTerm, String entHolder, String entID, String tenantName, String location, String building, Date entSTime, Date entETime, String gate, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.entHolder = entHolder;
        this.entID = entID;
        this.tenantName = tenantName;
        this.location = location;
        this.building = building;
        this.entSTime = entSTime;
        this.entETime = entETime;
        this.gate = gate;
        this.orderParameter = orderParameter;
        this.order = order;
    }

    public String getPreviousTerm() {
        return previousTerm;
    }

    public void setPreviousTerm(String previousTerm) {
        this.previousTerm = previousTerm;
    }

    public String getEntHolder() {
        return entHolder;
    }

    public void setEntHolder(String entHolder) {
        this.entHolder = entHolder;
    }

    public String getEntID() {
        return entID;
    }

    public void setEntID(String entID) {
        this.entID = entID;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Date getEntSTime() {
        return entSTime;
    }

    public void setEntSTime(Date entSTime) {
        this.entSTime = entSTime;
    }

    public Date getEntETime() {
        return entETime;
    }

    public void setEntETime(Date entETime) {
        this.entETime = entETime;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
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
