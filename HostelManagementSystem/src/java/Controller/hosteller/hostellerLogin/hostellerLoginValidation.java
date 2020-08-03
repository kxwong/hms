
package Controller.hosteller.hostellerLogin;

import Controller.Crypto;
import java.io.IOException;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Model.Account;
import Model.AccountManager;
import Model.Hosteller;
import Model.HostellerManager;
import javax.persistence.EntityManager;

public class hostellerLoginValidation extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            Crypto encrypt = new Crypto();
            String username = new String();
            String password = new String();

            if (request.getParameter("username") == null || request.getParameter("password") == null) {
                throw new Exception("Please insert username and password to proceed.");
            } else {
                username = encrypt.UNEncode(request.getParameter("username"));
                password = encrypt.UNEncode(request.getParameter("password"));
            }

            int accLevel = 0;

            AccountManager accManager = new AccountManager(em);
            HostellerManager hosManager = new HostellerManager(em);
            Hosteller hosteller = new Hosteller();

            accLevel = accManager.accountAuthenticate(username, password);
            if (accLevel == 1) {
                Account currentAccount = accManager.findAccountByUsername(username);
                hosteller = hosManager.findHostellerByUsername(currentAccount);
                if (hosteller.getStatus().toLowerCase().equals("active")) {
                    session.setAttribute("curHosteller", hosteller);
                    if (hosteller.getStayRoom() == null) {
                        session.setAttribute("roomStatus", "false");
                    } else {
                        session.setAttribute("roomStatus", "true");
                    }
                    response.sendRedirect("/retrieveCurrentRoomStatus");
                } else {
                    throw new Exception("Error: Account is not active, please contact admin for furhter info");
                }
            } else if (accLevel == 0) {
                throw new Exception("Error: Wrong username or password provided");
            } else {
                throw new Exception("Error: Admin account please proceed to another admin portal");
            }

        } catch (Exception ex) {
            String errorStatus = "true";
            session.setAttribute("errorStatus", errorStatus);
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
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
