
package Controller.hosteller.profileDetailsManagement;

import Model.Hosteller;
import Model.HostellerManager;
import java.io.IOException;
import java.util.Base64;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class editHostellerProfilePic extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String message = "false";
        String updateSuccess = "false";
        String hostellerID = "empty";
        Hosteller hosteller = new Hosteller();
        HostellerManager hostellerMgr = new HostellerManager(em);

        try {
            hostellerID = request.getParameter("hostellerID");
            hosteller = hostellerMgr.findHostellerByID(hostellerID);

            String imageByte = request.getParameter("imageByte");
            byte[] photoBytes = Base64.getDecoder().decode(imageByte.split(",")[1]);

            hosteller.setImage(photoBytes);

            utx.begin();
            boolean profilePicUpdate = hostellerMgr.updateHosteller(hosteller);
            utx.commit();

            if (profilePicUpdate != true) {
                throw new Exception("Profile picture update failed, please try again.");
            } else {
                updateSuccess = "true";
                message = "Profile picture successfully updated.";
                session.setAttribute("updateSuccess", updateSuccess);
                session.setAttribute("curHosteller", hosteller);
                session.setAttribute("message", message);
                response.sendRedirect("/hosteller/account/accountManagement");
            }

        } catch (Exception ex) {
            updateSuccess = "false";
            message = ex.getMessage();
            session.setAttribute("updateSuccess", updateSuccess);
            session.setAttribute("message", message);
            response.sendRedirect("/hosteller/account/accountManagement");

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
