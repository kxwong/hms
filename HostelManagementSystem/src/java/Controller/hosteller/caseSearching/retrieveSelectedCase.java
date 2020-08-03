
package Controller.hosteller.caseSearching;

import Controller.Crypto;
import Model.Issue;
import Model.IssueManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveSelectedCase extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {

            Issue selectedIssue = new Issue();
            IssueManager issueMgr = new IssueManager(em);

            if (request.getParameter("iid") == null || session.getAttribute("curHosteller") == null) {
                throw new Exception("Please re-login;");
            } else {
                String issueID = encrypt.UNDecode(request.getParameter("iid"));
                selectedIssue = issueMgr.findIssueByID(issueID);
                
                session.setAttribute("selectedIssue", selectedIssue);
                response.sendRedirect("/hosteller/reporting/selectedCaseDetails");
            }

        } catch (Exception ex) {
            response.sendRedirect("/retrieveAllCase");
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
