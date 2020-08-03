
package Controller.hosteller.facilityBookingHistory;

import Controller.Crypto;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveSelectedBookingRecord extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        try {

            Facilitybooking selectedFaciBooking = new Facilitybooking();
            FacilitybookingManager facilitybookingMgr = new FacilitybookingManager(em);
            Crypto encrypt = new Crypto();

            if (request.getParameter("bid") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                String bid = encrypt.UNDecode(request.getParameter("bid"));
                selectedFaciBooking = facilitybookingMgr.findFacilitybookingByID(bid);
                session.setAttribute("selectedFaciBooking", selectedFaciBooking);    
                response.sendRedirect("/hosteller/facilities/selectedFacilityRecord");
            }

        } catch (Exception ex) {
            response.sendRedirect("/retrieveAllBookingRecord");
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
