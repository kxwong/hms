
package Controller.hosteller.hostellerAccountRegistration;

import Controller.Crypto;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import Model.Hosteller;
import Model.HostellerManager;
import Model.Contact;
import Model.ContactManager;
import Model.Empdetails;
import Model.EmpdetailsManager;
import Model.Account;
import Model.AccountManager;
import Model.Registrationreq;
import Model.RegistrationreqManager;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;

public class hostellerDetailsValidation extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {

            HostellerManager hostellerMgr = new HostellerManager(em);
            AccountManager accManager = new AccountManager(em);
            RegistrationreqManager regReqMgr = new RegistrationreqManager(em);
            RegistrationreqManager registerRequestMgr = new RegistrationreqManager(em);
            Hosteller hosteller = new Hosteller();
            Contact contact = new Contact();
            Empdetails empDetails = new Empdetails();
            Registrationreq registerRequest = new Registrationreq();

            String imageByte = request.getParameter("imageByte");
            byte[] photoBytes = Base64.getDecoder().decode(imageByte.split(",")[1]);
            Account account = new Account();

            hosteller.setFirstName(request.getParameter("fname"));
            hosteller.setMiddleName(request.getParameter("mname"));
            hosteller.setLastName(request.getParameter("lname"));
            hosteller.setGender(request.getParameter("gender").toUpperCase());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            hosteller.setDoB(dateFormat.parse(request.getParameter("dob")));
            hosteller.setStatus("Inactive");
            hosteller.setNationality(request.getParameter("nationality"));
            hosteller.setIdentificationNo(request.getParameter("nric"));
            hosteller.setImage(photoBytes);

            //Auto generate hosteller ID start
            String newHostellerID = "HT";
            List<Hosteller> hostellerList = hostellerMgr.findAll();
            if (hostellerList.isEmpty()) {
                newHostellerID = "HT0001";
            } else {
                Hosteller latestHosteller = hostellerList.get(hostellerList.size() - 1);
                String currentID = latestHosteller.getHostellerID();
                String tempSerial = currentID.substring(2);
                int currentNum = Integer.parseInt(tempSerial) + 1;
                String currentSerial = String.valueOf(currentNum);
                if (currentSerial.length() == 1) {
                    newHostellerID += "000" + currentSerial;
                } else if (currentSerial.length() == 2) {
                    newHostellerID += "00" + currentSerial;
                } else if (currentSerial.length() == 3) {
                    newHostellerID += "0" + currentSerial;
                } else if (currentSerial.length() == 4) {
                    newHostellerID += currentSerial;
                }
            }
            //Auto generate hosteller ID end

            hosteller.setHostellerID(newHostellerID);

            //Start of contact objects
            contact.setHostellerID(newHostellerID);
            String address = request.getParameter("address1") + " " + request.getParameter("address2");
            contact.setAddress(address);
            contact.setPostcode(request.getParameter("postcode"));
            contact.setCity(request.getParameter("city"));
            contact.setState(request.getParameter("state"));
            contact.setCountry(request.getParameter("country"));
            
            
            
            contact.setMobilePhone(new BigInteger(String.valueOf(request.getParameter("countryCode")) +String.valueOf(request.getParameter("contactNum"))));
            contact.setHomePhone(new BigInteger(String.valueOf(request.getParameter("countryCode")) +String.valueOf(request.getParameter("homeNum"))));
            contact.setEmail(request.getParameter("email"));
            //End of contact objects

            //Start of Empdetails object
            empDetails.setHostellerID(newHostellerID);
            empDetails.setBranch(request.getParameter("branch"));
            empDetails.setDepartment(request.getParameter("department"));
            empDetails.setWorkerID(request.getParameter("workerID"));
            //End of Empdetails object

            //Start of Account object
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            account.setUsername(username);
            account.setPassword(password);
            account.setLevel(Short.parseShort("1"));
            //End of Account object

            //setting of hosteller account column
            hosteller.setUsername(account);

            //Start of Registration Request object
            //Auto generate request ID start
            String newRequestID = "RR";
            List<Registrationreq> RegistrationReqList = registerRequestMgr.findAll();
            if (RegistrationReqList.isEmpty()) {
                newRequestID = "RR0001";
            } else {
                Registrationreq latestRequest = RegistrationReqList.get(RegistrationReqList.size() - 1);
                String currentReqID = latestRequest.getRequestNo();
                String tempReqSerial = currentReqID.substring(2);
                String currenReqtSerial = String.valueOf(Integer.parseInt(tempReqSerial) + 1);

                if (currenReqtSerial.length() == 1) {
                    newRequestID += "000" + currenReqtSerial;
                } else if (currenReqtSerial.length() == 2) {
                    newRequestID += "00" + currenReqtSerial;
                } else if (currenReqtSerial.length() == 3) {
                    newRequestID += "0" + currenReqtSerial;
                } else if (currenReqtSerial.length() == 4) {
                    newRequestID += currenReqtSerial;
                }
            }

            //Auto generate request ID end
            registerRequest.setRequestNo(newRequestID);
            Date todayDate = new Date();
            registerRequest.setRequestDate(todayDate);
            registerRequest.setUpdateDate(todayDate);
            registerRequest.setStatus("Pending");
            //End of Registration Request object
            
            utx.begin();
            boolean updateAcc = accManager.addAccount(account);
            boolean updateReg = regReqMgr.addRegistrationreq(registerRequest);
            utx.commit();
            hosteller.setContact(contact);
            hosteller.setEmpdetails(empDetails);
            hosteller.setUsername(account);
            hosteller.setRegReqNo(registerRequest);
            
            
            //Create new data start
            utx.begin();
            boolean successHosteller = hostellerMgr.addHosteller(hosteller);
            utx.commit();
            
            //Initialize relationship from account to hosteller
            account.setHosteller(hosteller);
            utx.begin();
            boolean successUpdateAccount = accManager.updateAccount(account);
            utx.commit();
            
            //Create new data end

            if (successHosteller && updateAcc && updateReg && successUpdateAccount) {
                String successStatus = "true";
                String successMsg = "Your registration request is done, account status will be informed through your email";
                session.setAttribute("successStatus", successStatus);
                session.setAttribute("successMsg", successMsg);
                response.sendRedirect("/login/hosteller");
            } else {
                throw new Exception("An error has occured, please retry again");
            }

        } catch (Exception ex) {
            String errorStatus = "true";
            session.setAttribute("errorStatus", errorStatus);
            session.setAttribute("errMsg", ex.getMessage());
            response.sendRedirect("/login/hosteller");
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
