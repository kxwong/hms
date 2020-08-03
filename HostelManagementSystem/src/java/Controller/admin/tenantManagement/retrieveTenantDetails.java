
package Controller.admin.tenantManagement;

import Controller.Crypto;
import Model.HostellerManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveTenantDetails extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String hostellerID;
            try {
                String enHostellerID = request.getParameter("p");
                Crypto crypto = new Crypto();
                hostellerID = crypto.HTDecode(enHostellerID);
            } catch (Exception ex) {
                throw new Exception("Selected hosteller is not existed");
            }
            HostellerManager hostellerManager = new HostellerManager(mgr);
            if (hostellerManager.findHostellerByID(hostellerID) != null) {
                session.setAttribute("hosteller", hostellerManager.findHostellerByID(hostellerID));
                response.sendRedirect("/admin/hosteller/viewTenant");
            } else {
                throw new Exception("Selected hosteller is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveTenantListing?t=0");
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
