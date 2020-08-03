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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "registrationreq")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registrationreq.findAll", query = "SELECT r FROM Registrationreq r")
    , @NamedQuery(name = "Registrationreq.findRequestDate", query = "SELECT distinct(r.requestDate) FROM Registrationreq r ORDER BY r.requestDate ASC" )
    , @NamedQuery(name = "Registrationreq.findUpdateDate", query = "SELECT distinct(r.updateDate) FROM Registrationreq r ORDER BY r.updateDate ASC" )
    , @NamedQuery(name = "Registrationreq.findByRequestNo", query = "SELECT r FROM Registrationreq r WHERE r.requestNo = :requestNo")
    , @NamedQuery(name = "Registrationreq.findByRequestDate", query = "SELECT r FROM Registrationreq r WHERE r.requestDate = :requestDate")
    , @NamedQuery(name = "Registrationreq.findByUpdateDate", query = "SELECT r FROM Registrationreq r WHERE r.updateDate = :updateDate")
    , @NamedQuery(name = "Registrationreq.findByStatus", query = "SELECT r FROM Registrationreq r WHERE r.status = :status")
    , @NamedQuery(name = "Registrationreq.findByRemark", query = "SELECT r FROM Registrationreq r WHERE r.remark = :remark")})
public class Registrationreq implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "RequestNo")
    private String requestNo;
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
    @Size(max = 10)
    @Column(name = "Status")
    private String status;
    @Size(max = 100)
    @Column(name = "Remark")
    private String remark;
    @OneToOne(mappedBy = "regReqNo")
    private Hosteller hosteller;

    public Registrationreq() {
    }

    public Registrationreq(String requestNo) {
        this.requestNo = requestNo;
    }

    public Registrationreq(String requestNo, Date requestDate, Date updateDate) {
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

    @XmlTransient
    public Hosteller getHosteller() {
        return hosteller;
    }

    public void setHosteller(Hosteller hosteller) {
        this.hosteller = hosteller;
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
        if (!(object instanceof Registrationreq)) {
            return false;
        }
        Registrationreq other = (Registrationreq) object;
        if ((this.requestNo == null && other.requestNo != null) || (this.requestNo != null && !this.requestNo.equals(other.requestNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Registrationreq[ requestNo=" + requestNo + " ]";
    }
    
}
