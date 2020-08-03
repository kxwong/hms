package Controller.admin.issueManagement;

import Controller.Crypto;
import Model.Account;
import Model.Conversation;
import Model.ConversationManager;
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

public class storeIssueConversation extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crpyto = new Crypto();
        try {
            IssueManager issueManager = new IssueManager(mgr);
            if (issueManager.findIssueByID(((Issue) session.getAttribute("issue")).getCaseNo()) != null) {
                Issue issue = issueManager.findIssueByID(((Issue) session.getAttribute("issue")).getCaseNo());
                String content = request.getParameter("replyContent");
                if (content.equals("")) {
                    throw new Exception("Require input data of Content field");
                } else if(content.length() > 200){
                    throw new Exception("Content length can not over 200 letter");
                }else {
                    Date today = new Date();
                    ConversationManager conversationManager = new ConversationManager(mgr);
                    Conversation conversation = new Conversation();
                    conversation.setCaseNumber(issue);
                    conversation.setContent(content);
                    conversation.setContentID(conversationManager.generateID(issue.getCaseNo()));
                    conversation.setReplyBy((Account) session.getAttribute("account"));
                    conversation.setTime(today);
                    utx.begin();
                    conversationManager.addConversation(conversation);
                    utx.commit();
                    issue.setUpdateDate(today);
                    utx.begin();
                    issueManager.updateIssueStatus(issue);
                    utx.commit();
                    session.setAttribute("success", "Issue has been replied");
                    response.sendRedirect("/retrieveIssueDetails?p=" + crpyto.CAEncode(issue.getCaseNo()));
                }
            } else {
                session.setAttribute("error", "Selected issue is not existing");
                response.sendRedirect("/retrieveIssueListing?t=0");
            }

        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveIssueDetails?p=" + crpyto.CAEncode(((Issue) session.getAttribute("issue")).getCaseNo()));
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
