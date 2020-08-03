
package Controller.admin.bookingManagement;

import Controller.Crypto;
import Controller.Email;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateFacilityBooking extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String status = (String) request.getParameter("status");
            String remark = (String) request.getParameter("remark");
            FacilitybookingManager facilityBookingManager = new FacilitybookingManager(mgr);
            Facilitybooking facilityBooking;
            if (facilityBookingManager.findFacilitybookingByID(((Facilitybooking) session.getAttribute("facilityBooking")).getBookingID()) != null) {
                facilityBooking = facilityBookingManager.findFacilitybookingByID(((Facilitybooking) session.getAttribute("facilityBooking")).getBookingID());
                switch (status.toLowerCase()) {
                    case "approved":
                        facilityBooking.setStatus("Approved");
                        break;
                    case "rejected":
                        facilityBooking.setStatus("Rejected");
                        break;
                    default:
                        throw new Exception("Update fail. Status can not update to " + status);
                }
                facilityBooking.setRemark(remark);
                utx.begin();
                facilityBookingManager.updateFacilitybookingStatus(facilityBooking);
                utx.commit();
                Email email = new Email(facilityBooking.getBookBy().getLastName(), facilityBooking.getBookBy().getContact().getEmail(), "\n\nYour facility booking status is updated. Please log in to view the latest status.");
                session.setAttribute("success", "Facility booking is updated successfully");
                response.sendRedirect("/retrieveFacilityBookingDetails?p=" + crypto.FBEncode(facilityBooking.getBookingID()));
            } else {
                throw new Exception("Update fail. Unable to find facility booking details.");
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
