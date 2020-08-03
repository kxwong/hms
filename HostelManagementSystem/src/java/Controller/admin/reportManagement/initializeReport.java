package Controller.admin.reportManagement;

import Controller.DateToString;
import Model.Bill;
import Model.BillManager;
import Model.Entrecord;
import Model.EntrecordManager;
import Model.Facility;
import Model.FacilityManager;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Issue;
import Model.IssueManager;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class initializeReport extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String type = request.getParameter("t");
        String time = request.getParameter("d");
        DateToString dateString = new DateToString();
        String returnUrl = "/retrieveFloorplanListing";
        try {
            switch (type) {
                case "1":
                    try {
                        if (dateString.IsMonthYearValid(time)) {
                            FacilitybookingManager fbManager = new FacilitybookingManager(mgr);
                            List<Facilitybooking> bookingList = fbManager.findAllByDateRange(dateString.ToStartMonth(time), dateString.ToEndMonth(time));
                            if (bookingList.isEmpty()) {
                                throw new Exception("No facility booking at " + time);
                            }
                            session.setAttribute("bookingList", bookingList);
                            returnUrl = "/admin/report/facilityBooking/transaction";
                        } else {
                            throw new Exception("Invalid format of month and year");
                        }
                    } catch (Exception ex) {
                        returnUrl = "/retrieveBookingListing?t=0";
                        throw ex;
                    }
                    break;
                case "2":
                    try {
                        if (dateString.IsYearValid(time)) {
                            FacilityManager facilityManager = new FacilityManager(mgr);
                            FacilitybookingManager fbManager = new FacilitybookingManager(mgr);
                            List<Facility> facilityList = facilityManager.findAll();
                            List<String> locationList = facilityManager.findAllLocation();
                            List<Facilitybooking> bookingList = fbManager.findAllByDateRange(dateString.ToStartYear(time), dateString.ToEndYear(time));
                            if (bookingList.isEmpty()) {
                                throw new Exception("No facility booking at " + time);
                            }
                            session.setAttribute("time", time);
                            session.setAttribute("facilityList", facilityList);
                            session.setAttribute("locationList", locationList);
                            returnUrl = "/admin/report/facilityBooking/summary";
                        } else {
                            throw new Exception("Invalid format of year");
                        }
                    } catch (Exception ex) {
                        returnUrl = "/retrieveBookingListing?t=0";
                        throw ex;
                    }
                    break;
                case "3":
                    try {
                        if (dateString.IsMonthYearValid(time)) {
                            IssueManager issueManager = new IssueManager(mgr);
                            List<String> catList = issueManager.findCategory();
                            List<Issue> issueList = issueManager.findAllByDateRange(dateString.ToStartMonth(time), dateString.ToEndMonth(time));
                            if (issueList.isEmpty()) {
                                throw new Exception("No issue reported at " + time);
                            }
                            session.setAttribute("time", time);
                            session.setAttribute("catList", catList);
                            returnUrl = "/admin/report/issue/transaction";
                        } else {
                            throw new Exception("Invalid format of month and year");
                        }
                    } catch (Exception ex) {
                        returnUrl = "/retrieveIssueListing?t=0";
                        throw ex;
                    }
                    break;
                case "4":
                    try {
                        if (dateString.IsYearValid(time)) {
                            BillManager billManager = new BillManager(mgr);
                            List<Bill> billList = billManager.findAllByDateRange(dateString.ToStartYear(time), dateString.ToEndYear(time));
                            if (billList.isEmpty()) {
                                throw new Exception("No bill issued at " + time);
                            }
                            session.setAttribute("time", time);
                            returnUrl = "/admin/report/rental/summary";
                        } else {
                            throw new Exception("Invalid format of year");
                        }
                    } catch (Exception ex) {
                        returnUrl = "/retrieveBillListing?t=0";
                        throw ex;
                    }
                    break;
                case "5":
                    try {
                        if (dateString.IsMonthYearValid(time)) {
                            EntrecordManager entryRecordManager = new EntrecordManager(mgr);
                            List<Entrecord> entRecordList = entryRecordManager.findAllByDateRange(dateString.ToStartMonth(time), dateString.ToEndMonth(time));
                            if (entRecordList.isEmpty()) {
                                throw new Exception("No entry record at " + time);
                            }
                            session.setAttribute("entRecordList", entRecordList);
                            returnUrl = "/admin/report/entry/transaction";
                        } else {
                            throw new Exception("Invalid format of month and year");
                        }
                    } catch (Exception ex) {
                        returnUrl = "/retrieveTenantEntryListing?t=0";
                        throw ex;
                    }
                    break;
                default:
                    throw new Exception("Fail to generate report.");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
        }
        response.sendRedirect(returnUrl);
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
