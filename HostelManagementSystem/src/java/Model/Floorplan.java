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
@Table(name = "floorplan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Floorplan.findAll", query = "SELECT f FROM Floorplan f")
    , @NamedQuery(name = "Floorplan.findByHostelLocation", query = "SELECT f FROM Floorplan f WHERE f.hostelID.location = :location ORDER BY f.floor ASC")
    , @NamedQuery(name = "Floorplan.findByFloorplanID", query = "SELECT f FROM Floorplan f WHERE f.floorplanID = :floorplanID")
    , @NamedQuery(name = "Floorplan.findByPeopleCapacity", query = "SELECT f FROM Floorplan f WHERE f.peopleCapacity = :peopleCapacity")
    , @NamedQuery(name = "Floorplan.findByRoomCapacity", query = "SELECT f FROM Floorplan f WHERE f.roomCapacity = :roomCapacity")
    , @NamedQuery(name = "Floorplan.findByFloor", query = "SELECT f FROM Floorplan f WHERE f.floor = :floor")
    , @NamedQuery(name = "Floorplan.findHostelFloorDuplicate", query = "SELECT f FROM Floorplan f WHERE f.floor = :floor AND f.hostelID = :hostelID")
    , @NamedQuery(name = "Floorplan.findLocation", query = "SELECT distinct(f.hostelID.location) FROM Floorplan f ORDER BY f.hostelID.location ASC" )
    , @NamedQuery(name = "Floorplan.findAllByFloorAsc", query = "SELECT f FROM Floorplan f ORDER BY f.floor ASC" )
    , @NamedQuery(name = "Floorplan.findBuidling", query = "SELECT distinct(f.hostelID.building) FROM Floorplan f where f.hostelID.location = :location ORDER BY f.hostelID.building ASC" )
    , @NamedQuery(name = "Floorplan.findAllOrderDesc", query = "SELECT f FROM Floorplan f ORDER BY f.floorplanID DESC")})
public class Floorplan implements Serializable {

    @Column(name = "PeopleCapacity")
    private int peopleCapacity;
    @Column(name = "RoomCapacity")
    private int roomCapacity;
    @Column(name = "Floor")
    private int floor;
    @Lob
    @Column(name = "Image")
    private byte[] image;
    @Size(max = 20)
    @Column(name = "Status")
    private String status;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "FloorplanID")
    private String floorplanID;
    @JoinColumn(name = "HostelID", referencedColumnName = "HostelID")
    @ManyToOne
    private Hostel hostelID;
    @OneToMany(mappedBy = "floorplanID")
    private List<Room> roomList;

    public Floorplan() {
    }

    public Floorplan(String floorplanID) {
        this.floorplanID = floorplanID;
    }

    public String getFloorplanID() {
        return floorplanID;
    }

    public void setFloorplanID(String floorplanID) {
        this.floorplanID = floorplanID;
    }


    public Hostel getHostelID() {
        return hostelID;
    }

    public void setHostelID(Hostel hostelID) {
        this.hostelID = hostelID;
    }

    @XmlTransient
    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (floorplanID != null ? floorplanID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Floorplan)) {
            return false;
        }
        Floorplan other = (Floorplan) object;
        if ((this.floorplanID == null && other.floorplanID != null) || (this.floorplanID != null && !this.floorplanID.equals(other.floorplanID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Floorplan[ floorplanID=" + floorplanID + " ]";
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPeopleCapacity() {
        return peopleCapacity;
    }

    public void setPeopleCapacity(int peopleCapacity) {
        this.peopleCapacity = peopleCapacity;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
}
