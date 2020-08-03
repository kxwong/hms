
package Controller.admin.bookingManagement;

import Controller.Crypto;
import Model.FacilitybookingManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveFacilityBookingDetails extends HttpServlet {
  
    @PersistenceContext
    EntityManager mgr;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String bookingID;
            try {
                String enBookingID = request.getParameter("p");
                Crypto crypto = new Crypto();
                bookingID = crypto.FBDecode(enBookingID);
            } catch (Exception ex) {
                throw new Exception("Selected facility booking is not existed");
            }
            FacilitybookingManager facilityBookingManager = new FacilitybookingManager(mgr);
            if (facilityBookingManager.findFacilitybookingByID(bookingID) != null) {
                session.setAttribute("facilityBooking", facilityBookingManager.findFacilitybookingByID(bookingID));
                response.sendRedirect("/admin/facility/viewFacilityBooking");
            } else {
                throw new Exception("Selected facility booking is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveBookingListing?t=0");
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
