/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Room.findAll", query = "SELECT r FROM Room r"),
    @NamedQuery(name = "Room.findByLocation", query = "SELECT r FROM Room r WHERE r.floorplanID.hostelID.location = :location"),
    @NamedQuery(name = "Room.findByFloorplanStatus", query = "SELECT r FROM Room r WHERE r.floorplanID.floorplanID = :floorplanID AND r.status = :status ORDER BY r.roomNo ASC"),
    @NamedQuery(name = "Room.findByRoomNo", query = "SELECT r FROM Room r WHERE r.roomNo = :roomNo"),
    @NamedQuery(name = "Room.findByDescription", query = "SELECT r FROM Room r WHERE r.description = :description"),
    @NamedQuery(name = "Room.findByRentalFee", query = "SELECT r FROM Room r WHERE r.rentalFee = :rentalFee"),
    @NamedQuery(name = "Room.findByCapacity", query = "SELECT r FROM Room r WHERE r.capacity = :capacity"),
    @NamedQuery(name = "Room.findAllOrderDescWithFloorplan", query = "SELECT r FROM Room r WHERE r.floorplanID.floorplanID = :floorplanID ORDER BY r.roomNo desc"),
    @NamedQuery(name = "Room.findByStatus", query = "SELECT r FROM Room r WHERE r.status = :status")})
public class Room implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RentalFee")
    private double rentalFee;
    @Lob
    @Column(name = "Image")
    private byte[] image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Roommapping> roommappingList;
    @OneToMany(mappedBy = "visitRoom")
    private List<Visitor> visitorList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "RoomNo")
    private String roomNo;
    @Size(max = 40)
    @Column(name = "Description")
    private String description;
    @Column(name = "Capacity")
    private Short capacity;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;
    @OneToMany(mappedBy = "requestRoom")
    private List<Roombooking> roombookingList;
    @JoinColumn(name = "FloorplanID", referencedColumnName = "FloorplanID")
    @ManyToOne
    private Floorplan floorplanID;
    @OneToMany(mappedBy = "stayRoom")
    private List<Hosteller> hostellerList;

    public Room() {
    }

    public Room(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getCapacity() {
        return capacity;
    }

    public void setCapacity(Short capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<Roombooking> getRoombookingList() {
        return roombookingList;
    }

    public void setRoombookingList(List<Roombooking> roombookingList) {
        this.roombookingList = roombookingList;
    }

    public Floorplan getFloorplanID() {
        return floorplanID;
    }

    public void setFloorplanID(Floorplan floorplanID) {
        this.floorplanID = floorplanID;
    }

    @XmlTransient
    public List<Hosteller> getHostellerList() {
        return hostellerList;
    }

    public void setHostellerList(List<Hosteller> hostellerList) {
        this.hostellerList = hostellerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomNo != null ? roomNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomNo == null && other.roomNo != null) || (this.roomNo != null && !this.roomNo.equals(other.roomNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Room[ roomNo=" + roomNo + " ]";
    }

    @XmlTransient
    public List<Visitor> getVisitorList() {
        return visitorList;
    }

    public void setVisitorList(List<Visitor> visitorList) {
        this.visitorList = visitorList;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @XmlTransient
    public List<Roommapping> getRoommappingList() {
        return roommappingList;
    }

    public void setRoommappingList(List<Roommapping> roommappingList) {
        this.roommappingList = roommappingList;
    }
    
    public Roommapping First() {
        Roommapping roomMapping = new Roommapping();
        for (int i = 0; i < this.roommappingList.size(); i++) {
            if( this.roommappingList.get(i).IsFirst()){
                roomMapping =  this.roommappingList.get(i);
            }
        }
        return roomMapping;
    }
    
    public Roommapping Last() {
        Roommapping roomMapping = new Roommapping();
        for (int i = 0; i < this.roommappingList.size(); i++) {
            if( this.roommappingList.get(i).IsLast()){
                roomMapping =  this.roommappingList.get(i);
            }
        }
        return roomMapping;
    }
}
