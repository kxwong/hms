
package Controller.hosteller.roomBooking;

import Controller.Crypto;
import Model.Floorplan;
import Model.FloorplanManager;
import Model.Room;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveFloorPlanDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {
            FloorplanManager floorplanMgr = new FloorplanManager(em);
            Floorplan curFloorplan = new Floorplan();
            List<Room> roomList = new ArrayList<Room>();
            String floorplanID = encrypt.UNDecode(request.getParameter("fid"));
            try {
                curFloorplan = floorplanMgr.findFloorplanByID(floorplanID);
                roomList = curFloorplan.getRoomList();
            } catch (Exception ex) {
                String errMsg = ex.getMessage();
                session.setAttribute("bookSuccess", "false");
                session.setAttribute("message", errMsg);
                response.sendRedirect("/retrieveCurrentRoomStatus");
            }
            session.setAttribute("floorplan", curFloorplan);
            session.setAttribute("roomList", roomList);
            response.sendRedirect("/hosteller/room/roomSelection");

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("bookSuccess", "false");
            session.setAttribute("message", errMsg);
            response.sendRedirect("/retrieveCurrentRoomStatus");
        }
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
