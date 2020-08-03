package Controller.admin.tenantRegistration;

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

public class updateRegistrationReq extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String status = (String) request.getParameter("status");
            String remark = (String) request.getParameter("remark");
            HostellerManager hostellerManager = new HostellerManager(mgr);
            RegistrationreqManager registrationreqManager = new RegistrationreqManager(mgr);
            Registrationreq registrationreq;
            Hosteller hosteller;
            if (registrationreqManager.findRegistrationreqByID(((Registrationreq) session.getAttribute("registrationReq")).getRequestNo()) != null) {
                registrationreq = registrationreqManager.findRegistrationreqByID(((Registrationreq) session.getAttribute("registrationReq")).getRequestNo());
                hosteller = hostellerManager.findHostellerByRegreq(registrationreq.getRequestNo());
                switch (status.toLowerCase()) {
                    case "approved":
                        registrationreq.setStatus("Approved");
                        hosteller.setStatus("Active");
                        break;
                    case "rejected":
                        registrationreq.setStatus("Rejected");
                        hosteller.setStatus("Inactive");
                        break;
                    default:
                        throw new Exception("Update fail. Status can not update to " + status);
                }
                registrationreq.setRemark(remark);
                utx.begin();
                hostellerManager.updateHostellerStatus(hosteller);
                registrationreqManager.updateRegistrationreq(registrationreq);
                utx.commit();
                Email email = new Email(hosteller.getLastName(), hosteller.getContact().getEmail(), hosteller.getStatus().toLowerCase().equals("active") ? "\n\nYour account registration is approved and account is activated. You are able to login now." : "\n\nYour account registration is rejected. Please contact the management for futher information.");
                session.setAttribute("success", "Registration request is updated successfully");
                response.sendRedirect("/retrieveRegistrationDetails?p=" + crypto.RREncode(registrationreq.getRequestNo()));
            } else {
                throw new Exception("Update fail. Unable to find registration request details.");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveRegistrationListing?t=0");
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
