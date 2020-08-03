package Controller.admin.tenantManagement;

import Controller.Crypto;
import Controller.Email;
import Model.Hosteller;
import Model.HostellerManager;
import Model.Registrationreq;
import Model.RegistrationreqManager;
import java.io.IOException;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateTenantStatus extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String update = (String) request.getParameter("update");
            HostellerManager hostellerManager = new HostellerManager(mgr);
            RegistrationreqManager registrationreqManager = new RegistrationreqManager(mgr);
            Hosteller hosteller;
            if (hostellerManager.findHostellerByID(((Hosteller) session.getAttribute("hosteller")).getHostellerID()) != null) {
                hosteller = hostellerManager.findHostellerByID(((Hosteller) session.getAttribute("hosteller")).getHostellerID());
                Registrationreq regreq = hosteller.getRegReqNo();
                switch (update.toLowerCase()) {
                    case "enable":
                        hosteller.setStatus("Active");
                        break;
                    case "disable":
                        hosteller.setStatus("Inactive");
                        break;
                    case "approve":
                        hosteller.setStatus("Active");
                        regreq.setStatus("Approved");
                        break;
                    case "reject":
                        hosteller.setStatus("Inactive");
                        regreq.setStatus("Rejected");
                        break;
                    default:
                        break;
                }
                utx.begin();
                hostellerManager.updateHostellerStatus(hosteller);
                registrationreqManager.updateRegistrationreq(regreq);
                utx.commit();
                Email email = new Email(hosteller.getLastName(), hosteller.getContact().getEmail(), hosteller.getStatus().toLowerCase().equals("active")?"\n\nYour account is activated. You are able to login now.":"\n\nYour account is terminated. Please contact the management for futher information.");
                session.setAttribute("success", "Hosteller status is updated successfully");
                response.sendRedirect("/retrieveTenantDetails?p=" + crypto.HTEncode(hosteller.getHostellerID()));
            } else {
                throw new Exception("Update fail. Unable to find hosteller details.");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveTenantListing?t=0");
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
