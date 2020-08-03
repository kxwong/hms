/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "entcard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entcard.findAll", query = "SELECT e FROM Entcard e")
    , @NamedQuery(name = "Entcard.findByEntCardNo", query = "SELECT e FROM Entcard e WHERE e.entCardNo = :entCardNo")
    , @NamedQuery(name = "Entcard.findByIssueTime", query = "SELECT e FROM Entcard e WHERE e.issueTime = :issueTime")})
public class Entcard implements Serializable {

    @OneToMany(mappedBy = "entCardNo")
    private List<Visitor> visitorList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "EntCardNo")
    private String entCardNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IssueTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;
    @OneToMany(mappedBy = "entCardNo")
    private List<Entrecord> entrecordList;
    @OneToOne(mappedBy = "entCardNo")
    private Hosteller hosteller;
    @OneToOne(mappedBy = "entCardNo")
    private Visitor visitor;

    public Entcard() {
    }

    public Entcard(String entCardNo) {
        this.entCardNo = entCardNo;
    }

    public Entcard(String entCardNo, Date issueTime) {
        this.entCardNo = entCardNo;
        this.issueTime = issueTime;
    }

    public String getEntCardNo() {
        return entCardNo;
    }

    public void setEntCardNo(String entCardNo) {
        this.entCardNo = entCardNo;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    @XmlTransient
    public List<Entrecord> getEntrecordList() {
        return entrecordList;
    }

    public void setEntrecordList(List<Entrecord> entrecordList) {
        this.entrecordList = entrecordList;
    }

    @XmlTransient
    public Hosteller getHosteller() {
        return hosteller;
    }

    public void setHosteller(Hosteller hosteller) {
        this.hosteller = hosteller;
    }

    @XmlTransient
    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entCardNo != null ? entCardNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entcard)) {
            return false;
        }
        Entcard other = (Entcard) object;
        if ((this.entCardNo == null && other.entCardNo != null) || (this.entCardNo != null && !this.entCardNo.equals(other.entCardNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Entcard[ entCardNo=" + entCardNo + " ]";
    }

    @XmlTransient
    public List<Visitor> getVisitorList() {
        return visitorList;
    }

    public void setVisitorList(List<Visitor> visitorList) {
        this.visitorList = visitorList;
    }
    
}
