
package Controller.hosteller.caseHistory;

import Controller.Crypto;
import Model.Conversation;
import Model.ConversationManager;
import Model.Issue;
import Model.IssueManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveCaseDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {
            Issue curIssue = new Issue();
            IssueManager issueMgr = new IssueManager(em);
            String cid = new String();
            List<Conversation> conversationList = new ArrayList();
            ConversationManager conversationMgr = new ConversationManager(em);

            if (request.getParameter("cid") == null && session.getAttribute("cid")==null) {
                throw new Exception("An error has occured.");
            } else {
                
                if(request.getParameter("cid")!=null){
                    cid = encrypt.UNDecode(request.getParameter("cid"));
                }else{
                    cid = (String)session.getAttribute("cid");
                }
                                              
                curIssue = issueMgr.findIssueByID(cid);  
                conversationList = conversationMgr.findAllConversation(curIssue);
                
                session.setAttribute("curIssue", curIssue);  
                session.setAttribute("conversationList", conversationList);
                response.sendRedirect("/hosteller/reporting/manageSelfCaseDetails");
            }

        } catch (Exception ex) {
            response.sendRedirect("/hosteller/reporting/caseHistory");
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
