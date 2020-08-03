package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.FloorplanManager;
import Model.Hostel;
import Model.Room;
import Model.RoomManager;
import Model.Roommapping;
import Model.RoommappingPK;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class storeRoom extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            RoomManager roomManager = new RoomManager(mgr);
            FloorplanManager floorplanManager = new FloorplanManager(mgr);
            String desc = request.getParameter("desc");
            double rental;
            int capacity;
            byte[] imageByte;
            String extenstion = request.getParameter("imageByte").split("/")[1].split(";")[0];
            try {
                rental = Double.parseDouble(request.getParameter("rental"));
                capacity = Integer.parseInt(request.getParameter("capacity"));
                imageByte = Base64.getDecoder().decode(request.getParameter("imageByte").split(",")[1]);
            } catch (Exception ex) {
                throw new Exception("Wrong format for input field");
            }
            if (desc.equals("") || rental <= 0 || capacity <= 0 || imageByte.length <= 0) {
                throw new Exception("Please fill out required field");
            } else if (desc.length() > 40) {
                throw new Exception("Description field only allow 40 characters");
            } else if (rental < 200 || rental > 1000) {
                throw new Exception("Rental fee should between 200 to 1000");
            } else if (capacity <= 0) {
                throw new Exception("Capacity should at least 1");
            } else if (imageByte.length > 1070000) {
                throw new Exception("Room image should not exceed 1 mb");
            } else if (!checkImageFile(extenstion)) {
                throw new Exception("Room image with extension ." + extenstion + " is not allowed");
            } else {
                Floorplan floorplan = (Floorplan) session.getAttribute("floorplan");
                if (floorplanManager.findFloorplanByID(floorplan.getFloorplanID()) != null) {
                    int currentPeopleCapacity = 0;
                    for (int x = 0; x < floorplan.getRoomList().size(); x++) {
                        if (floorplan.getRoomList().get(x).getStatus().toLowerCase().equals("available")) {
                            currentPeopleCapacity += floorplan.getRoomList().get(x).getCapacity();
                        }
                    }
                    if (floorplan.getPeopleCapacity() >= currentPeopleCapacity + capacity) {
                        Room room = new Room();
                        room.setRoomNo(generateRoomNo(floorplan));
                        room.setDescription(desc);
                        room.setRentalFee(rental);
                        room.setImage(imageByte);
                        room.setRoommappingList(new Roommapping().generateRoommappingList((String) session.getAttribute("coordinate"), floorplan, room));
                        room.setCapacity((short) capacity);
                        room.setStatus("Available");
                        room.setFloorplanID(floorplan);
                        utx.begin();
                        roomManager.addRoom(room);
                        utx.commit();
                        utx.begin();
                        roomManager.updateRoomMapping(new Roommapping().updateNode(room));
                        utx.commit();
                        Crypto crypto = new Crypto();
                        session.removeAttribute("locationList");
                        session.removeAttribute("coordinate");
                        session.removeAttribute("selectType");
                        session.removeAttribute("floorplanList");
                        session.removeAttribute("floorplan");
                        session.removeAttribute("buildingList");
                        session.setAttribute("success", "New room is addedd successfully");
                        response.sendRedirect("/retrieveFloorplanDetails?p=" + crypto.FPEncode(floorplan.getFloorplanID()));
                    } else {
                        throw new Exception("Capacity of room has reached the people capacity of floorplan");
                    }
                } else {
                    throw new Exception("Room not allow to add on not existing floorplan");
                }
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/admin/hostel/addRoomStep2");
        }
    }

    protected boolean checkImageFile(String ext) {
        String imageExt[] = {"jpg", "jpeg", "bmp", "gif", "png"};
        boolean imageMatchExtension = false;
        for (int i = 0; i < imageExt.length; i++) {
            if (imageExt[i].equals(ext)) {
                imageMatchExtension = true;
            }
        }
        return imageMatchExtension;
    }

    protected String generateRoomNo(Floorplan floorplan) {
        Hostel hostel = floorplan.getHostelID();
        String locationName[] = hostel.getLocation().split(" ");
        String locationCode = "";
        if (locationName.length == 0) {
            locationCode = locationName[0].substring(0, 1);
        } else {
            locationCode = String.valueOf(locationName[0].charAt(0)) + String.valueOf(locationName[1].charAt(0));
        }
        String buildingName[] = hostel.getBuilding().split(" ");
        String buildingCode = "";
        if (buildingName.length == 0) {
            buildingCode = String.valueOf(buildingName[0].charAt(0));
        } else {
            buildingCode = String.valueOf(buildingName[1].charAt(0));
        }
        RoomManager roomManager = new RoomManager(mgr);
        String serialNumber = roomManager.getSerialNumber(floorplan);
        String floorplanSerialNumber = floorplan.getFloorplanID().substring(2);
        return floorplanSerialNumber + locationCode + buildingCode + String.valueOf(floorplan.getFloor()) + serialNumber;
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
