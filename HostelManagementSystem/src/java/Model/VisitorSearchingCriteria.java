
package Model;

public class VisitorSearchingCriteria {
    
    private String previousTerm;
    private String visitorID;
    private String visitorName;
    private String visitorIC;
    private String orderParameter;
    private String order;

    public VisitorSearchingCriteria() {
    }

    public VisitorSearchingCriteria(String previousTerm, String visitorID, String visitorName, String visitorIC, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.visitorID = visitorID;
        this.visitorName = visitorName;
        this.visitorIC = visitorIC;
        this.orderParameter = orderParameter;
        this.order = order;
    }

    public String getPreviousTerm() {
        return previousTerm;
    }

    public void setPreviousTerm(String previousTerm) {
        this.previousTerm = previousTerm;
    }

    public String getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorIC() {
        return visitorIC;
    }

    public void setVisitorIC(String visitorIC) {
        this.visitorIC = visitorIC;
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
