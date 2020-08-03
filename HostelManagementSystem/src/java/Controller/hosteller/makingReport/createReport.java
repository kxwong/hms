package Controller.hosteller.makingReport;

import Model.Attachment;
import Model.AttachmentManager;
import Model.Conversation;
import Model.ConversationManager;
import Model.Issue;
import Model.IssueManager;
import Model.Hosteller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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

public class createReport extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Date curDate = new Date();

            Issue newIssue = new Issue();
            IssueManager issueMgr = new IssueManager(em);

            List<Conversation> conversationList = new ArrayList();
            Conversation newConversation = new Conversation();
            ConversationManager conversationMgr = new ConversationManager(em);

            Hosteller curHosteller = new Hosteller();

            List<Attachment> attachmentList = new ArrayList();
            AttachmentManager attachmentMgr = new AttachmentManager(em);

            if (request.getParameterValues("imageByte") != null) {
                String fileByte[] = request.getParameterValues("imageByte");

                for (int i = 0; i < fileByte.length; i++) {

                    String id = attachmentMgr.generateID();
                    Attachment newAttachment = new Attachment();
                    byte[] fileBytes = Base64.getDecoder().decode(fileByte[i].split(",")[1]);
                    newAttachment.setAttachID(id);
                    newAttachment.setFile(fileBytes);
                    newAttachment.setHeader(fileByte[i].split(",")[0]);

                    utx.begin();
                    boolean attachmentSuccess = attachmentMgr.addAttachment(newAttachment);
                    utx.commit();
                    attachmentList.add(newAttachment);
                }
            }

            if (request.getParameter("type") == null) {
                throw new Exception("An error has occured.");
            } else {
                String type = request.getParameter("type");
                String category = request.getParameter("category");
                String title = request.getParameter("title");
                String issueDetails = request.getParameter("issueDetails");

                curHosteller = (Hosteller) session.getAttribute("curHosteller");
                newIssue.setUpdateDate(curDate);
                newIssue.setStatus("Processing");
                String caseID = issueMgr.generateID();
                newIssue.setCaseNo(caseID);
                newIssue.setIssueType(type);
                newIssue.setTitle(title);
                newIssue.setCategory(category);
                newIssue.setIssueDate(curDate);
                newIssue.setIssueBy(curHosteller);

                newConversation.setContent(issueDetails);
                newConversation.setReplyBy(curHosteller.getUsername());
                newConversation.setCaseNumber(newIssue);
                newConversation.setContentID(conversationMgr.generateID(newIssue.getCaseNo()));
                newConversation.setTime(curDate);

                conversationList.add(newConversation);
                newIssue.setConversationList(conversationList);
                newIssue.setAttachmentList(attachmentList);

                utx.begin();
                boolean issueSuccess = issueMgr.addIssue(newIssue);
                boolean conversationSuccess = conversationMgr.addConversation(newConversation);
                utx.commit();

                if (issueSuccess == true && conversationSuccess == true) {
                    List<Issue> newSelfIssueList = issueMgr.findAllByHosteller(curHosteller);
                    session.setAttribute("selfIssueList", newSelfIssueList);
                    session.setAttribute("successMsg", "Your report has successfully made, you are able to manage it through here.");
                    response.sendRedirect("/retrieveAllSelfCase");
                } else {
                    throw new Exception("An error has occured");
                }
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/retrieveAllSelfCase");
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
