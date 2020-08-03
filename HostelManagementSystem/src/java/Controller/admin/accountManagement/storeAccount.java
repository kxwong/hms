package Controller.admin.accountManagement;

import Controller.Crypto;
import Model.Account;
import Model.AccountManager;
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

public class storeAccount extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String level = request.getParameter("level");
            String username = request.getParameter("username");
            String pw = request.getParameter("pw");
            String cpw = request.getParameter("cpw");
            if (level.equals("") || username.equals("") || pw.equals("") || cpw.equals("")) {
                throw new Exception("Please fill in all input field");
            } else if (username.length() < 6) {
                throw new Exception("Username value require at least 6 character");
            } else if (pw.length() < 8) {
                throw new Exception("Password value require at least 8 character");
            } else if (!pw.equals(cpw)) {
                throw new Exception("Password and confirm password must be same");
            } else {
                int autlevel = 3;
                if (level.toLowerCase().equals("guard")) {
                    autlevel = 2;
                } else if (level.toLowerCase().equals("admin")) {
                    autlevel = 3;
                } else {
                    throw new Exception("Error value for position field");
                }
                Crypto crypto = new Crypto();
                AccountManager accountManager = new AccountManager(mgr);
                if (accountManager.findAccountByID(crypto.UNEncode(username)) == null) {
                    Account account = new Account();
                    account.setUsername(crypto.UNEncode(username));
                    account.setPassword(crypto.PWEncode(pw));
                    account.setLevel(autlevel);
                    utx.begin();
                    accountManager.addAccount(account);
                    utx.commit();
                    session.setAttribute("success", level+" account is authorized successfully.");
                } else {
                    throw new Exception("Duplicated username detected");
                }

            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
        }
        response.sendRedirect("/admin/account/authorizeAccount");
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
