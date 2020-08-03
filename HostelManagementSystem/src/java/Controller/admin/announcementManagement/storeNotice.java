package Controller.admin.announcementManagement;

import Model.Announcement;
import Model.AnnouncementManager;
import Model.Attachment;
import Model.AttachmentManager;
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

public class storeNotice extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String[] fileByteList = {};
            try {
                fileByteList = request.getParameterValues("fileByte");
            } catch (Exception ex) {
            }
            AnnouncementManager announcementManager = new AnnouncementManager(mgr);
            AttachmentManager attachmentManager = new AttachmentManager(mgr);
            boolean isEmptyValue = false;
            if (fileByteList != null) {
                for (String fileByte : fileByteList) {
                    if (fileByte.length() <= 0) {
                        isEmptyValue = true;
                    }
                }
            }
            boolean isDuplivateValue = false;
            if (fileByteList != null) {
                for (int i = 0; i < fileByteList.length; i++) {
                    for (int j = 0; j < fileByteList.length; j++) {
                        if (fileByteList[i].equals(fileByteList[j]) && i != j) {
                            isDuplivateValue = true;
                        }
                    }
                }
            }
            if (title.equals("")) {
                throw new Exception("Please fill out title field");
            } else if (content.equals("")) {
                throw new Exception("Please fill out content field");
            } else if (isEmptyValue) {
                throw new Exception("Please attach the attachment");
            } else if (isDuplivateValue) {
                throw new Exception("Please attach diffrent attachment");
            } else if (title.length() > 100) {
                throw new Exception("Title is too long");
            } else if (content.length() > 3000) {
                throw new Exception("Content is too long");
            } else {
                Announcement notice = new Announcement();
                notice.setAnnounceID(announcementManager.generateID());
                notice.setContent(content);
                notice.setTitle(title);
                notice.setDate(new Date());
                if (fileByteList != null) {
                    List<Attachment> attList = new ArrayList<>();
                    for (String fileByte : fileByteList) {
                        Attachment att = new Attachment();
                        att.setAttachID(attachmentManager.generateID());
                        att.setHeader(fileByte.split(",")[0]);
                        att.setFile(Base64.getDecoder().decode(fileByte.split(",")[1]));
                        utx.begin();
                        attachmentManager.addAttachment(att);
                        utx.commit();
                        attList.add(att);
                    }
                    notice.setAttachmentList(attList);
                }
                utx.begin();
                announcementManager.addAnnouncement(notice);
                utx.commit();
                session.setAttribute("success", "New notice is addedd successfully");
                response.sendRedirect("/retrieveNoticeListing");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/admin/announcement/makeAnnouncement");
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
