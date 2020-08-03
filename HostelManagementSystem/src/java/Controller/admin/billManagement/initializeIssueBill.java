package Controller.admin.billManagement;

import Controller.DateToString;
import Model.BillManager;
import Model.Hosteller;
import Model.HostellerManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class initializeIssueBill extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            HostellerManager hostellerManager = new HostellerManager(mgr);
            List<Hosteller> tempoHostellerList = hostellerManager.findAll();
            List<Hosteller> hostellerList = new ArrayList<>();
            for (int i = 0; i < tempoHostellerList.size(); i++) {
                try {
                    if (tempoHostellerList.get(i).getStayRoom() != null) {
                        hostellerList.add(tempoHostellerList.get(i));
                    }
                } catch (Exception ex) {
                }
            }
            BillManager billManager = new BillManager(mgr);
            DateToString dateString = new DateToString();
            List<Hosteller> issuedHostellerList = new ArrayList<>();
            Date today = new Date();
            today.setMonth(today.getMonth() + 1);
            for (int i = 0; i < billManager.findAll().size(); i++) {
                String ref = dateString.ToStringMonth(today.getMonth()) + " " + String.valueOf(today.getYear() + 1900);
                if (billManager.findAll().get(i).getDescription().contains(ref)) {
                    issuedHostellerList.add(billManager.findAll().get(i).getIssueTo());
                }
            }
            if (hostellerList.isEmpty()) {
                throw new Exception("There are none of the hosteller own a room currently");
            } else if (issuedHostellerList.size() == hostellerList.size()) {
                throw new Exception("You already issued the next month rental bill");
            } else {
                if (!issuedHostellerList.isEmpty()) {
                    for (int i = 0; i < issuedHostellerList.size(); i++) {
                        hostellerList.remove(issuedHostellerList.get(i));
                    }
                }
                session.setAttribute("issueList", hostellerList);
                session.setAttribute("issueDate", today);
                response.sendRedirect("/admin/rental/issueBill");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveBillListing?t=0");
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
