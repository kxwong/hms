package Controller.admin.bookingManagement;

import Controller.Crypto;
import Controller.DateToString;
import Model.FacilitybookingManager;
import Model.FacilitybookingSearchingCriteria;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveBookingListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FacilitybookingManager facilitybookingManager = new FacilitybookingManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            FacilitybookingSearchingCriteria searchingCriteria = (FacilitybookingSearchingCriteria) session.getAttribute("fbsearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new FacilitybookingSearchingCriteria("0", "", "", "", getInitBookStartDate(facilitybookingManager), getInitBookEndDate(facilitybookingManager), getInitUpdateStartDate(facilitybookingManager), getInitUpdateEndDate(facilitybookingManager), "Pending", "f.bookingID", "ASC");
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
                    session.setAttribute("fbsearchingCriteria", searchingCriteria);
                    session.setAttribute("facilityBookingList", facilitybookingManager.findAllWithParameter(searchingCriteria.getBookingID(), searchingCriteria.getFacilityID(), searchingCriteria.getBookedBy(),
                            searchingCriteria.getBookSTime(), searchingCriteria.getBookETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/facility/bookingListing");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String bookingID = request.getParameter("bookingID");
                    String facilityID = request.getParameter("facilityID");
                    String bookSTime = request.getParameter("bookStartTime");
                    String bookETime = request.getParameter("bookEndTime");
                    String bookedBy = request.getParameter("bookedBy");
                    String updateSTime = request.getParameter("updateStartTime");
                    String updateETime = request.getParameter("updateEndTime");
                    Date bookStartTime = null;
                    Date bookEndTime = null;
                    Date updateStartTime = null;
                    Date updateEndTime = null;

                    if (bookingID.equals("") && facilityID.equals("") && bookedBy.equals("") && bookSTime.equals("") && bookETime.equals("") && updateSTime.equals("") && updateETime.equals("")) {
                        throw new Exception("No input for searching");
                    } else if ((bookSTime.equals("") && !bookETime.equals("")) || (!bookSTime.equals("") && bookETime.equals(""))) {
                        if (bookSTime.equals("")) {
                            throw new Exception("Require book start time as search range");
                        } else {
                            throw new Exception("Require book end time as search range");
                        }
                    } else if ((updateSTime.equals("") && !updateETime.equals("")) || (!updateSTime.equals("") && updateETime.equals(""))) {
                        if (updateSTime.equals("")) {
                            throw new Exception("Require update start time as search range");
                        } else {
                            throw new Exception("Require update end time as search range");
                        }
                    } else {
                        if (!bookSTime.equals("") && !bookETime.equals("")) {
                            bookStartTime = dateString.StringToStartDate(bookSTime);
                            bookEndTime = dateString.StringToEndDate(bookETime);
                            if (bookStartTime.after(bookEndTime)) {
                                throw new Exception("Book start time should before the end time");
                            }
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            updateStartTime = dateString.StringToStartDate(updateSTime);
                            updateEndTime = dateString.StringToEndDate(updateETime);
                            if (updateStartTime.after(updateEndTime)) {
                                throw new Exception("Book start time should before the end time");
                            }
                        }
                        if (!bookingID.equals("")) {
                            searchingCriteria.setBookingID(bookingID);
                        } else {
                            searchingCriteria.setBookingID("");
                        }
                        if (!facilityID.equals("")) {
                            searchingCriteria.setFacilityID(facilityID);
                        } else {
                            searchingCriteria.setFacilityID("");
                        }
                        if (!bookedBy.equals("")) {
                            searchingCriteria.setBookedBy(bookedBy);
                        } else {
                            searchingCriteria.setBookedBy("");
                        }
                        if (!bookSTime.equals("") && !bookETime.equals("")) {
                            searchingCriteria.setBookSTime(bookStartTime);
                            searchingCriteria.setBookETime(bookEndTime);
                        } else {
                            searchingCriteria.setBookSTime(getInitBookStartDate(facilitybookingManager));
                            searchingCriteria.setBookETime(getInitBookEndDate(facilitybookingManager));
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            searchingCriteria.setUpdateSTime(updateStartTime);
                            searchingCriteria.setUpdateETime(updateEndTime);
                        } else {
                            searchingCriteria.setUpdateSTime(getInitUpdateStartDate(facilitybookingManager));
                            searchingCriteria.setUpdateETime(getInitUpdateEndDate(facilitybookingManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("fbsearchingCriteria", searchingCriteria);
                        session.setAttribute("facilityBookingList", facilitybookingManager.findAllWithParameter(searchingCriteria.getBookingID(), searchingCriteria.getFacilityID(), searchingCriteria.getBookedBy(),
                                searchingCriteria.getBookSTime(), searchingCriteria.getBookETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                                searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/facility/bookingListing");
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
                    session.setAttribute("fbsearchingCriteria", searchingCriteria);
                    session.setAttribute("facilityBookingList", facilitybookingManager.findAllWithParameter(searchingCriteria.getBookingID(), searchingCriteria.getFacilityID(), searchingCriteria.getBookedBy(),
                            searchingCriteria.getBookSTime(), searchingCriteria.getBookETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/facility/bookingListing");
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
            session.setAttribute("fbsearchingCriteria", new FacilitybookingSearchingCriteria("0", "", "", "", getInitBookStartDate(facilitybookingManager), getInitBookEndDate(facilitybookingManager), getInitUpdateStartDate(facilitybookingManager), getInitUpdateEndDate(facilitybookingManager), "Pending", "f.bookingID", "ASC"));
            session.setAttribute("facilityBookingList", facilitybookingManager.findAllWithParameter("", "", "", getInitBookStartDate(facilitybookingManager), getInitBookEndDate(facilitybookingManager), getInitUpdateStartDate(facilitybookingManager), getInitUpdateEndDate(facilitybookingManager), "Pending", "f.bookingID", "ASC"));
            response.sendRedirect("/admin/facility/bookingListing");
        }
    }

    protected Date getInitBookStartDate(FacilitybookingManager facilitybookingManager) {
        try {
            Date bookStartDate = facilitybookingManager.findBookDate().get(0);
            bookStartDate.setDate(bookStartDate.getDate() - 1);
            return bookStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitBookEndDate(FacilitybookingManager facilitybookingManager) {
        try {
            Date bookEndDate = facilitybookingManager.findBookDate().get(facilitybookingManager.findBookDate().size() - 1);
            bookEndDate.setDate(bookEndDate.getDate() + 1);
            return bookEndDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateStartDate(FacilitybookingManager facilitybookingManager) {
        try {
            Date updateStartDate = facilitybookingManager.findUpdateDate().get(0);
            updateStartDate.setDate(updateStartDate.getDate() - 1);
            return updateStartDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateEndDate(FacilitybookingManager facilitybookingManager) {
        try {
            Date updateEndDate = facilitybookingManager.findUpdateDate().get(facilitybookingManager.findUpdateDate().size() - 1);
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
