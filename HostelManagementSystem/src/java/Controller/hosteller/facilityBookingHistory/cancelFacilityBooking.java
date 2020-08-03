
package Controller.hosteller.facilityBookingHistory;

import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Hosteller;
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

public class cancelFacilityBooking extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            FacilitybookingManager facilitybookingMgr = new FacilitybookingManager(em);

            if (request.getParameter("fbid") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                String bfid = request.getParameter("fbid");
                Facilitybooking curFacilitybooking = facilitybookingMgr.findFacilitybookingByID(bfid);

                curFacilitybooking.setStatus("Cancelled");

                utx.begin();
                boolean facilityBookingSuccess = facilitybookingMgr.updateFacilitybooking(curFacilitybooking);
                utx.commit();

                if (facilityBookingSuccess == true) {
                    Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");
                    List<Facilitybooking> facilityBookingList = facilitybookingMgr.findAllByHosteller(curHosteller);
                    session.setAttribute("facilityBookingList", facilityBookingList);
                    session.setAttribute("successMsg", "Booking successfully cancel.");
                    response.sendRedirect("/hosteller/facilities/facilityRecord");
                } else {
                    throw new Exception("An error has occured, please try again.");
                }
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/facilities/facilityRecord");
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
