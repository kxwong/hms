
package Controller.admin.visitorManagement;

import Controller.Crypto;
import Model.VisitorManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveVisitorDetails extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String visitorID;
            try {
                String enVisitorID = request.getParameter("p");
                Crypto crypto = new Crypto();
                visitorID = crypto.VIDecode(enVisitorID);
            } catch (Exception ex) {
                throw new Exception("Selected visitor is not existed");
            }
            VisitorManager visitorManager = new VisitorManager(mgr);
            if (visitorManager.findVisitorByID(visitorID) != null) {
                session.setAttribute("visitor", visitorManager.findVisitorByID(visitorID));
                response.sendRedirect("/guard/visitor/visitorDetails");
            } else {
                throw new Exception("Selected visitor is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveVisitorListing?t=0");
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
