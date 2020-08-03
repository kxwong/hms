
package Controller.hosteller.currentRoomManagement;

import Model.Hosteller;
import Model.HostellerManager;
import Model.Room;
import Model.RoomManager;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class retrieveCurrentRoomStatus extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            Hosteller hosteller = (Hosteller) session.getAttribute("curHosteller");
            RoomManager roomMgr = new RoomManager(em);
            Room curRoom = new Room();

            HostellerManager hostellerMgr = new HostellerManager(em);
            String id = hosteller.getHostellerID();
            hosteller = hostellerMgr.findHostellerByID(id);

            if (hosteller.getStayRoom() != null) {
                curRoom = hosteller.getStayRoom();
                List<Hosteller> hostellerList = hostellerMgr.findByRoom(curRoom.getRoomNo());
                session.setAttribute("hostellerList", hostellerList);
            }

            session.setAttribute("curHosteller", hosteller);
            response.sendRedirect("/hosteller/room/currentRoomStatus");

        } catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
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
