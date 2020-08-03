package Model;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.*;

public class RoomManager {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;

    public RoomManager(EntityManager mgr) {
        this.mgr = mgr;
    }

    public boolean addRoom(Room room) {
        mgr.persist(room);
        Floorplan floorplan = mgr.find(Floorplan.class, room.getFloorplanID().getFloorplanID());
        List<Room> floorplanRoomList = floorplan.getRoomList();
        floorplanRoomList.add(room);
        floorplan.setRoomList(floorplanRoomList);
        return true;
    }

    public Room findRoomByID(String id) {
        Room room = mgr.find(Room.class, id);
        return room;
    }

    public List<Room> findRoomListByLocation(String location) {
        List<Room> roomList = mgr.createNamedQuery("Room.findByLocation").setParameter("location", location).getResultList();

        if (roomList.size() >= 1) {
            return roomList;
        }
        return roomList;
    }

    public boolean deleteRoom(String id) {
        Room room = findRoomByID(id);
        if (room != null) {
            mgr.remove(room);
            return true;
        }
        return false;
    }

    public boolean updateRoom(Room room) throws Exception {
        Room tempoRoom = findRoomByID(room.getRoomNo());
        if (tempoRoom != null) {
            tempoRoom.setDescription(room.getDescription());
            tempoRoom.setRentalFee(room.getRentalFee());
            tempoRoom.setImage(room.getImage());
            tempoRoom.setCapacity(room.getCapacity());
            tempoRoom.setStatus(room.getStatus());
            return true;
        }
        return false;
    }

    public Roommapping findRoomMappingByPK(RoommappingPK id) {
        Roommapping roomMapping = (Roommapping) mgr.createNamedQuery("Roommapping.findByPK")
                .setParameter("coordinateSequence", id.getCoordinateSequence())
                .setParameter("roomNo", id.getRoomNo())
                .getSingleResult();
        return roomMapping;
    }

    public boolean deleteRoomMapping(Roommapping tempoRoomMapping) {
        boolean result = false;
        Roommapping roomMapping = findRoomMappingByPK(tempoRoomMapping.getRoommappingPK());
        if (roomMapping != null) {
            mgr.remove(roomMapping);
            result = true;
        }
        return result;
    }

    public boolean releaseRoomMapping(Room room) throws Exception {
        Room tempoRoom = findRoomByID(room.getRoomNo());
        releaseNode(tempoRoom);
        for (Roommapping tempoRoomMapping : tempoRoom.getRoommappingList()) {
            deleteRoomMapping(tempoRoomMapping);
        }
        return true;
    }

    public boolean modifyRoomMapping(Room room) throws Exception {
        for (Roommapping roomMapping : room.getRoommappingList()) {
            mgr.persist(roomMapping);
        }
        Room tempoRoom = findRoomByID(room.getRoomNo());
        tempoRoom.setRoommappingList(room.getRoommappingList());
        return true;
    }

    public boolean releaseNode(Room room) throws Exception {
        Room tempoRoom = findRoomByID(room.getRoomNo());
        if (tempoRoom != null) {
            for (int i = 0; i < tempoRoom.getRoommappingList().size(); i++) {
                Roommapping tempoRoomMapping = tempoRoom.getRoommappingList().get(i);
                Roommapping roomMapping = findRoomMappingByPK(tempoRoomMapping.getRoommappingPK());
                roomMapping.Next(null);
                roomMapping.Previous(null);
            }
            return true;
        }
        return false;
    }

    public boolean updateRoomMapping(Room room) throws Exception {
        Room tempoRoom = findRoomByID(room.getRoomNo());
        if (tempoRoom != null) {
            for (int i = 0; i < room.getRoommappingList().size(); i++) {
                Roommapping tempoRoomMapping = room.getRoommappingList().get(i);
                Roommapping roomMapping = findRoomMappingByPK(tempoRoomMapping.getRoommappingPK());
                roomMapping.Next(room.getRoommappingList().get(i).Next());
                roomMapping.Previous(room.getRoommappingList().get(i).Previous());
            }
            return true;
        }
        return false;
    }

    public List<Room> findAll() {
        List<Room> list = mgr.createNamedQuery("Room.findAll").getResultList();
        return list;
    }

    public List<Room> findByFloorplanStatus(String floorplanID, String status) {
        List<Room> list = mgr.createNamedQuery("Room.findByFloorplanStatus").setParameter("floorplanID", floorplanID).setParameter("status", status).getResultList();
        return list;
    }

    public List<Room> findAllByFloorplan(Floorplan floorplan) {
        List<Room> list = mgr.createNamedQuery("Room.findAllOrderDescWithFloorplan").setParameter("floorplanID", floorplan.getFloorplanID()).getResultList();
        return list;
    }

    public String getSerialNumber(Floorplan floorplan) {
        List<Room> list = mgr.createNamedQuery("Room.findAllOrderDescWithFloorplan").setParameter("floorplanID", floorplan.getFloorplanID()).getResultList();
        int serialNumber = 1;
        try {
            String previousID = list.get(0).getRoomNo();
            serialNumber = Integer.parseInt(previousID.substring(previousID.length() - 1, previousID.length())) + 1;
        } catch (Exception ex) {
        }
        String newSerial = String.valueOf(serialNumber);
        if (serialNumber < 10) {
            newSerial = "0" + String.valueOf(serialNumber);
        }
        return newSerial;
    }
}
