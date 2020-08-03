package Controller.admin.roomAllocation;

import Controller.Crypto;
import Controller.DateToString;
import Model.RoombookingManager;
import Model.RoombookingSearchingCriteria;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveRoomRequestListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RoombookingManager roomBookingManager = new RoombookingManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            RoombookingSearchingCriteria searchingCriteria = (RoombookingSearchingCriteria) session.getAttribute("rbsearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new RoombookingSearchingCriteria("0", "", "", getInitReqStartDate(roomBookingManager), getInitReqEndDate(roomBookingManager), "", "", getInitUpdateStartDate(roomBookingManager), getInitUpdateEndDate(roomBookingManager), "Pending", "r.requestNo", "ASC");
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
                    session.setAttribute("rbsearchingCriteria", searchingCriteria);
                    session.setAttribute("roomBookingList", roomBookingManager.findAllWithParameter(searchingCriteria.getRequestNo(), searchingCriteria.getRequestType(),
                            searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getRequestRoom(), searchingCriteria.getRequestBillNo(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/hostel/roomrequestlisting");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String requestNo = request.getParameter("requestNo");
                    String requestType = request.getParameter("requestType");
                    String requestSTime = request.getParameter("requestSTime");
                    String requestETime = request.getParameter("requestETime");
                    String requestRoom = request.getParameter("requestRoom");
                    String requestBillNo = request.getParameter("requestBillNo");
                    String updateSTime = request.getParameter("updateSTime");
                    String updateETime = request.getParameter("updateETime");
                    Date requestStartTime = null;
                    Date requestEndTime = null;
                    Date updateStartTime = null;
                    Date updateEndTime = null;

                    if (requestNo.equals("") && requestType.equals("") && requestRoom.equals("") && requestSTime.equals("") && requestETime.equals("") && requestBillNo.equals("") && updateSTime.equals("") && updateETime.equals("")) {
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
                        if (!requestType.equals("")) {
                            searchingCriteria.setRequestType(requestType);
                        } else {
                            searchingCriteria.setRequestType("");
                        }
                        if (!requestRoom.equals("")) {
                            searchingCriteria.setRequestRoom(requestRoom);
                        } else {
                            searchingCriteria.setRequestRoom("");
                        }
                        if (!requestSTime.equals("") && !requestETime.equals("")) {
                            searchingCriteria.setRequestSTime(requestStartTime);
                            searchingCriteria.setRequestETime(requestEndTime);
                        } else {
                            searchingCriteria.setRequestSTime(getInitReqStartDate(roomBookingManager));
                            searchingCriteria.setRequestETime(getInitReqEndDate(roomBookingManager));
                        }
                        if (!requestBillNo.equals("")) {
                            searchingCriteria.setRequestBillNo(requestBillNo);
                        } else {
                            searchingCriteria.setRequestBillNo("");
                        }
                        if (!updateSTime.equals("") && !updateETime.equals("")) {
                            searchingCriteria.setUpdateSTime(updateStartTime);
                            searchingCriteria.setUpdateETime(updateEndTime);
                        } else {
                            searchingCriteria.setUpdateSTime(getInitUpdateStartDate(roomBookingManager));
                            searchingCriteria.setUpdateETime(getInitUpdateEndDate(roomBookingManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("rbsearchingCriteria", searchingCriteria);
                        session.setAttribute("roomBookingList", roomBookingManager.findAllWithParameter(searchingCriteria.getRequestNo(), searchingCriteria.getRequestType(),
                                searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                                searchingCriteria.getRequestRoom(), searchingCriteria.getRequestBillNo(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/hostel/roomrequestlisting");
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
                    session.setAttribute("rbsearchingCriteria", searchingCriteria);
                    session.setAttribute("roomBookingList", roomBookingManager.findAllWithParameter(searchingCriteria.getRequestNo(), searchingCriteria.getRequestType(),
                            searchingCriteria.getRequestSTime(), searchingCriteria.getRequestETime(), searchingCriteria.getUpdateSTime(), searchingCriteria.getUpdateETime(),
                            searchingCriteria.getRequestRoom(), searchingCriteria.getRequestBillNo(), searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/hostel/roomrequestlisting");
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
            session.setAttribute("rbsearchingCriteria", new RoombookingSearchingCriteria("0", "", "", getInitReqStartDate(roomBookingManager), getInitReqEndDate(roomBookingManager), "", "", getInitUpdateStartDate(roomBookingManager), getInitUpdateEndDate(roomBookingManager), "Pending", "r.requestNo", "ASC"));
            session.setAttribute("roomBookingList", roomBookingManager.findAllWithParameter("", "", getInitReqStartDate(roomBookingManager), getInitReqEndDate(roomBookingManager), getInitUpdateStartDate(roomBookingManager), getInitUpdateEndDate(roomBookingManager), "", "", "Pending", "r.requestNo", "ASC"));
            response.sendRedirect("/admin/hostel/roomrequestlisting");
        }
    }

    protected Date getInitReqStartDate(RoombookingManager roomBookingManager) {
        try {
            Date requestStartDate = roomBookingManager.findRequestDate().get(0);
            requestStartDate.setDate(requestStartDate.getDate() - 1);
            return requestStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitReqEndDate(RoombookingManager roomBookingManager) {
        try {
            Date requestEndDate = roomBookingManager.findRequestDate().get(roomBookingManager.findRequestDate().size() - 1);
            requestEndDate.setDate(requestEndDate.getDate() + 1);
            return requestEndDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateStartDate(RoombookingManager roomBookingManager) {
        try {
            Date updateStartDate = roomBookingManager.findUpdateDate().get(0);
            updateStartDate.setDate(updateStartDate.getDate() - 1);
            return updateStartDate;
        } catch (Exception ex) {
            return null;
        }
    }

    protected Date getInitUpdateEndDate(RoombookingManager roomBookingManager) {
        try {
            Date updateEndDate = roomBookingManager.findUpdateDate().get(roomBookingManager.findRequestDate().size() - 1);
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
