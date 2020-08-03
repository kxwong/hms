/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.Room;
import Model.RoomManager;
import java.io.IOException;
import java.util.Base64;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateRoom extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            RoomManager roomManager = new RoomManager(mgr);
            String desc = request.getParameter("desc");
            String status = request.getParameter("status");
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
                Room currentRoom = (Room) session.getAttribute("room");
                currentRoom = roomManager.findRoomByID(currentRoom.getRoomNo());
                Floorplan floorplan = currentRoom.getFloorplanID();
                int currentPeopleCapacity = 0;
                for (int x = 0; x < floorplan.getRoomList().size(); x++) {
                    if (floorplan.getRoomList().get(x).getStatus().toLowerCase().equals("available")) {
                        currentPeopleCapacity += floorplan.getRoomList().get(x).getCapacity();
                    }
                }
                if (floorplan.getPeopleCapacity() >= currentPeopleCapacity - currentRoom.getCapacity() + capacity) {
                    currentRoom.setDescription(desc);
                    currentRoom.setRentalFee(rental);
                    currentRoom.setImage(imageByte);
                    currentRoom.setCapacity((short) capacity);
                    currentRoom.setStatus(status);
                    utx.begin();
                    roomManager.updateRoom(currentRoom);
                    utx.commit();
                    session.removeAttribute("locationList");
                    session.removeAttribute("coordinate");
                    session.removeAttribute("selectType");
                    session.removeAttribute("floorplanList");
                    session.removeAttribute("floorplan");
                    session.removeAttribute("buildingList");
                    session.setAttribute("success", "Room is updated successfully");
                    response.sendRedirect("/retrieveRoomDetail?p=" + crypto.REncode(currentRoom.getRoomNo()));
                } else {
                    throw new Exception("Capacity of room has reached the people capacity of floorplan");
                }
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/initializeEditRoomDetails?p=" + crypto.REncode(((Room) session.getAttribute("room")).getRoomNo()));
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
