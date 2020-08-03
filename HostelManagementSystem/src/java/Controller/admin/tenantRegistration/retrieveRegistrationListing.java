
package Controller.admin.tenantRegistration;

import Controller.Crypto;
import Controller.DateToString;
import Model.RegistrationReqSearchingCriteria;
import Model.RegistrationreqManager;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveRegistrationListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegistrationreqManager registrationreqManager = new RegistrationreqManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            RegistrationReqSearchingCriteria searchingCriteria = (RegistrationReqSearchingCriteria) session.getAttribute("rrsearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new RegistrationReqSearchingCriteria("0",getInitReqStartDate(registrationreqManager), getInitReqEndDate(registrationreqManager), "", "", getInitUpdateStartDate(registrationreqManager), getInitUpdateEndDate(registrationreqManager), "Pending", "r.requestNo", "ASC");
                    String status;
                    try {
                        String enStatus = request.getParameter("s");
                        status = crypto.CDecode(enStatus);
                    } catch (Exception ex) {
                        throw new Exception("Selected status is not existed");
                    }
                    if (status.equals("All")) {
                        searchingCriteria.setStatus("");
                    } else {
                        searchingCriteria.setStatus(status);
                    }
                    searchingCriteria.setPreviousTerm("1");
                    session.setAttribute("rrsearchingCriteria", searchingCriteria);
                    session.setAttribute("registrationReqList", registrationreqManager.findAllWithParameter(searchingCriteria.getRequestNo(),
                            searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getTenantName(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/hosteller/registrationlisting");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String requestNo = request.getParameter("requestNo");
                    String requestSTime = request.getParameter("requestSTime");
                    String requestETime = request.getParameter("requestETime");
                    String tenantName = request.getParameter("tenantName");
                    String updateSTime = request.getParameter("updateSTime");
                    String updateETime = request.getParameter("updateETime");
                    Date requestStartTime = null;
                    Date requestEndTime = null;
                    Date updateStartTime = null;
                    Date updateEndTime = null;

                    if (requestNo.equals("")&& tenantName.equals("") && requestSTime.equals("") && requestETime.equals("") && updateSTime.equals("") && updateETime.equals("")) {
                        throw new Exception("No input for searching");
                    } else if ((requestSTime.equals("") && !requestETime.equals("")) || (!requestSTime.equals("") && requestETime.equals(""))) {
                        if (requestSTime.equals("")) {
                            throw new Exception("Require request start time as search range");
                        } else {
                            throw new Exception("Require request end time as search range");
                        }
                    } else if ((updateSTime.equals("") && !updateETime.equals("")) || (!updateSTime.equals("") && updateETime.equals(""))) {
                        if (updateSTime.equals("")) {
                            throw new Exception("Require update start time as search range");
                        } else {
                            throw new Exception("Require update end time as search range");
                        }
                    } else {
                        if (!requestSTime.equals("") && !requestETime.equals("")) {
                            requestStartTime = dateString.StringToStartDate(requestSTime);
                            requestEndTime = dateString.StringToEndDate(requestETime);
                            if (requestStartTime.after(requestEndTime)) {
                                throw new Exception("Request start time should before the end time");
                            }
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            updateStartTime = dateString.StringToStartDate(updateSTime);
                            updateEndTime = dateString.StringToEndDate(updateETime);
                            if (updateStartTime.after(updateEndTime)) {
                                throw new Exception("Update start time should before the end time");
                            }
                        }
                        if (!requestNo.equals("")) {
                            searchingCriteria.setRequestNo(requestNo);
                        } else {
                            searchingCriteria.setRequestNo("");
                        }
                        if (!tenantName.equals("")) {
                            searchingCriteria.setTenantName(tenantName);
                        } else {
                            searchingCriteria.setTenantName("");
                        }
                        if (!requestSTime.equals("") && !requestETime.equals("")) {
                            searchingCriteria.setRequestSTime(requestStartTime);
                            searchingCriteria.setRequestETime(requestEndTime);
                        } else {
                            searchingCriteria.setRequestSTime(getInitReqStartDate(registrationreqManager));
                            searchingCriteria.setRequestETime(getInitReqEndDate(registrationreqManager));
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            searchingCriteria.setUpdateSTime(updateStartTime);
                            searchingCriteria.setUpdateETime(updateEndTime);
                        } else {
                            searchingCriteria.setUpdateSTime(getInitUpdateStartDate(registrationreqManager));
                            searchingCriteria.setUpdateETime(getInitUpdateEndDate(registrationreqManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("rrsearchingCriteria", searchingCriteria);
                        session.setAttribute("registrationReqList", registrationreqManager.findAllWithParameter(searchingCriteria.getRequestNo(),
                            searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getTenantName(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/hosteller/registrationlisting");
                    }
                    break;
                case 3:
                    String order;
                    try {
                        String enOrder = request.getParameter("o");
                        order = crypto.CDecode(enOrder);
                    } catch (Exception ex) {
                        throw new Exception("Selected header is not existed");
                    }
                    searchingCriteria.setOrderParameter(order);
                    if (searchingCriteria.getPreviousTerm().equals("3")) {
                        if (searchingCriteria.getOrder().equals("ASC")) {
                            searchingCriteria.setOrder("DESC");
                        } else {
                            searchingCriteria.setOrder("ASC");
                        }
                    }
                    searchingCriteria.setPreviousTerm("3");
                    session.setAttribute("rrsearchingCriteria", searchingCriteria);
                    session.setAttribute("registrationReqList", registrationreqManager.findAllWithParameter(searchingCriteria.getRequestNo(),
                            searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getTenantName(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/hosteller/registrationlisting");
                    break;
                default:
                    if (term != 0) {
                        session.setAttribute("error", "Invalid searching criteria");
                    }
                    throw new Exception();
            }
        } catch (Exception ex) {
            if (session.getAttribute("error") == null) {
                session.setAttribute("error", ex.getLocalizedMessage());
            }
            session.setAttribute("rrsearchingCriteria", new RegistrationReqSearchingCriteria("0",getInitReqStartDate(registrationreqManager), getInitReqEndDate(registrationreqManager), "", "", getInitUpdateStartDate(registrationreqManager), getInitUpdateEndDate(registrationreqManager), "Pending", "r.requestNo", "ASC"));
            session.setAttribute("registrationReqList", registrationreqManager.findAllWithParameter("", getInitReqStartDate(registrationreqManager), getInitReqEndDate(registrationreqManager), getInitUpdateStartDate(registrationreqManager), getInitUpdateEndDate(registrationreqManager),"", "Pending", "r.requestNo", "ASC"));
            response.sendRedirect("/admin/hosteller/registrationlisting");
        }
    }

    protected Date getInitReqStartDate(RegistrationreqManager registrationreqManager) {
        try {
            Date requestStartDate = registrationreqManager.findRequestDate().get(0);
            requestStartDate.setDate(requestStartDate.getDate() - 1);
            return requestStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitReqEndDate(RegistrationreqManager registrationreqManager) {
        try {
            Date requestEndDate = registrationreqManager.findRequestDate().get(registrationreqManager.findRequestDate().size() - 1);
            requestEndDate.setDate(requestEndDate.getDate() + 1);
            return requestEndDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateStartDate(RegistrationreqManager registrationreqManager) {
        try {
            Date updateStartDate = registrationreqManager.findUpdateDate().get(0);
            updateStartDate.setDate(updateStartDate.getDate() - 1);
            return updateStartDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateEndDate(RegistrationreqManager registrationreqManager) {
        try {
            Date updateEndDate = registrationreqManager.findUpdateDate().get(registrationreqManager.findUpdateDate().size() - 1);
            updateEndDate.setDate(updateEndDate.getDate() + 1);
            return updateEndDate;
        } catch (Exception ex) {
            return null;
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
