
package Controller.hosteller.resetAccountPassword;

import Controller.Crypto;
import Controller.Email;
import Model.Account;
import Model.AccountManager;
import Model.Hosteller;
import Model.HostellerManager;
import java.io.IOException;
import java.util.Random;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class sendResetEmail extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            Crypto encrypt = new Crypto();
            Hosteller curHosteller = new Hosteller();
            HostellerManager hostellerMgr = new HostellerManager(em);
            Account curAccount = new Account();
            AccountManager accountMgr = new AccountManager(em);

            if (request.getParameter("username") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                String encryptedUsername = encrypt.B64Encryption(request.getParameter("username"));

                curAccount = accountMgr.findAccountByUsername(encryptedUsername);

                int lowerLimit = 97;
                int upperLimit = 122;

                Random random = new Random();

                StringBuffer randomPw = new StringBuffer(7);

                for (int i = 0; i < 7; i++) {
                    int nextRandomChar = lowerLimit + (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
                    randomPw.append((char) nextRandomChar);
                }

                int randomInt1 = random.nextInt((9 - 0) + 1) + 0;

                randomPw.append(String.valueOf(randomInt1));

                try {
                    if (curAccount == null) {
                        throw new Exception("Account not found.");
                    } else if (curAccount.getLevel() != 1) {
                        throw new Exception("You are not authorized for changing this account.");
                    } else {
                        String email = new String();
                        curHosteller = hostellerMgr.findHostellerByUsername(curAccount);
                        email = curHosteller.getContact().getEmail();
                        String name = curHosteller.getFirstName() + " " + curHosteller.getMiddleName() + " " + curHosteller.getLastName();
                        
                        String content = ("\n\n Your new password for your Daikin Hostel Account is " + randomPw + ".\n\n This is a system generated email. Please do not reply.");
                                              
                        new Email(name, email, content);
                        String password = String.valueOf(randomPw);

                        curAccount.setPassword(encrypt.UNEncode(password));

                        utx.begin();
                        boolean accountUpdate = accountMgr.updateAccount(curAccount);
                        utx.commit();

                        if (accountUpdate == true) {
                            session.setAttribute("successStatus", "true");
                            session.setAttribute("successMsg", String.valueOf("Account Reset \n Your new password has been send to your account associated email"));
                            response.sendRedirect("/login/hosteller");
                        } else {
                            throw new Exception("An error has occured, please re-try.");
                        }

                    }
                } catch (Exception ex) {
                    String message = ex.getMessage();
                    session.setAttribute("errMsg", message);
                    session.setAttribute("errorStatus", "true");
                    response.sendRedirect("/login/hosteller");
                }

            }

        } catch (Exception ex) {
            String message = ex.getMessage();
            session.setAttribute("errMsg", message);
            session.setAttribute("errorStatus", "true");
            response.sendRedirect("/login/hosteller");
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
