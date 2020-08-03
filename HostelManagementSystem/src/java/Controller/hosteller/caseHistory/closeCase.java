
package Controller.hosteller.caseHistory;

import Model.Issue;
import Model.IssueManager;
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

public class closeCase extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Issue curIssue = new Issue();
            IssueManager issueMgr = new IssueManager(em);

            if (request.getParameter("iid") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                String iid = request.getParameter("iid");
                curIssue = issueMgr.findIssueByID(iid);

                curIssue.setStatus("Closed");

                utx.begin();
                boolean updateSuccess = issueMgr.updateIssue(curIssue);
                utx.commit();

                if (updateSuccess == true) {
                    session.setAttribute("cid", curIssue.getCaseNo());
                    session.setAttribute("successMsg", "Case successfully closed.");
                    response.sendRedirect("/retrieveCaseDetails");
                } else {
                    throw new Exception("An error has occured, case close failed.");
                }

            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/reporting/manageSelfCaseDetails");
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
