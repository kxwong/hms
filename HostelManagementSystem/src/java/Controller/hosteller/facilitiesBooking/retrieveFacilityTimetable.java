package Controller.hosteller.facilitiesBooking;

import Controller.Crypto;
import Model.Facility;
import Model.FacilityManager;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Hosteller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveFacilityTimetable extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        FacilityManager facilityMgr = new FacilityManager(em);
        Facility selectedFacility = new Facility();
        List<Facilitybooking> facilityBooking = new ArrayList();
        FacilitybookingManager facilitybookingMgr = new FacilitybookingManager(em);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null && request.getParameter("fid") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                String facilityID = encrypt.UNDecode(request.getParameter("fid"));
                String curDateStr = request.getParameter("curDate");
                Date curDate = simpleDateFormat.parse(curDateStr);
                selectedFacility = facilityMgr.findFacilityByID(facilityID);
                String facilityName = selectedFacility.getDescription().split("@")[0];
                List<Facility> facilityCourtList = new ArrayList();

                Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");
                String hostelLocation = curHosteller.getStayRoom().getFloorplanID().getHostelID().getLocation();
                facilityBooking = facilitybookingMgr.findBookingByFacility(selectedFacility);
                facilityCourtList = facilityMgr.findLikeDescription(facilityName, hostelLocation);

                session.setAttribute("facilityCourtList", facilityCourtList);
                session.setAttribute("facilityBookingList", facilityBooking);
                session.setAttribute("curDate", curDate);
                session.setAttribute("selectedFacility", selectedFacility);
                response.sendRedirect("/hosteller/facilities/facilitiesBooking");
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
