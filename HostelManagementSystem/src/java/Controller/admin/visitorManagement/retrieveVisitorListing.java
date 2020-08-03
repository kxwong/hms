package Controller.admin.visitorManagement;

import Controller.Crypto;
import Model.VisitorManager;
import Model.VisitorSearchingCriteria;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveVisitorListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VisitorManager visitorManager = new VisitorManager(mgr);
        HttpSession session = request.getSession();
        try {
            Crypto crypto = new Crypto();
            int term = 4;
            try {
                term = Integer.parseInt(request.getParameter("t"));
            } catch (Exception ex) {
            }
            VisitorSearchingCriteria searchingCriteria = (VisitorSearchingCriteria) session.getAttribute("vsearchingCriteria");
            switch (term) {
                case 2:
                    String visitorID = request.getParameter("visitorID");
                    String visitorName = request.getParameter("visitorName");
                    String visitorIC = request.getParameter("visitorIC");

                    if (visitorID.equals("") && visitorName.equals("") && visitorIC.equals("")) {
                        throw new Exception("No input for searching");
                    } else {
                        if (!visitorID.equals("")) {
                            searchingCriteria.setVisitorID(visitorID);
                        } else {
                            searchingCriteria.setVisitorID("");
                        }
                        if (!visitorName.equals("")) {
                            searchingCriteria.setVisitorName(visitorName);
                        } else {
                            searchingCriteria.setVisitorName("");
                        }
                        if (!visitorIC.equals("")) {
                            searchingCriteria.setVisitorIC(visitorIC);
                        } else {
                            searchingCriteria.setVisitorIC("");
                        }
                        searchingCriteria.setPreviousTerm("2");
                        session.setAttribute("vsearchingCriteria", searchingCriteria);
                        session.setAttribute("visitorList", visitorManager.findAllWithParameter(searchingCriteria.getVisitorID(),
                                searchingCriteria.getVisitorName(), searchingCriteria.getVisitorIC(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                        response.sendRedirect("/guard/visitor/visitorlisting");
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
                    session.setAttribute("vsearchingCriteria", searchingCriteria);
                    session.setAttribute("visitorList", visitorManager.findAllWithParameter(searchingCriteria.getVisitorID(),
                            searchingCriteria.getVisitorName(), searchingCriteria.getVisitorIC(), searchingCriteria.getOrderParameter(), searchingCriteria.getOrder()));
                    response.sendRedirect("/guard/visitor/visitorlisting");
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
            session.setAttribute("vsearchingCriteria", new VisitorSearchingCriteria("0", "", "", "", "v.visitorID", "ASC"));
            session.setAttribute("visitorList", visitorManager.findAllWithParameter("", "", "", "v.visitorID", "ASC"));
            response.sendRedirect("/guard/visitor/visitorlisting");
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
