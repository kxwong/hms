/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kxwong
 */
@Embeddable
public class RoommappingPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CoordinateSequence")
    private short coordinateSequence;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "RoomNo")
    private String roomNo;

    public RoommappingPK() {
    }

    public RoommappingPK(short coordinateSequence, String roomNo) {
        this.coordinateSequence = coordinateSequence;
        this.roomNo = roomNo;
    }

    public short getCoordinateSequence() {
        return coordinateSequence;
    }

    public void setCoordinateSequence(short coordinateSequence) {
        this.coordinateSequence = coordinateSequence;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) coordinateSequence;
        hash += (roomNo != null ? roomNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoommappingPK)) {
            return false;
        }
        RoommappingPK other = (RoommappingPK) object;
        if (this.coordinateSequence != other.coordinateSequence) {
            return false;
        }
        if ((this.roomNo == null && other.roomNo != null) || (this.roomNo != null && !this.roomNo.equals(other.roomNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.RoommappingPK[ coordinateSequence=" + coordinateSequence + ", roomNo=" + roomNo + " ]";
    }
    
}
