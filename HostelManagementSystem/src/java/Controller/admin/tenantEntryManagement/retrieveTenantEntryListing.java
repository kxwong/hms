package Controller.admin.tenantEntryManagement;

import Controller.Crypto;
import Controller.DateToString;
import Model.EntrecordManager;
import Model.EntryRecordSearchingCriteria;
import java.io.IOException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveTenantEntryListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntrecordManager entryRecordManager = new EntrecordManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            EntryRecordSearchingCriteria searchingCriteria = (EntryRecordSearchingCriteria) session.getAttribute("tsearchingCriteria");
            switch (term) {
                case 1:
                    searchingCriteria = new EntryRecordSearchingCriteria("0", "H", "", "", "", "", getInitStartDate(entryRecordManager), getInitEndDate(entryRecordManager), "Entry", "e.entRecordID", "ASC");
                    String gate;
                    try {
                        String enGate = request.getParameter("s");
                        gate = crypto.CDecode(enGate);
                    } catch (Exception ex) {
                        throw new Exception("Selected gate is not existed");
                    }
                    if (gate.equals("All")) {
                        searchingCriteria.setGate("");
                    } else {
                        searchingCriteria.setGate(gate);
                    }
                    searchingCriteria.setPreviousTerm("1");
                    session.setAttribute("tsearchingCriteria", searchingCriteria);
                    session.setAttribute("tenantEntryRecordList", entryRecordManager.findAllWithParameter(searchingCriteria.getEntHolder(), searchingCriteria.getEntID(), searchingCriteria.getTenantName(),
                            searchingCriteria.getLocation(), searchingCriteria.getBuilding(), searchingCriteria.getEntSTime(), searchingCriteria.getEntETime(),
                            searchingCriteria.getGate(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/entry/tenantEntryListing");
                    break;
                case 2:
                    DateToString dateString = new DateToString();
                    String entID = request.getParameter("entID");
                    String tenantName = request.getParameter("tenantName");
                    String location = request.getParameter("location");
                    String building = request.getParameter("building");
                    String sDate = request.getParameter("startDate");
                    String eDate = request.getParameter("endDate");
                    Date startTime = null;
                    Date endTime = null;

                    if (entID.equals("") && tenantName.equals("") && sDate.equals("") && eDate.equals("") && location.equals("") && building.equals("")) {
                        throw new Exception("No input for searching");
                    } else if ((sDate.equals("") && !eDate.equals("")) || (!sDate.equals("") && eDate.equals(""))) {
                        if (sDate.equals("")) {
                            throw new Exception("Require start date as search range");
                        } else {
                            throw new Exception("Require end date as search range");
                        }
                    } else {
                        if (!sDate.equals("") && !eDate.equals("")) {
                            startTime = dateString.DateTimeStringToDate(sDate);
                            endTime = dateString.DateTimeStringToDate(eDate);
                            if (startTime.after(endTime)) {
                                throw new Exception("Start date should before the end date");
                            }
                        }
                        if (!entID.equals("")) {
                            searchingCriteria.setEntID(entID);
                        } else {
                            searchingCriteria.setEntID("");
                        }
                        if (!tenantName.equals("")) {
                            searchingCriteria.setTenantName(tenantName);
                        } else {
                            searchingCriteria.setTenantName("");
                        }
                        if (!location.equals("")) {
                            searchingCriteria.setLocation(location);
                        } else {
                            searchingCriteria.setLocation("");
                        }
                        if (!building.equals("")) {
                            searchingCriteria.setBuilding(building);
                        } else {
                            searchingCriteria.setBuilding("");
                        }
                        if (!sDate.equals("") && !eDate.equals("")) {
                            searchingCriteria.setEntSTime(startTime);
                            searchingCriteria.setEntETime(endTime);
                        } else {
                            searchingCriteria.setEntSTime(getInitStartDate(entryRecordManager));
                            searchingCriteria.setEntETime(getInitEndDate(entryRecordManager));
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("tsearchingCriteria", searchingCriteria);
                        session.setAttribute("tenantEntryRecordList", entryRecordManager.findAllWithParameter(searchingCriteria.getEntHolder(), searchingCriteria.getEntID(), searchingCriteria.getTenantName(),
                                searchingCriteria.getLocation(), searchingCriteria.getBuilding(), searchingCriteria.getEntSTime(), searchingCriteria.getEntETime(),
                                searchingCriteria.getGate(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/entry/tenantEntryListing");
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
                    session.setAttribute("tsearchingCriteria", searchingCriteria);
                    session.setAttribute("tenantEntryRecordList", entryRecordManager.findAllWithParameter(searchingCriteria.getEntHolder(), searchingCriteria.getEntID(), searchingCriteria.getTenantName(),
                            searchingCriteria.getLocation(), searchingCriteria.getBuilding(), searchingCriteria.getEntSTime(), searchingCriteria.getEntETime(),
                            searchingCriteria.getGate(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/entry/tenantEntryListing");
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
            session.setAttribute("tsearchingCriteria", new EntryRecordSearchingCriteria("0", "H", "", "", "", "", getInitStartDate(entryRecordManager), getInitEndDate(entryRecordManager), "Entry", "e.entRecordID", "ASC"));
            session.setAttribute("tenantEntryRecordList", entryRecordManager.findAllWithParameter("H", "", "", "", "", getInitStartDate(entryRecordManager), getInitEndDate(entryRecordManager), "Entry", "e.entRecordID", "ASC"));
            response.sendRedirect("/admin/entry/tenantEntryListing");
        }
    }

    protected Date getInitStartDate(EntrecordManager entRecordManager) {
        try {
            Date requestStartDate = entRecordManager.findDate().get(0);
            requestStartDate.setDate(requestStartDate.getDate() - 1);
            return requestStartDate;
        } catch (Exception ex) {
            return null;
        }

    }

    protected Date getInitEndDate(EntrecordManager entRecordManager) {
        try {
            Date requestEndDate = entRecordManager.findDate().get(entRecordManager.findDate().size() - 1);
            requestEndDate.setDate(requestEndDate.getDate() + 1);
            return requestEndDate;
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
