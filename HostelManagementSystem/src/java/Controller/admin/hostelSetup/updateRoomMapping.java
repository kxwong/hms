package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.Room;
import Model.RoomManager;
import Model.Roommapping;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class updateRoomMapping extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String selectType = request.getParameter("selectType");
            String coordinate = request.getParameter("coordinate");
            if (selectType == null && selectType.equals("")) {
                throw new Exception("No room mapping type is found");
            } else if (coordinate == null || coordinate.equals("")) {
                throw new Exception("No highligted area is found");
            } else {
                if (IsPolCoorValid(coordinate)) {
                    String coordinateList[] = coordinate.split("_");
                    if (!(coordinateList.length >= 3 && coordinateList.length <= 9)) {
                        throw new Exception("Highligted area is not valid");
                    }
                } else {
                    throw new Exception("Highligted area is not valid");
                }
                Room currentRoom = (Room) session.getAttribute("room");
                Floorplan floorplan = (Floorplan) session.getAttribute("floorplan");
                RoomManager roomManager = new RoomManager(mgr);
                currentRoom = roomManager.findRoomByID(currentRoom.getRoomNo());
                currentRoom.setRoommappingList(new Roommapping().generateRoommappingList(coordinate, floorplan, currentRoom));
                utx.begin();
                roomManager.releaseRoomMapping(currentRoom);
                utx.commit();
                utx.begin();
                roomManager.modifyRoomMapping(new Roommapping().updateNode(currentRoom));
                utx.commit();
                currentRoom = roomManager.findRoomByID(currentRoom.getRoomNo());
                session.setAttribute("success", "Room is updated successfully");
                response.sendRedirect("/retrieveRoomDetail?p=" + crypto.REncode(currentRoom.getRoomNo()));
            }

        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/initializeEditRoomMapping?p=" + crypto.REncode(((Room) session.getAttribute("room")).getRoomNo()));
        }
    }

    protected boolean IsRecCoorValid(String coordinate) {
        boolean valid = true;
        try {
            int x = Integer.parseInt(coordinate.split(",")[0]);
            int y = Integer.parseInt(coordinate.split(",")[1]);
            int xt = Integer.parseInt(coordinate.split(",")[2]);
            int yt = Integer.parseInt(coordinate.split(",")[3]);
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
    }

    protected boolean IsPolCoorValid(String coordinate) {
        boolean valid = true;
        try {
            String coordinateList[] = coordinate.split("_");
            for (int i = 0; i < coordinateList.length; i++) {
                int x = Integer.parseInt(coordinateList[i].split(",")[0]);
                int y = Integer.parseInt(coordinateList[i].split(",")[1]);
            }
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
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
