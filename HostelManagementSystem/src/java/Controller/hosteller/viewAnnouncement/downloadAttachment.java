
package Controller.hosteller.viewAnnouncement;

import Model.Attachment;
import Model.AttachmentManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class downloadAttachment extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Attachment selectedAttachment = new Attachment();
        AttachmentManager attachmentMgr = new AttachmentManager(em);

        try {

            if (request.getParameter("attid") == null) {
                throw new Exception("Please re-login.");
            } else {
                String attid = request.getParameter("attid");
                selectedAttachment = attachmentMgr.findAttachmentByID(attid);

                try (OutputStream out = new FileOutputStream("filename.pdf")) {
                    out.write(selectedAttachment.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception ex) {
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
