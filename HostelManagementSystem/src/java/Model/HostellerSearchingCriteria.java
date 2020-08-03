
package Model;

public class HostellerSearchingCriteria {
    
    private String previousTerm;
    private String name;
    private String room;
    private String email;
    private String contactno;
    private String requestno;
    private String status;
    private String orderParameter;
    private String order;

    public HostellerSearchingCriteria() {
    }

    public HostellerSearchingCriteria(String previousTerm, String name, String room, String email, String contactno, String requestno, String status, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.name = name;
        this.room = room;
        this.email = email;
        this.contactno = contactno;
        this.requestno = requestno;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
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
