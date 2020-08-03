
package Controller.hosteller.hostellerAccountRegistration;

import Controller.Crypto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import Model.Account;
import Model.AccountManager;
import Model.HostelManager;
import java.util.ArrayList;
import java.util.List;

public class hostellerAccountSetting extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            Crypto encrypt = new Crypto();
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            AccountManager accManager = new AccountManager(em);
            
            String encryptedUsername = encrypt.UNEncode(username);
            String encryptedPassword = encrypt.UNEncode(password);
            
            Account tempAccount = accManager.findAccountByUsername(encryptedUsername);
            List<String> locationList = new ArrayList();
            HostelManager hostelMgr = new HostelManager(em);
            

            locationList = hostelMgr.finaAllLocation();

            if (tempAccount != null) { //if existing account found    
                throw new Exception("Username already been used");
            } else {
                if (password.length() < 8 || password.length() > 16) {
                    throw new Exception("Password must between 8 - 16 digit");
                } else {
                    int word = 0;
                    int digit = 0;
                    for (int i = 0; i < password.length(); i++) {
                        Character un = password.charAt(i);
                        if (Character.isAlphabetic(un)) {
                            word++;
                        }
                        if (Character.isDigit(un)) {
                            digit++;
                        }
                    }
                    if (word == 0) {
                        throw new Exception("Password must contains at least 1 word");
                    }

                    if (digit == 0) {
                        throw new Exception("Password must contains at least 1 digit");
                    }

                }
                session.setAttribute("locationList", locationList);
                session.setAttribute("username", encryptedUsername);
                session.setAttribute("password", encryptedPassword);
                response.sendRedirect("/hosteller/account/accountRegistration");
            }

        } catch (Exception ex) {
            String errorStatus = "true";
            String errMsg = ex.getMessage();
            session.setAttribute("errorStatus", errorStatus);
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/account/accountCredential");
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
