
package Controller.hosteller.profileDetailsManagement;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Model.*;
import java.math.BigInteger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class editHostellerDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String updateSuccess = "false";
        String message = "empty";

        try {
            Hosteller currentHosteller = (Hosteller) session.getAttribute("curHosteller");
            HostellerManager hostellerMgr = new HostellerManager(em);
            EmpdetailsManager empdetailsMgr = new EmpdetailsManager(em);
            ContactManager contactMgr = new ContactManager(em);
            String hostellerID = currentHosteller.getHostellerID();
            String nric = request.getParameter("nric");
            String nationality = request.getParameter("nationality");
            String address = request.getParameter("address");
            String postCode = request.getParameter("postCode");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String country = request.getParameter("country");
            String email = request.getParameter("email");
            String workerID = request.getParameter("workerID");
            String branch = request.getParameter("branch");
            String department = request.getParameter("department");

            currentHosteller.setIdentificationNo(nric);
            currentHosteller.setNationality(nationality);

            Empdetails empdetails = currentHosteller.getEmpdetails();
            empdetails.setBranch(branch);
            empdetails.setDepartment(department);
            empdetails.setWorkerID(workerID);

            utx.begin();
            boolean successEmp = empdetailsMgr.updateEmpdetails(empdetails);
            utx.commit();

            Contact contact = currentHosteller.getContact();
            contact.setEmail(email);
            contact.setAddress(address);
            contact.setCity(city);
            contact.setCountry(country);
            contact.setHomePhone(new BigInteger(String.valueOf(request.getParameter("countryCode")) + String.valueOf(request.getParameter("homeContact"))));
            contact.setMobilePhone(new BigInteger(String.valueOf(request.getParameter("countryCode")) + String.valueOf(request.getParameter("mobileContact"))));
            contact.setPostcode(postCode);
            contact.setState(state);

            utx.begin();
            boolean successContact = contactMgr.updateContact(contact);
            utx.commit();


            utx.begin();
            boolean successHosteller = hostellerMgr.updateHosteller(currentHosteller);
            utx.commit();

            if (successHosteller != true && successContact != true && successEmp != true) {
                throw new Exception("Error: An error occured, update fail");
            } else {
                updateSuccess = "true";
                currentHosteller = hostellerMgr.findHostellerByID(hostellerID);
                message = "Details updated successfully.";
                session.setAttribute("updateSuccess", updateSuccess);
                session.setAttribute("curHosteller", currentHosteller);
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
