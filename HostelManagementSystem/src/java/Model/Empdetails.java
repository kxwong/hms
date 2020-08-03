/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "empdetails")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empdetails.findAll", query = "SELECT e FROM Empdetails e")
    , @NamedQuery(name = "Empdetails.findByHostellerID", query = "SELECT e FROM Empdetails e WHERE e.hostellerID = :hostellerID")
    , @NamedQuery(name = "Empdetails.findByBranch", query = "SELECT e FROM Empdetails e WHERE e.branch = :branch")
    , @NamedQuery(name = "Empdetails.findByDepartment", query = "SELECT e FROM Empdetails e WHERE e.department = :department")
    , @NamedQuery(name = "Empdetails.findByWorkerID", query = "SELECT e FROM Empdetails e WHERE e.workerID = :workerID")})
public class Empdetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "HostellerID")
    private String hostellerID;
    @Size(max = 30)
    @Column(name = "Branch")
    private String branch;
    @Size(max = 40)
    @Column(name = "Department")
    private String department;
    @Size(max = 9)
    @Column(name = "WorkerID")
    private String workerID;
    @JoinColumn(name = "HostellerID", referencedColumnName = "HostellerID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Hosteller hosteller;

    public Empdetails() {
    }

    public Empdetails(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public String getHostellerID() {
        return hostellerID;
    }

    public void setHostellerID(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public Hosteller getHosteller() {
        return hosteller;
    }

    public void setHosteller(Hosteller hosteller) {
        this.hosteller = hosteller;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hostellerID != null ? hostellerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empdetails)) {
            return false;
        }
        Empdetails other = (Empdetails) object;
        if ((this.hostellerID == null && other.hostellerID != null) || (this.hostellerID != null && !this.hostellerID.equals(other.hostellerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Empdetails[ hostellerID=" + hostellerID + " ]";
    }
    
}
