package Controller.admin.issueManagement;

import Controller.Crypto;
import Controller.DateToString;
import Model.IssueManager;
import Model.IssueSearchingCriteria;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveIssueListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        IssueManager issueManager = new IssueManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            IssueSearchingCriteria searchingCriteria = (IssueSearchingCriteria) session.getAttribute("isearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new IssueSearchingCriteria("0", "", "", "", "", getInitIssueStartDate(issueManager), getInitIssueEndDate(issueManager), getInitUpdateStartDate(issueManager), getInitUpdateEndDate(issueManager), "Processing", "i.caseNo", "ASC");
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
                    session.setAttribute("isearchingCriteria", searchingCriteria);
                    session.setAttribute("issueList", issueManager.findAllWithParameter(searchingCriteria.getCaseno(), searchingCriteria.getTitle(), searchingCriteria.getCategory(), searchingCriteria.getIssue(),
                            searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/issue/issueListing");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String caseno = request.getParameter("caseno");
                    String title = request.getParameter("title");
                    String category = request.getParameter("category");
                    String issue = request.getParameter("issue");
                    String issueSTime = request.getParameter("issueStartTime");
                    String issueETime = request.getParameter("issueEndTime");
                    String updateSTime = request.getParameter("updateStartTime");
                    String updateETime = request.getParameter("updateEndTime");
                    Date issueStartTime = null;
                    Date issueEndTime = null;
                    Date updateStartTime = null;
                    Date updateEndTime = null;

                    if (caseno.equals("") && title.equals("") && category.equals("") && issue.equals("") && issueSTime.equals("") && issueETime.equals("") && updateSTime.equals("") && updateETime.equals("")) {
                        throw new Exception("No input for searching");
                    } else if ((issueSTime.equals("") && !issueETime.equals("")) || (!issueSTime.equals("") && issueETime.equals(""))) {
                        if (issueSTime.equals("")) {
                            throw new Exception("Require issue start time as search range");
                        } else {
                            throw new Exception("Require issue end time as search range");
                        }
                    } else if ((updateSTime.equals("") && !updateETime.equals("")) || (!updateSTime.equals("") && updateETime.equals(""))) {
                        if (updateSTime.equals("")) {
                            throw new Exception("Require update start time as search range");
                        } else {
                            throw new Exception("Require update end time as search range");
                        }
                    } else {
                        if (!issueSTime.equals("") && !issueETime.equals("")) {
                            issueStartTime = dateString.StringToStartDate(issueSTime);
                            issueEndTime = dateString.StringToEndDate(issueETime);
                            if (issueStartTime.after(issueEndTime)) {
                                throw new Exception("Issue start time should before the end time");
                            }
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            updateStartTime = dateString.StringToStartDate(updateSTime);
                            updateEndTime = dateString.StringToEndDate(updateETime);
                            if (updateStartTime.after(updateEndTime)) {
                                throw new Exception("Update start time should before the end time");
                            }
                        }
                        if (!caseno.equals("")) {
                            searchingCriteria.setCaseno(caseno);
                        } else {
                            searchingCriteria.setCaseno("");
                        }
                        if (!title.equals("")) {
                            searchingCriteria.setTitle(title);
                        } else {
                            searchingCriteria.setTitle("");
                        }
                        if (!category.equals("")) {
                            searchingCriteria.setCategory(category);
                        } else {
                            searchingCriteria.setCategory("");
                        }
                        if (!issue.equals("")) {
                            searchingCriteria.setIssue(issue);
                        } else {
                            searchingCriteria.setIssue("");
                        }
                        if (!issueSTime.equals("") && !issueETime.equals("")) {
                            searchingCriteria.setIssueSTime(issueStartTime);
                            searchingCriteria.setIssueETime(issueEndTime);
                        } else {
                            searchingCriteria.setIssueSTime(getInitIssueStartDate(issueManager));
                            searchingCriteria.setIssueETime(getInitIssueEndDate(issueManager));
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            searchingCriteria.setUpdateSTime(updateStartTime);
                            searchingCriteria.setUpdateETime(updateEndTime);
                        } else {
                            searchingCriteria.setUpdateSTime(getInitUpdateStartDate(issueManager));
                            searchingCriteria.setUpdateETime(getInitUpdateEndDate(issueManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("isearchingCriteria", searchingCriteria);
                        session.setAttribute("issueList", issueManager.findAllWithParameter(searchingCriteria.getCaseno(), searchingCriteria.getTitle(), searchingCriteria.getCategory(), searchingCriteria.getIssue(),
                                searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                                searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/issue/issueListing");
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
                    session.setAttribute("isearchingCriteria", searchingCriteria);
                    session.setAttribute("issueList", issueManager.findAllWithParameter(searchingCriteria.getCaseno(), searchingCriteria.getTitle(), searchingCriteria.getCategory(), searchingCriteria.getIssue(),
                            searchingCriteria.getIssueSTime(), searchingCriteria.getIssueETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/issue/issueListing");
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
            session.setAttribute("isearchingCriteria", new IssueSearchingCriteria("0", "", "", "", "", getInitIssueStartDate(issueManager), getInitIssueEndDate(issueManager), getInitUpdateStartDate(issueManager), getInitUpdateEndDate(issueManager), "Processing", "i.caseNo", "ASC"));
            session.setAttribute("issueList", issueManager.findAllWithParameter("", "", "", "", getInitIssueStartDate(issueManager), getInitIssueEndDate(issueManager), getInitUpdateStartDate(issueManager), getInitUpdateEndDate(issueManager), "Processing", "i.caseNo", "ASC"));
            response.sendRedirect("/admin/issue/issueListing");
        }
    }

    protected Date getInitIssueStartDate(IssueManager issueManager) {
        try {
            Date requestStartDate = issueManager.findIssueDate().get(0);
            requestStartDate.setDate(requestStartDate.getDate() - 1);
            return requestStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitIssueEndDate(IssueManager issueManager) {
        try {
            Date requestEndDate = issueManager.findIssueDate().get(issueManager.findIssueDate().size() - 1);
            requestEndDate.setDate(requestEndDate.getDate() + 1);
            return requestEndDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateStartDate(IssueManager issueManager) {
        try {
            Date updateStartDate = issueManager.findUpdateDate().get(0);
            updateStartDate.setDate(updateStartDate.getDate() - 1);
            return updateStartDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateEndDate(IssueManager issueManager) {
        try {
            Date updateEndDate = issueManager.findUpdateDate().get(issueManager.findUpdateDate().size() - 1);
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
