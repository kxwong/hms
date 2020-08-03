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
@Table(name = "roombooking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roombooking.findAll", query = "SELECT r FROM Roombooking r")
    , @NamedQuery(name = "Roombooking.findByBillNo", query = "SELECT r FROM Roombooking r WHERE r.billNo.billNo = :billNo")
    , @NamedQuery(name = "Roombooking.findAllOrderByDESC", query = "SELECT r FROM Roombooking r ORDER BY r.requestNo DESC")
    , @NamedQuery(name = "Roombooking.findAllOrderByHosteller", query = "SELECT r FROM Roombooking r WHERE r.requestBy = :requestBy")
    , @NamedQuery(name = "Roombooking.findAllByStatus", query = "SELECT r FROM Roombooking r WHERE r.status = :status AND r.requestBy = :requestBy")
    , @NamedQuery(name = "Roombooking.findRequestDate", query = "SELECT distinct(r.requestDate) FROM Roombooking r ORDER BY r.requestDate ASC")
    , @NamedQuery(name = "Roombooking.findUpdateDate", query = "SELECT distinct(r.updateDate) FROM Roombooking r ORDER BY r.updateDate ASC")
    , @NamedQuery(name = "Roombooking.findByRequestNo", query = "SELECT r FROM Roombooking r WHERE r.requestNo = :requestNo")
    , @NamedQuery(name = "Roombooking.findByRequestType", query = "SELECT r FROM Roombooking r WHERE r.requestType = :requestType")
    , @NamedQuery(name = "Roombooking.findByRequestDate", query = "SELECT r FROM Roombooking r WHERE r.requestDate = :requestDate")
    , @NamedQuery(name = "Roombooking.findByUpdateDate", query = "SELECT r FROM Roombooking r WHERE r.updateDate = :updateDate")
    , @NamedQuery(name = "Roombooking.findByStatus", query = "SELECT r FROM Roombooking r WHERE r.status = :status")
    , @NamedQuery(name = "Roombooking.findByRemark", query = "SELECT r FROM Roombooking r WHERE r.remark = :remark")})
public class Roombooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "RequestNo")
    private String requestNo;
    @Size(max = 15)
    @Column(name = "RequestType")
    private String requestType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RequestDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;
    @Size(max = 100)
    @Column(name = "Remark")
    private String remark;
    @JoinColumn(name = "RequestRoom", referencedColumnName = "RoomNo")
    @ManyToOne
    private Room requestRoom;
    @JoinColumn(name = "RequestBy", referencedColumnName = "HostellerID")
    @ManyToOne
    private Hosteller requestBy;
    @JoinColumn(name = "BillNo", referencedColumnName = "BillNo")
    @ManyToOne
    private Bill billNo;

    public Roombooking() {
    }

    public Roombooking(String requestNo) {
        this.requestNo = requestNo;
    }

    public Roombooking(String requestNo, Date requestDate, Date updateDate) {
        this.requestNo = requestNo;
        this.requestDate = requestDate;
        this.updateDate = updateDate;
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

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public Room getRequestRoom() {
        return requestRoom;
    }

    public void setRequestRoom(Room requestRoom) {
        this.requestRoom = requestRoom;
    }

    public Hosteller getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Hosteller requestBy) {
        this.requestBy = requestBy;
    }

    public Bill getBillNo() {
        return billNo;
    }

    public void setBillNo(Bill billNo) {
        this.billNo = billNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestNo != null ? requestNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roombooking)) {
            return false;
        }
        Roombooking other = (Roombooking) object;
        if ((this.requestNo == null && other.requestNo != null) || (this.requestNo != null && !this.requestNo.equals(other.requestNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Roombooking[ requestNo=" + requestNo + " ]";
    }

}
