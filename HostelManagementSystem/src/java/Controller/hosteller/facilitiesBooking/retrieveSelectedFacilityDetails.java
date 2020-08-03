
package Controller.hosteller.facilitiesBooking;

import Controller.Crypto;
import Model.Facility;
import Model.FacilityManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveSelectedFacilityDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Facility selectedFacility = new Facility();
            FacilityManager facilityMgr = new FacilityManager(em);
            SimpleDateFormat formatSelectDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTime = new SimpleDateFormat("HHmm");
            Crypto encrypt = new Crypto();

            if (session.getAttribute("curHosteller") == null) {

                throw new Exception("An error has occured, please re-login again.");
            } else {
                try {
                    if (request.getParameter("sid") != null && request.getParameter("selectedDate") != null && request.getParameter("selectedTime") != null) {
                        String sid = encrypt.UNDecode(request.getParameter("sid"));
                        Date selectedDate = formatSelectDate.parse(request.getParameter("selectedDate"));
                        Date selectedTime = formatTime.parse(request.getParameter("selectedTime"));

                        selectedFacility = facilityMgr.findFacilityByID(sid);
                        session.setAttribute("selectedFacility", selectedFacility);
                        session.setAttribute("selectedDate", selectedDate);
                        session.setAttribute("selectedTime", selectedTime);
                        response.sendRedirect("/hosteller/facilities/selectedFacilityDetails");
                    } else {
                        throw new Exception("An error has occured, please select a facility before proceed.");
                    }
                } catch (Exception ex) {
                    response.sendRedirect("/retrieveAllFacilitiesWithinBranch");
                }
            }
        } catch (Exception ex) {
            response.sendRedirect("/retrieveAllFacilitiesWithinBranch");
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
