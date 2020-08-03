/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "facilitybooking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facilitybooking.findAll", query = "SELECT f FROM Facilitybooking f")
    , @NamedQuery(name = "Facilitybooking.findAllByDateRangeFacilityStatus", query = "SELECT f FROM Facilitybooking f WHERE f.bookTime BETWEEN :startDate AND :enddate AND f.bookFacility.facilityID = :id AND f.status = :status")
    , @NamedQuery(name = "Facilitybooking.findAllByDateRangeFacility", query = "SELECT f FROM Facilitybooking f WHERE f.bookTime BETWEEN :startDate AND :enddate AND f.bookFacility.facilityID = :id")
    , @NamedQuery(name = "Facilitybooking.findAllByDateRange", query = "SELECT f FROM Facilitybooking f WHERE f.bookTime BETWEEN :startDate AND :enddate ORDER BY f.bookTime ASC")
    , @NamedQuery(name = "Facilitybooking.findAllOrderByDESC", query = "SELECT f FROM Facilitybooking f ORDER BY f.bookingID DESC")
    , @NamedQuery(name = "Facilitybooking.findByFacility", query = "SELECT f FROM Facilitybooking f WHERE f.bookFacility = :bookFacility")
    , @NamedQuery(name = "Facilitybooking.findAllByHostellerID", query = "SELECT f FROM Facilitybooking f WHERE f.bookBy = :bookBy")
    , @NamedQuery(name = "Facilitybooking.findBookDate", query = "SELECT distinct(f.bookTime) FROM Facilitybooking f ORDER BY f.bookTime ASC")
    , @NamedQuery(name = "Facilitybooking.findUpdateDate", query = "SELECT distinct(f.updateTime) FROM Facilitybooking f ORDER BY f.updateTime ASC")
    , @NamedQuery(name = "Facilitybooking.findByBookingID", query = "SELECT f FROM Facilitybooking f WHERE f.bookingID = :bookingID")
    , @NamedQuery(name = "Facilitybooking.findByBookTime", query = "SELECT f FROM Facilitybooking f WHERE f.bookTime = :bookTime")
    , @NamedQuery(name = "Facilitybooking.findByBookTimeAndFacility", query = "SELECT f FROM Facilitybooking f WHERE f.bookTime = :bookTime AND f.bookFacility = :bookFacility")
    , @NamedQuery(name = "Facilitybooking.findByBookQuantity", query = "SELECT f FROM Facilitybooking f WHERE f.bookQuantity = :bookQuantity")
    , @NamedQuery(name = "Facilitybooking.findByRequestTime", query = "SELECT f FROM Facilitybooking f WHERE f.requestTime = :requestTime")
    , @NamedQuery(name = "Facilitybooking.findByUpdateTime", query = "SELECT f FROM Facilitybooking f WHERE f.updateTime = :updateTime")
    , @NamedQuery(name = "Facilitybooking.findByStatus", query = "SELECT f FROM Facilitybooking f WHERE f.status = :status")
    , @NamedQuery(name = "Facilitybooking.findByRemark", query = "SELECT f FROM Facilitybooking f WHERE f.remark = :remark")})
public class Facilitybooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "BookingID")
    private String bookingID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BookTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookTime;
    @Column(name = "BookQuantity")
    private Short bookQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RequestTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UpdateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;
    @Size(max = 100)
    @Column(name = "Remark")
    private String remark;
    @JoinColumn(name = "BookFacility", referencedColumnName = "FacilityID")
    @ManyToOne
    private Facility bookFacility;
    @JoinColumn(name = "BookBy", referencedColumnName = "HostellerID")
    @ManyToOne
    private Hosteller bookBy;

    public Facilitybooking() {
    }

    public Facilitybooking(String bookingID) {
        this.bookingID = bookingID;
    }

    public Facilitybooking(String bookingID, Date bookTime, Date requestTime, Date updateTime) {
        this.bookingID = bookingID;
        this.bookTime = bookTime;
        this.requestTime = requestTime;
        this.updateTime = updateTime;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Short getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(Short bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Facility getBookFacility() {
        return bookFacility;
    }

    public void setBookFacility(Facility bookFacility) {
        this.bookFacility = bookFacility;
    }

    public Hosteller getBookBy() {
        return bookBy;
    }

    public void setBookBy(Hosteller bookBy) {
        this.bookBy = bookBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingID != null ? bookingID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facilitybooking)) {
            return false;
        }
        Facilitybooking other = (Facilitybooking) object;
        if ((this.bookingID == null && other.bookingID != null) || (this.bookingID != null && !this.bookingID.equals(other.bookingID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Facilitybooking[ bookingID=" + bookingID + " ]";
    }

}
