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

public class updateAccountPassword extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String opw = request.getParameter("opw");
            String pw = request.getParameter("pw");
            String cpw = request.getParameter("cpw");
            if (opw.equals("") || pw.equals("") || cpw.equals("")) {
                throw new Exception("Please fill in all input field");
            } else if (pw.length() < 8) {
                throw new Exception("Password value require at least 8 character");
            } else if (opw.equals(pw)) {
                throw new Exception("Old password and password cannot be same");
            } else if (!pw.equals(cpw)) {
                throw new Exception("Password and confirm password must be same");
            } else {
                Crypto crypto = new Crypto();
                AccountManager accountManager = new AccountManager(mgr);
                Account account = (Account) session.getAttribute("account");
                if (accountManager.findAccountByID(account.getUsername()) != null) {
                    if (account.getPassword().equals(crypto.PWEncode(opw))) {
                        account.setPassword(crypto.PWEncode(pw));
                        utx.begin();
                        accountManager.updateAccountPassword(account);
                        utx.commit();
                        session.setAttribute("success", "Passowrd is changed successfully.");
                    } else {
                        throw new Exception("Old password validated fail");
                    }
                } else {
                    throw new Exception("Error on getting user details");
                }

            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
        }
        response.sendRedirect("/admin/account/changePassword");
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
