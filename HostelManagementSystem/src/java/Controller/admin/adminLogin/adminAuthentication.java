package Controller.admin.adminLogin;

import Controller.Crypto;
import Model.AccountManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class adminAuthentication extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountManager accountManager = new AccountManager(mgr);
        Crypto crypto = new Crypto();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        try {
            int level = accountManager.accountAuthenticate(crypto.UNEncode(username), crypto.PWEncode(password));
            if (level == 2 || level == 3) {
                if (!session.isNew()) {
                    session.invalidate();
                    session = request.getSession();
                }
                session.setAttribute("account", accountManager.findAccount(crypto.UNEncode(username), crypto.PWEncode(password)));
                session.setAttribute("mgr", mgr);
                if (level == 2) {
                    response.sendRedirect("/retrieveVisitorListing?t=0");
                } else if (level == 3) {
                    response.sendRedirect("/retrieveFloorplanListing");
                }
            } else {
                throw new Exception("Wrong username or password.");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/login/admin");
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
