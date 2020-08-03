/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "roommapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roommapping.findAll", query = "SELECT r FROM Roommapping r"),
    @NamedQuery(name = "Roommapping.findByPK", query = "SELECT r FROM Roommapping r WHERE r.roommappingPK.coordinateSequence = :coordinateSequence AND  r.roommappingPK.roomNo = :roomNo"),
    @NamedQuery(name = "Roommapping.findByCoordinateSequence", query = "SELECT r FROM Roommapping r WHERE r.roommappingPK.coordinateSequence = :coordinateSequence"),
    @NamedQuery(name = "Roommapping.findByRoomNo", query = "SELECT r FROM Roommapping r WHERE r.roommappingPK.roomNo = :roomNo"),
    @NamedQuery(name = "Roommapping.findByCoordinateX", query = "SELECT r FROM Roommapping r WHERE r.coordinateX = :coordinateX"),
    @NamedQuery(name = "Roommapping.findByCoordinateY", query = "SELECT r FROM Roommapping r WHERE r.coordinateY = :coordinateY"),
    @NamedQuery(name = "Roommapping.findByIsFirst", query = "SELECT r FROM Roommapping r WHERE r.isFirst = :isFirst"),
    @NamedQuery(name = "Roommapping.findByIsLast", query = "SELECT r FROM Roommapping r WHERE r.isLast = :isLast")})
public class Roommapping implements Serializable {

    @JoinColumn(name = "RoomNo", referencedColumnName = "RoomNo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Room room;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RoommappingPK roommappingPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CoordinateX")
    private BigDecimal coordinateX;
    @Column(name = "CoordinateY")
    private BigDecimal coordinateY;
    @Column(name = "IsFirst")
    private Boolean isFirst;
    @Column(name = "IsLast")
    private Boolean isLast;
    @JoinColumns({
        @JoinColumn(name = "NextRoomNo", referencedColumnName = "RoomNo"),
        @JoinColumn(name = "NextCoordinateSequence", referencedColumnName = "CoordinateSequence")})
    @OneToOne
    private Roommapping Next;
    @JoinColumns({
        @JoinColumn(name = "PreviousRoomNo", referencedColumnName = "RoomNo"),
        @JoinColumn(name = "PreviousCoordinateSequence", referencedColumnName = "CoordinateSequence")})
    @OneToOne
    private Roommapping Previous;

    public Roommapping() {
    }

    public Roommapping(RoommappingPK roommappingPK) {
        this.roommappingPK = roommappingPK;
    }

    public Roommapping(short coordinateSequence, String roomNo) {
        this.roommappingPK = new RoommappingPK(coordinateSequence, roomNo);
    }

    public RoommappingPK getRoommappingPK() {
        return roommappingPK;
    }

    public void setRoommappingPK(RoommappingPK roommappingPK) {
        this.roommappingPK = roommappingPK;
    }

    public double getCoordinateX() {
        return coordinateX.doubleValue();
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = BigDecimal.valueOf(coordinateX);
    }

    public double getCoordinateY() {
        return coordinateY.doubleValue();
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = BigDecimal.valueOf(coordinateY);
    }

    public Boolean IsFirst() {
        return isFirst;
    }

    public void IsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean IsLast() {
        return isLast;
    }

    public void IsLast(Boolean isLast) {
        this.isLast = isLast;
    }

    public Roommapping Next() {
        return Next;
    }

    public void Next(Roommapping roommapping) {
        this.Next = roommapping;
    }

    public Roommapping Previous() {
        return Previous;
    }

    public void Previous(Roommapping roommapping) {
        this.Previous = roommapping;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roommappingPK != null ? roommappingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Roommapping)) {
            return false;
        }
        Roommapping other = (Roommapping) object;
        if ((this.roommappingPK == null && other.roommappingPK != null) || (this.roommappingPK != null && !this.roommappingPK.equals(other.roommappingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Roommapping[ roommappingPK=" + roommappingPK + " ]";
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room updateNode(Room room) {
        List<Roommapping> roomMappingList = room.getRoommappingList();
        for (int i = 0; i < roomMappingList.size(); i++) {
            int nextIndex = i + 1;
            if (nextIndex == roomMappingList.size()) {
                nextIndex = 0;
            }
            int previousIndex = i - 1;
            if (previousIndex < 0) {
                previousIndex = roomMappingList.size() - 1;
            }
            roomMappingList.get(i).Next(roomMappingList.get(nextIndex));
            roomMappingList.get(i).Previous(roomMappingList.get(previousIndex));
        }
        room.setRoommappingList(roomMappingList);
        return room;
    }

    public List<Roommapping> generateRoommappingList(String coordinate, Floorplan floorplan, Room room) throws IOException {
        InputStream in = new ByteArrayInputStream(floorplan.getImage());
        BufferedImage buf = ImageIO.read(in);
        double transformFact = (double) buf.getWidth() / (double) 1040;
        List<Roommapping> roomMappingList = new ArrayList<>();
        double maxX = (double) buf.getWidth();
        double maxY = (double) buf.getHeight();
        String[] coordList = coordinate.split("_");
        for (int i = 0; i < coordList.length; i++) {
            double coordX = (double) Math.round(Integer.parseInt(coordList[i].split(",")[0]) * transformFact);
            double coordY = (double) Math.round(Integer.parseInt(coordList[i].split(",")[1]) * transformFact);
            double coordXFact = deviateFact(coordX / maxX);
            double coordYFact = deviateFact(coordY / maxY);
            Roommapping roomMapping = new Roommapping();
            roomMapping.setRoommappingPK(new RoommappingPK((short) (i + 1), room.getRoomNo()));
            roomMapping.setRoom(room);
            roomMapping.setCoordinateX(coordXFact);
            roomMapping.setCoordinateY(coordYFact);
            roomMapping.IsFirst(false);
            roomMapping.IsLast(false);
            if (i + 1 == 1) {
                roomMapping.IsFirst(true);
            } else if (i + 1 == coordList.length) {
                roomMapping.IsLast(true);
            }
            roomMappingList.add(roomMapping);
        }
        return roomMappingList;
    }

    private double deviateFact(double coord) {
        if (coord < 0 || coord > 1) {
            if (coord < 0) {
                coord = 0.0;
            } else {
                coord = 1.0;
            }
        }
        return coord;
    }

}
