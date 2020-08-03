package Controller.admin.tenantRegistration;

import Controller.Email;
import Model.Hosteller;
import Model.HostellerManager;
import Model.Registrationreq;
import Model.RegistrationreqManager;
import java.io.IOException;
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

public class updateBatchRegistrationReq extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            RegistrationreqManager registrationreqManager = new RegistrationreqManager(mgr);
            HostellerManager hostellerManager = new HostellerManager(mgr);
            List<Registrationreq> regreqList = registrationreqManager.findAll();
            int count = 0;
            for (Registrationreq regreq : regreqList) {
                if (regreq.getStatus().toLowerCase().equals("pending")) {
                    regreq.setStatus("Approved");
                    Hosteller hosteller = hostellerManager.findHostellerByRegreq(regreq.getRequestNo());
                    hosteller.setStatus("Active");
                    utx.begin();
                    hostellerManager.updateHostellerStatus(hosteller);
                    registrationreqManager.updateRegistrationreq(regreq);
                    utx.commit();
                    count++;
                    Email email = new Email(hosteller.getLastName(), hosteller.getContact().getEmail(), hosteller.getStatus().toLowerCase().equals("active") ? "\n\nYour account registration is approved and account is activated. You are able to login now." : "\n\nYour account registration is rejected. Please contact the management for futher information.");
                }
            }
            if (count >= 1) {
                session.setAttribute("success", "Total " + count + " pending registration request is approved successfully");
            } else {
                throw new Exception("None of the pending registration request is waiting for approval");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
        }
        response.sendRedirect("/retrieveRegistrationListing?t=0");
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
