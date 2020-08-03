package Model;

import java.util.Date;

public class FacilitybookingSearchingCriteria {

    private String previousTerm;
    private String bookingID;
    private String facilityID;
    private String bookedBy;
    private Date bookSTime;
    private Date bookETime;
    private Date updateSTime;
    private Date updateETime;
    private String status;
    private String orderParameter;
    private String order;

    public FacilitybookingSearchingCriteria() {
    }

    public FacilitybookingSearchingCriteria(String previousTerm, String bookingID, String facilityID, String bookedBy, Date bookSTime, Date bookETime, Date updateSTime, Date updateETime, String status, String orderParameter, String order) {
        this.previousTerm = previousTerm;
        this.bookingID = bookingID;
        this.facilityID = facilityID;
        this.bookedBy = bookedBy;
        this.bookSTime = bookSTime;
        this.bookETime = bookETime;
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

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public Date getBookSTime() {
        return bookSTime;
    }

    public void setBookSTime(Date bookSTime) {
        this.bookSTime = bookSTime;
    }

    public Date getBookETime() {
        return bookETime;
    }

    public void setBookETime(Date bookETime) {
        this.bookETime = bookETime;
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
