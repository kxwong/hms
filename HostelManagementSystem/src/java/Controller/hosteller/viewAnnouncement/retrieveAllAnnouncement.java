
package Controller.hosteller.viewAnnouncement;

import Controller.Crypto;
import Model.Announcement;
import Model.AnnouncementManager;
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

public class retrieveAllAnnouncement extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {

            List<Announcement> announcementList = new ArrayList();
            AnnouncementManager announcementMgr = new AnnouncementManager(em);

            if (request.getParameter("ob") != null && request.getParameter("seq") != null) {
                String orderBy = encrypt.UNDecode(request.getParameter("ob"));
                String seq = encrypt.UNDecode(request.getParameter("seq"));
                seq = seq.toUpperCase();

                announcementList = em.createQuery("SELECT a FROM Announcement a " + "ORDER BY " + orderBy + " " + seq).getResultList();
                session.setAttribute("seq", seq.toLowerCase());
            }else{
                announcementList = announcementMgr.retrieveAllAnnouncementDesc();
            }

            session.setAttribute("announcementList", announcementList);
            response.sendRedirect("/hosteller/announcement/announcementViewing");

        } catch (Exception ex) {
            response.sendRedirect("/retrieveCurrentRoomStatus");
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
