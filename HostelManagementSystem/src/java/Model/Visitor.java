/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "visitor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visitor.findAll", query = "SELECT v FROM Visitor v")
    , @NamedQuery(name = "Visitor.findAllOrderDesc", query = "SELECT v FROM Visitor v ORDER BY v.visitorID DESC")
    , @NamedQuery(name = "Visitor.findByVisitorID", query = "SELECT v FROM Visitor v WHERE v.visitorID = :visitorID")
    , @NamedQuery(name = "Visitor.findByName", query = "SELECT v FROM Visitor v WHERE v.name = :name")
    , @NamedQuery(name = "Visitor.findByIdentificationNo", query = "SELECT v FROM Visitor v WHERE v.identificationNo = :identificationNo")
    , @NamedQuery(name = "Visitor.findByMobilePhone", query = "SELECT v FROM Visitor v WHERE v.mobilePhone = :mobilePhone")
    , @NamedQuery(name = "Visitor.findByCard", query = "SELECT v FROM Visitor v WHERE v.entCardNo.entCardNo = :entCardNo")
    , @NamedQuery(name = "Visitor.findByEntryReason", query = "SELECT v FROM Visitor v WHERE v.entryReason = :entryReason")})
public class Visitor implements Serializable {

    @JoinColumn(name = "VisitRoom", referencedColumnName = "RoomNo")
    @ManyToOne
    private Room visitRoom;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "VisitorID")
    private String visitorID;
    @Size(max = 50)
    @Column(name = "Name")
    private String name;
    @Size(max = 20)
    @Column(name = "IdentificationNo")
    private String identificationNo;
    @Column(name = "MobilePhone")
    private BigInteger mobilePhone;
    @Size(max = 100)
    @Column(name = "EntryReason")
    private String entryReason;
    @JoinColumn(name = "EntCardNo", referencedColumnName = "EntCardNo")
    @ManyToOne
    private Entcard entCardNo;

    public Visitor() {
    }

    public Visitor(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public BigInteger getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(BigInteger mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEntryReason() {
        return entryReason;
    }

    public void setEntryReason(String entryReason) {
        this.entryReason = entryReason;
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
        hash += (visitorID != null ? visitorID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Visitor)) {
            return false;
        }
        Visitor other = (Visitor) object;
        if ((this.visitorID == null && other.visitorID != null) || (this.visitorID != null && !this.visitorID.equals(other.visitorID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Visitor[ visitorID=" + visitorID + " ]";
    }

    public Room getVisitRoom() {
        return visitRoom;
    }

    public void setVisitRoom(Room visitRoom) {
        this.visitRoom = visitRoom;
    }
    
}
