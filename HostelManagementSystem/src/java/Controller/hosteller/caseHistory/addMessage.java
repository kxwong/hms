
package Controller.hosteller.caseHistory;

import Model.Conversation;
import Model.ConversationManager;
import Model.Hosteller;
import Model.Issue;
import Model.IssueManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class addMessage extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Hosteller hosteller = (Hosteller) session.getAttribute("curHosteller");
            Conversation newConversation = new Conversation();
            ConversationManager conversationMgr = new ConversationManager(em);
            IssueManager issueMgr = new IssueManager(em);
            Issue curIssue = issueMgr.findIssueByID(request.getParameter("caseID"));
            Date curTime = new Date();

            List<Conversation> conversationList = new ArrayList();
            conversationList = curIssue.getConversationList();

            newConversation.setCaseNumber(curIssue);
            newConversation.setContent(request.getParameter("replyMessage"));

            String id = conversationMgr.generateID(curIssue.getCaseNo());
            newConversation.setContentID(id);
            newConversation.setReplyBy(hosteller.getUsername());
            newConversation.setTime(curTime);

            conversationList.add(newConversation);


            curIssue.setUpdateDate(curTime);
            curIssue.setConversationList(conversationList);
            
            if(curIssue.getStatus().equals("Closed")){
                throw new Exception("The case has been closed.");
            }

            utx.begin();
            boolean conversationSuccess = conversationMgr.addConversation(newConversation);
            boolean issueSuccess = issueMgr.updateIssue(curIssue);
            utx.commit();

            if (conversationSuccess == true && issueSuccess == true) {
                session.setAttribute("cid", curIssue.getCaseNo());
                session.setAttribute("successMsg", "Your message has been uploaded.");
                response.sendRedirect("/retrieveCaseDetails");
            } else {
                throw new Exception("An error has occured.");
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
