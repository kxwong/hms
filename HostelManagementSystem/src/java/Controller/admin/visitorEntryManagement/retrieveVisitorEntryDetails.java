package Controller.admin.visitorEntryManagement;

import Controller.Crypto;
import Model.EntrecordManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveVisitorEntryDetails extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String entID;
            try {
                String enEntID = request.getParameter("p");
                Crypto crypto = new Crypto();
                entID = crypto.ENDecode(enEntID);
            } catch (Exception ex) {
                throw new Exception("Selected visitor entrance record is not existed");
            }
            EntrecordManager entrecordManager = new EntrecordManager(mgr);
            if (entrecordManager.findEntrecordByID(entID) != null) {
                session.setAttribute("visitorEntry", entrecordManager.findEntrecordByID(entID));
                response.sendRedirect("/admin/entry/viewVisitorEntry");
            } else {
                throw new Exception("Selected visitor entrance record is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveVisitorEntryListing?t=0");
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
