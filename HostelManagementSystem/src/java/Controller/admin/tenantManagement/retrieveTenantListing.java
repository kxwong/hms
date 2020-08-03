package Controller.admin.tenantManagement;

import Controller.Crypto;
import Model.HostellerManager;
import Model.HostellerSearchingCriteria;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveTenantListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HostellerManager hostellerManager = new HostellerManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            HostellerSearchingCriteria searchingCriteria = (HostellerSearchingCriteria) session.getAttribute("htsearchingCriteria");
            switch (term) {
                case 2:
                    String name = request.getParameter("name");
                    String room = request.getParameter("room");
                    String email = request.getParameter("email");
                    String contact = request.getParameter("contact");
                    String requestno = request.getParameter("requestno");
                    String status = request.getParameter("status");

                    if (name.equals("") && room.equals("") && email.equals("") && contact.equals("") && requestno.equals("") && status.equals("")) {
                        throw new Exception("No input for searching");
                    } else {
                        if (!name.equals("")) {
                            searchingCriteria.setName(name);
                        } else {
                            searchingCriteria.setName("");
                        }
                        if (!room.equals("")) {
                            searchingCriteria.setRoom(room);
                        } else {
                            searchingCriteria.setRoom("");
                        }
                        if (!email.equals("")) {
                            searchingCriteria.setEmail(email);
                        } else {
                            searchingCriteria.setEmail("");
                        }
                        if (!contact.equals("")) {
                            searchingCriteria.setContactno(contact);
                        } else {
                            searchingCriteria.setContactno("");
                        }
                        if (!requestno.equals("")) {
                            searchingCriteria.setRequestno(requestno);
                        } else {
                            searchingCriteria.setRequestno("");
                        }
                        if (!status.equals("")) {
                            searchingCriteria.setStatus(status);
                        } else {
                            searchingCriteria.setStatus("");
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("htsearchingCriteria", searchingCriteria);
                        session.setAttribute("hostellerList", hostellerManager.findAllWithParameter(searchingCriteria.getName(),
                                searchingCriteria.getRoom(), searchingCriteria.getEmail(), searchingCriteria.getContactno(), searchingCriteria.getRequestno(),
                                searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/admin/hosteller/tenantlisting");
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
                    session.setAttribute("htsearchingCriteria", searchingCriteria);
                    session.setAttribute("hostellerList", hostellerManager.findAllWithParameter(searchingCriteria.getName(),
                            searchingCriteria.getRoom(), searchingCriteria.getEmail(), searchingCriteria.getContactno(), searchingCriteria.getRequestno(),
                            searchingCriteria.getStatus(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/admin/hosteller/tenantlisting");
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
            session.setAttribute("htsearchingCriteria", new HostellerSearchingCriteria("0", "", "", "", "", "", "", "h.firstName, h.middleName, h.lastName", "ASC"));
            session.setAttribute("hostellerList", hostellerManager.findAllWithParameter("", "", "", "", "", "", "h.firstName, h.middleName, h.lastName", "ASC"));
            response.sendRedirect("/admin/hosteller/tenantlisting");
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
