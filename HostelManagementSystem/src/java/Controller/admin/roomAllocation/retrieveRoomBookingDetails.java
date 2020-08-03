
package Controller.admin.roomAllocation;

import Controller.Crypto;
import Model.RoombookingManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveRoomBookingDetails extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String roomBookingNo;
            try {
                String enRoomBookingNo = request.getParameter("p");
                Crypto crypto = new Crypto();
                roomBookingNo = crypto.RBDecode(enRoomBookingNo);
            } catch (Exception ex) {
                throw new Exception("Selected room booking is not existed");
            }
            RoombookingManager roomBookingManager = new RoombookingManager(mgr);
            if (roomBookingManager.findRoombookingByID(roomBookingNo) != null) {
                session.setAttribute("roomBooking", roomBookingManager.findRoombookingByID(roomBookingNo));
                response.sendRedirect("/admin/hostel/viewRoomRequest");
            } else {
                throw new Exception("Selected room booking is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveRoomRequestListing");
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
