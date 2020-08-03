
package Controller.hosteller.accountSettingManagement;

import Controller.Crypto;
import Model.Account;
import Model.AccountManager;
import Model.Hosteller;
import Model.HostellerManager;
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


public class accountSettingUpdate extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        String updateStatus = "false";
        String message = "false";
        Hosteller currentHosteller = new Hosteller();
        HostellerManager hostellerMgr = new HostellerManager(em);

        try {
            String username = crypto.UNEncode(request.getParameter("username"));
            String curPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String passwordConfirmation = request.getParameter("newPasswordConfirmation");           
            
            AccountManager accountMgr = new AccountManager(em);
            Account currentAccount = accountMgr.findAccountByUsername(username);

            if (!curPassword.equals(crypto.UNDecode(currentAccount.getPassword()))) {
                throw new Exception("Password are not correct");
            } else {
                if (!(newPassword.equals(passwordConfirmation))) {
                    throw new Exception("Password and confirmation does not matched");
                } else if (newPassword.length() < 8 || newPassword.length() > 16) {
                    throw new Exception("Password must between 8 - 16 digit");
                } else {
                    int word = 0;
                    int digit = 0;
                    for (int i = 0; i < newPassword.length(); i++) {
                        Character un = newPassword.charAt(i);
                        if (Character.isAlphabetic(un)) {
                            word++;
                        }
                        if (Character.isDigit(un)) {
                            digit++;
                        }
                    }
                    if (word == 0) {
                        throw new Exception("Password must contain a letter");
                    }
                    if (digit == 0) {
                        throw new Exception("Password must contain a digit");
                    }
                }

                currentAccount.setPassword(crypto.UNEncode(newPassword));

                utx.begin();
                boolean successAccount = accountMgr.updateAccount(currentAccount);
                utx.commit();

                if (successAccount == false) {
                    throw new Exception("Update failed, please try again");
                } else {
                    updateStatus = "true";
                    currentHosteller = hostellerMgr.findHostellerByUsername(currentAccount);
                    message = "Account credential updated successfully.";
                    session.setAttribute("updateSuccess", updateStatus);
                    session.setAttribute("curHosteller", currentHosteller);
                    session.setAttribute("message", message);
                    response.sendRedirect("/hosteller/account/accountManagement");
                }
            }

        } catch (Exception ex) {
            updateStatus = "false";
            message = ex.getMessage();
            session.setAttribute("updateSuccess", updateStatus);
            session.setAttribute("message", message);
            response.sendRedirect("/hosteller/account/accountManagement");
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
