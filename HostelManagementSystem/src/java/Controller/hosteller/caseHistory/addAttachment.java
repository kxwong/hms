package Controller.hosteller.caseHistory;

import Model.Attachment;
import Model.AttachmentManager;
import Model.Hosteller;
import Model.Issue;
import Model.IssueManager;
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

public class addAttachment extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Hosteller hosteller = (Hosteller) session.getAttribute("curHosteller");
            AttachmentManager attachmentMgr = new AttachmentManager(em);
            IssueManager issueMgr = new IssueManager(em);
            Issue curIssue = issueMgr.findIssueByID(request.getParameter("caseID"));
            List<Attachment> attachmentList = new ArrayList();
            Date curTime = new Date();
            boolean attachmentSuccess = false;

            String fileByte[] = request.getParameterValues("imageByte");
            attachmentList = curIssue.getAttachmentList();

            if (curIssue.getStatus().equals("Closed")) {
                throw new Exception("The case has been closed.");
            }

            for (int i = 0; i < fileByte.length; i++) {

                String id = attachmentMgr.generateID();
                Attachment newAttachment = new Attachment();
                byte[] fileBytes = Base64.getDecoder().decode(fileByte[i].split(",")[1]);
                newAttachment.setAttachID(id);
                newAttachment.setFile(fileBytes);
                newAttachment.setHeader(fileByte[i].split(",")[0]);

                utx.begin();
                attachmentSuccess = attachmentMgr.addAttachment(newAttachment);
                utx.commit();

                attachmentList.add(newAttachment);
            }

            curIssue.setUpdateDate(curTime);
            curIssue.setAttachmentList(attachmentList);

            utx.begin();
            boolean issueUpdateSuccess = issueMgr.updateIssue(curIssue);
            utx.commit();

            if (attachmentSuccess == true && issueUpdateSuccess == true) {
                session.setAttribute("cid", curIssue.getCaseNo());
                session.setAttribute("successMsg", "Your attachment has been uploaded.");
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
