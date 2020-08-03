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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "facility")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facility.findAll", query = "SELECT f FROM Facility f")
    , @NamedQuery(name = "Facility.findDistinctFacility", query = "SELECT DISTINCT f.description, f.category, f.operatingDay FROM Facility f")
    , @NamedQuery(name = "Facility.findAllByHostelID", query = "SELECT f FROM Facility f WHERE f.hostelID = :hostelID")
    , @NamedQuery(name = "Facility.findAllOrderDesc", query = "SELECT f FROM Facility f ORDER BY f.facilityID DESC")
    , @NamedQuery(name = "Facility.findAllOrderDescriptionAsc", query = "SELECT f FROM Facility f ORDER BY f.description ASC")
    , @NamedQuery(name = "Facility.findLocation", query = "SELECT distinct(f.hostelID.location) FROM Facility f ORDER BY f.hostelID.location ASC")
    , @NamedQuery(name = "Facility.findBuidling", query = "SELECT distinct(f.hostelID.building) FROM Facility f where f.hostelID.location = :location ORDER BY f.hostelID.building ASC")
    , @NamedQuery(name = "Facility.findByFacilityID", query = "SELECT f FROM Facility f WHERE f.facilityID = :facilityID")
    , @NamedQuery(name = "Facility.findByDescription", query = "SELECT f FROM Facility f WHERE f.description = :description")
    , @NamedQuery(name = "Facility.findLikeDescription", query = "SELECT f FROM Facility f WHERE f.description LIKE :description")
    , @NamedQuery(name = "Facility.findByCategory", query = "SELECT f FROM Facility f WHERE f.category = :category")
    , @NamedQuery(name = "Facility.findByOperatingDay", query = "SELECT f FROM Facility f WHERE f.operatingDay = :operatingDay")
    , @NamedQuery(name = "Facility.findByStartHour", query = "SELECT f FROM Facility f WHERE f.startHour = :startHour")
    , @NamedQuery(name = "Facility.findByEndHour", query = "SELECT f FROM Facility f WHERE f.endHour = :endHour")
    , @NamedQuery(name = "Facility.findByCapacity", query = "SELECT f FROM Facility f WHERE f.capacity = :capacity")
    , @NamedQuery(name = "Facility.findByStatus", query = "SELECT f FROM Facility f WHERE f.status = :status")})
public class Facility implements Serializable {

    @Column(name = "Capacity")
    private int capacity;
    @Lob
    @Column(name = "Image")
    private byte[] image;
    @Size(max = 200)
    @Column(name = "Reminder")
    private String reminder;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "FacilityID")
    private String facilityID;
    @Size(max = 50)
    @Column(name = "Description")
    private String description;
    @Size(max = 20)
    @Column(name = "Category")
    private String category;
    @Size(max = 10)
    @Column(name = "OperatingDay")
    private String operatingDay;
    @Basic(optional = false)
    @NotNull
    @Column(name = "StartHour")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startHour;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EndHour")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endHour;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;
    @OneToMany(mappedBy = "bookFacility")
    private List<Facilitybooking> facilitybookingList;
    @JoinColumn(name = "HostelID", referencedColumnName = "HostelID")
    @ManyToOne
    private Hostel hostelID;

    public Facility() {
    }

    public Facility(String facilityID) {
        this.facilityID = facilityID;
    }

    public Facility(String facilityID, Date startHour, Date endHour) {
        this.facilityID = facilityID;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOperatingDay() {
        return operatingDay;
    }

    public void setOperatingDay(String operatingDay) {
        this.operatingDay = operatingDay;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Date getEndHour() {
        return endHour;
    }

    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<Facilitybooking> getFacilitybookingList() {
        return facilitybookingList;
    }

    public void setFacilitybookingList(List<Facilitybooking> facilitybookingList) {
        this.facilitybookingList = facilitybookingList;
    }

    public Hostel getHostelID() {
        return hostelID;
    }

    public void setHostelID(Hostel hostelID) {
        this.hostelID = hostelID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facilityID != null ? facilityID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facility)) {
            return false;
        }
        Facility other = (Facility) object;
        if ((this.facilityID == null && other.facilityID != null) || (this.facilityID != null && !this.facilityID.equals(other.facilityID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Facility[ facilityID=" + facilityID + " ]";
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

}
