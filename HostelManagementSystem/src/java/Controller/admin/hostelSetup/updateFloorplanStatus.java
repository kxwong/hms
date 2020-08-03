
package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.FloorplanManager;
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

public class updateFloorplanStatus extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            FloorplanManager floorplanManager = new FloorplanManager(mgr);
            Floorplan floorplan = floorplanManager.findFloorplanByID(((Floorplan) session.getAttribute("floorplan")).getFloorplanID());
            if (floorplan.getStatus().toLowerCase().equals("active")) {
                utx.begin();
                floorplanManager.updateFloorplanStatusInactive(floorplan);
                utx.commit();
            } else if (floorplan.getStatus().toLowerCase().equals("inactive")) {
                utx.begin();
                floorplanManager.updateFloorplanStatusActive(floorplan);
                utx.commit();
            } else {
                throw new Exception("Fail to update floorplan status");
            }
            session.setAttribute("success", "Floorplan is updated successfully");
            response.sendRedirect("/retrieveFloorplanDetails?p=" + crypto.FPEncode(floorplan.getFloorplanID()));
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/admin/hostel/initializeEditFloorplan?p=" + crypto.FPEncode(((Floorplan) session.getAttribute("floorplan")).getFloorplanID()));
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
