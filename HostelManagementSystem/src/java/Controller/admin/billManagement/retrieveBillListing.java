package Controller.admin.billManagement;

import Controller.Crypto;
import Controller.DateToString;
import Model.BillSearchingCriteria;
import Model.BillManager;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveBillListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BillManager billManager = new BillManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            BillSearchingCriteria searchingCriteria = (BillSearchingCriteria) session.getAttribute("bsearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new BillSearchingCriteria("0", "", "", "", getInitIssueStartDate(billManager), getInitIssueEndDate(billManager), getInitDueStartDate(billManager), getInitDueEndDate(billManager), "Pending", "b.billNo", "ASC");
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
                    session.setAttribute("bsearchingCriteria", searchingCriteria);
                    session.setAttribute("billList", billManager.findAllWithParameter(searchingCriteria.getBillNo(), searchingCriteria.getDesc(), searchingCriteria.getIssueTo(),
                            searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getDueSTime(), searchingCriteria.getDueETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/rental/billListing");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String billno = request.getParameter("billno");
                    String desc = request.getParameter("desc");
                    String issueTo = request.getParameter("issueTo");
                    String issueSTime = request.getParameter("issueSTime");
                    String issueETime = request.getParameter("issueETime");
                    String dueSTime = request.getParameter("dueSTime");
                    String dueETime = request.getParameter("dueETime");
                    Date issueStartTime = null;
                    Date issueEndTime = null;
                    Date dueStartTime = null;
                    Date dueEndTime = null;

                    if (billno.equals("") && desc.equals("") && issueTo.equals("") && issueSTime.equals("") && issueETime.equals("") && dueSTime.equals("") && dueETime.equals("")) {
                        throw new Exception("No input for searching");
                    } else if ((issueSTime.equals("") && !issueETime.equals("")) || (!issueSTime.equals("") && issueETime.equals(""))) {
                        if (issueSTime.equals("")) {
                            throw new Exception("Require issue start time as search range");
                        } else {
                            throw new Exception("Require issue end time as search range");
                        }
                    } else if ((dueSTime.equals("") && !dueETime.equals("")) || (!dueSTime.equals("") && dueETime.equals(""))) {
                        if (dueSTime.equals("")) {
                            throw new Exception("Require due start time as search range");
                        } else {
                            throw new Exception("Require due end time as search range");
                        }
                    } else {
                        if (!issueSTime.equals("") && !issueETime.equals("")) {
                            issueStartTime = dateString.StringToStartDate(issueSTime);
                            issueEndTime = dateString.StringToEndDate(issueETime);
                            if (issueStartTime.after(issueEndTime)) {
                                throw new Exception("Issue start time should before the end time");
                            }
                        }
                        if (!dueSTime.equals("") && !dueETime.equals("")) {
                            dueStartTime = dateString.StringToStartDate(dueSTime);
                            dueEndTime = dateString.StringToEndDate(dueETime);
                            if (dueStartTime.after(dueEndTime)) {
                                throw new Exception("Due start time should before the end time");
                            }
                        }
                        if (!billno.equals("")) {
                            searchingCriteria.setBillNo(billno);
                        } else {
                            searchingCriteria.setBillNo("");
                        }
                        if (!issueTo.equals("")) {
                            searchingCriteria.setIssueTo(issueTo);
                        } else {
                            searchingCriteria.setIssueTo("");
                        }
                        if (!desc.equals("")) {
                            searchingCriteria.setDesc(desc);
                        } else {
                            searchingCriteria.setDesc("");
                        }
                        if (!issueSTime.equals("") && !issueETime.equals("")) {
                            searchingCriteria.setIssueSTime(issueStartTime);
                            searchingCriteria.setIssueETime(issueEndTime);
                        } else {
                            searchingCriteria.setIssueSTime(getInitIssueStartDate(billManager));
                            searchingCriteria.setIssueETime(getInitIssueEndDate(billManager));
                        }
                        if (!dueSTime.equals("") && !dueETime.equals("")) {
                            searchingCriteria.setDueSTime(dueStartTime);
                            searchingCriteria.setDueETime(dueEndTime);
                        } else {
                            searchingCriteria.setDueSTime(getInitDueStartDate(billManager));
                            searchingCriteria.setDueETime(getInitDueEndDate(billManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("bsearchingCriteria", searchingCriteria);
                        session.setAttribute("billList", billManager.findAllWithParameter(searchingCriteria.getBillNo(), searchingCriteria.getDesc(), searchingCriteria.getIssueTo(),
                                searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getDueSTime(), searchingCriteria.getDueETime(),
                                searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/rental/billListing");
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
                    session.setAttribute("bsearchingCriteria", searchingCriteria);
                    session.setAttribute("billList", billManager.findAllWithParameter(searchingCriteria.getBillNo(), searchingCriteria.getDesc(), searchingCriteria.getIssueTo(),
                            searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getDueSTime(), searchingCriteria.getDueETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/rental/billListing");
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
            session.setAttribute("bsearchingCriteria", new BillSearchingCriteria("0", "", "", "", getInitIssueStartDate(billManager), getInitIssueEndDate(billManager), getInitDueStartDate(billManager), getInitDueEndDate(billManager), "Pending", "b.billNo", "ASC"));
            session.setAttribute("billList", billManager.findAllWithParameter("", "", "", getInitIssueStartDate(billManager), getInitIssueEndDate(billManager), getInitDueStartDate(billManager), getInitDueEndDate(billManager), "Pending", "b.billNo", "ASC"));
            response.sendRedirect("/admin/rental/billListing");
        }
    }

    protected Date getInitIssueStartDate(BillManager billManager) {
        try {
            Date requestStartDate = billManager.findIssueDate().get(0);
            requestStartDate.setDate(requestStartDate.getDate() - 1);
            return requestStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitIssueEndDate(BillManager billManager) {
        try {
            Date requestEndDate = billManager.findIssueDate().get(billManager.findIssueDate().size() - 1);
            requestEndDate.setDate(requestEndDate.getDate() + 1);
            return requestEndDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitDueStartDate(BillManager billManager) {
        try {
            Date updateStartDate = billManager.findDueDate().get(0);
            updateStartDate.setDate(updateStartDate.getDate() - 1);
            return updateStartDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitDueEndDate(BillManager billManager) {
        try {
            Date updateEndDate = billManager.findDueDate().get(billManager.findDueDate().size() - 1);
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
