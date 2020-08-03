
package Controller.hosteller.viewCurrentBilling;

import Controller.Crypto;
import Model.Bill;
import Model.BillManager;
import Model.Hosteller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveCurrentBillingList extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        BillManager billMgr = new BillManager(em);
        Crypto encrypt = new Crypto();

        try {
            Hosteller curHosteller = new Hosteller();
            List<Bill> billList = new ArrayList<Bill>();
            List<Bill> tempoBillList = new ArrayList<Bill>();

            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                curHosteller = (Hosteller) session.getAttribute("curHosteller");
                if (request.getParameter("ob") != null && request.getParameter("seq") != null) {
                    String orderBy = encrypt.UNDecode(request.getParameter("ob"));
                    String seq = encrypt.UNDecode(request.getParameter("seq"));
                    seq = seq.toUpperCase();

                    tempoBillList = em.createQuery("SELECT b FROM Bill b WHERE b.issueTo = :issueTo " + "ORDER BY " + orderBy + " " + seq).setParameter("issueTo", curHosteller).getResultList();
                    session.setAttribute("seq", seq.toLowerCase());
                } else {
                    tempoBillList = billMgr.findAllByHosteller(curHosteller);
                }

                for (int i = 0; i < tempoBillList.size(); i++) {
                    if (tempoBillList.get(i).getPaidDate() == null) {
                        billList.add(tempoBillList.get(i));
                    }
                }

                session.setAttribute("billList", billList);
                response.sendRedirect("/hosteller/rental/viewCurrentBilling");

            }

        } catch (Exception ex) {
            response.sendRedirect("/retrieveCurrentRoomStatus");
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
