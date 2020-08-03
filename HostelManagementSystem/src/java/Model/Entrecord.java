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
@Table(name = "entrecord")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entrecord.findAll", query = "SELECT e FROM Entrecord e")
    , @NamedQuery(name = "Entrecord.findAllByDateRange", query = "SELECT e FROM Entrecord e WHERE e.accessTime BETWEEN :startDate AND :enddate ORDER BY e.accessTime ASC")
    , @NamedQuery(name = "Entrecord.findDate", query = "SELECT distinct(e.accessTime) FROM Entrecord e ORDER BY e.accessTime ASC" )
    , @NamedQuery(name = "Entrecord.findByEntRecordID", query = "SELECT e FROM Entrecord e WHERE e.entRecordID = :entRecordID")
    , @NamedQuery(name = "Entrecord.findByAccessTime", query = "SELECT e FROM Entrecord e WHERE e.accessTime = :accessTime")
    , @NamedQuery(name = "Entrecord.findByEntCard", query = "SELECT e FROM Entrecord e WHERE e.entCardNo.entCardNo = :entCardNo")
    , @NamedQuery(name = "Entrecord.findByGate", query = "SELECT e FROM Entrecord e WHERE e.gate = :gate")})
public class Entrecord implements Serializable {

    @JoinColumn(name = "Hostel", referencedColumnName = "HostelID")
    @ManyToOne
    private Hostel hostel;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "EntRecordID")
    private String entRecordID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AccessTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessTime;
    @Size(max = 10)
    @Column(name = "Gate")
    private String gate;
    @JoinColumn(name = "EntCardNo", referencedColumnName = "EntCardNo")
    @ManyToOne
    private Entcard entCardNo;

    public Entrecord() {
    }

    public Entrecord(String entRecordID) {
        this.entRecordID = entRecordID;
    }

    public Entrecord(String entRecordID, Date accessTime) {
        this.entRecordID = entRecordID;
        this.accessTime = accessTime;
    }

    public String getEntRecordID() {
        return entRecordID;
    }

    public void setEntRecordID(String entRecordID) {
        this.entRecordID = entRecordID;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public Entcard getEntCardNo() {
        return entCardNo;
    }

    public void setEntCardNo(Entcard entCardNo) {
        this.entCardNo = entCardNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entRecordID != null ? entRecordID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entrecord)) {
            return false;
        }
        Entrecord other = (Entrecord) object;
        if ((this.entRecordID == null && other.entRecordID != null) || (this.entRecordID != null && !this.entRecordID.equals(other.entRecordID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Entrecord[ entRecordID=" + entRecordID + " ]";
    }

    public Hostel getHostel() {
        return hostel;
    }

    public void setHostel(Hostel hostel) {
        this.hostel = hostel;
    }
    
}
