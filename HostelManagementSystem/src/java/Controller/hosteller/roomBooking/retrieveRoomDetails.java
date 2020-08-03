
package Controller.hosteller.roomBooking;

import Controller.Crypto;
import Model.Hosteller;
import Model.HostellerManager;
import Model.Room;
import Model.RoomManager;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveRoomDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        HostellerManager hostellerMgr = new HostellerManager(em);
        Crypto encrypt = new Crypto();

        try {

            String roomNo;
            RoomManager roomMgr = new RoomManager(em);

            if (request.getParameter("rid") == null) {
                throw new Exception("A room has not been selected, please try again.");
            }else {
                roomNo = encrypt.UNDecode(request.getParameter("rid"));

                if (roomMgr.findRoomListByLocation(roomNo) == null) {
                    throw new Exception("An error has encountered please try again.");
                } else {
                    Room roomSelected = roomMgr.findRoomByID(roomNo);
                    List<Hosteller> hostellerList = hostellerMgr.findByRoom(roomNo);
                    session.setAttribute("hostellerList", hostellerList);
                    session.setAttribute("roomSelected", roomSelected);
                    response.sendRedirect("/hosteller/room/roomDetailsViewing");
                }

            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/room/floorPlanListing");
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
