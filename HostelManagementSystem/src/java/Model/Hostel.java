/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "hostel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hostel.findAll", query = "SELECT h FROM Hostel h")
    , @NamedQuery(name = "Hostel.findAllLocation", query = "SELECT DISTINCT h.location FROM Hostel h")
    , @NamedQuery(name = "Hostel.findByHostelID", query = "SELECT h FROM Hostel h WHERE h.hostelID = :hostelID")
    , @NamedQuery(name = "Hostel.findByBuilding", query = "SELECT h FROM Hostel h WHERE h.building = :building")
    , @NamedQuery(name = "Hostel.findByLocation", query = "SELECT h FROM Hostel h WHERE h.location = :location")
    , @NamedQuery(name = "Hostel.findHostelDuplicate", query = "SELECT h FROM Hostel h WHERE h.building = :building AND h.location = :location")
    , @NamedQuery(name = "Hostel.findAllOrderDesc", query = "SELECT h FROM Hostel h ORDER BY h.hostelID DESC")})
public class Hostel implements Serializable {

    @OneToMany(mappedBy = "hostel")
    private List<Entrecord> entrecordList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "HostelID")
    private String hostelID;
    @Size(max = 15)
    @Column(name = "Building")
    private String building;
    @Size(max = 30)
    @Column(name = "Location")
    private String location;
    @OneToMany(mappedBy = "hostelID")
    private List<Floorplan> floorplanList;
    @OneToMany(mappedBy = "hostelID")
    private List<Facility> facilityList;

    public Hostel() {
    }

    public Hostel(String hostelID) {
        this.hostelID = hostelID;
    }

    public String getHostelID() {
        return hostelID;
    }

    public void setHostelID(String hostelID) {
        this.hostelID = hostelID;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlTransient
    public List<Floorplan> getFloorplanList() {
        return floorplanList;
    }

    public void setFloorplanList(List<Floorplan> floorplanList) {
        this.floorplanList = floorplanList;
    }

    @XmlTransient
    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hostelID != null ? hostelID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hostel)) {
            return false;
        }
        Hostel other = (Hostel) object;
        if ((this.hostelID == null && other.hostelID != null) || (this.hostelID != null && !this.hostelID.equals(other.hostelID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Hostel[ hostelID=" + hostelID + " ]";
    }

    @XmlTransient
    public List<Entrecord> getEntrecordList() {
        return entrecordList;
    }

    public void setEntrecordList(List<Entrecord> entrecordList) {
        this.entrecordList = entrecordList;
    }
    
}
