
package Controller.admin.issueManagement;

import Controller.Crypto;
import Model.Issue;
import Model.IssueManager;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateIssueStatus extends HttpServlet {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String caseNo;
            Crypto crypto = new Crypto();
            try {
                String enCaseNo = request.getParameter("p");
                caseNo = crypto.CADecode(enCaseNo);
            } catch (Exception ex) {
                throw new Exception("Selected case issue is not existed");
            }
            IssueManager issueManager = new IssueManager(mgr);
            if (issueManager.findIssueByID(caseNo) != null) {
                Issue issue = issueManager.findIssueByID(caseNo);
                issue.setStatus("Closed");
                issue.setUpdateDate(new Date());
                utx.begin();
                issueManager.updateIssueStatus(issue);
                utx.commit();
                session.setAttribute("success", "Issue case has been closed");
                response.sendRedirect("/retrieveIssueDetails?p=" + crypto.CAEncode(issue.getCaseNo()));
            } else {
                throw new Exception("Selected case issue is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveIssueListing?t=0");
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
