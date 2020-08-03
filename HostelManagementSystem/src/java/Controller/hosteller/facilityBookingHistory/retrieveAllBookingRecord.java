
package Controller.hosteller.facilityBookingHistory;

import Controller.Crypto;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Hosteller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveAllBookingRecord extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {

            List<Facilitybooking> facilityBookingList = new ArrayList();
            FacilitybookingManager facilityBookingMgr = new FacilitybookingManager(em);

            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");

                if (request.getParameter("ob") != null && request.getParameter("seq") != null) {

                    String orderBy = encrypt.UNDecode(request.getParameter("ob"));
                    String seq = encrypt.UNDecode(request.getParameter("seq"));
                    seq = seq.toUpperCase();

                    facilityBookingList = em.createQuery("SELECT f FROM Facilitybooking f WHERE f.bookBy = :bookBy " + "ORDER BY " + orderBy + " " + seq).setParameter("bookBy", curHosteller).getResultList();
                    session.setAttribute("seq", seq.toLowerCase());
                } else {
                    facilityBookingList = facilityBookingMgr.findAllByHosteller(curHosteller);
                }
                    session.setAttribute("facilityBookingList", facilityBookingList);
                    response.sendRedirect("/hosteller/facilities/facilityRecord");
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
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
