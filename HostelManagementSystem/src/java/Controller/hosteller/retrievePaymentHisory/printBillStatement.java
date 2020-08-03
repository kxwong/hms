
package Controller.hosteller.retrievePaymentHisory;

import Controller.Crypto;
import Model.Bill;
import Model.BillManager;
import Model.Billitem;
import Model.BillitemManager;
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

public class printBillStatement extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {

            Bill curBill = new Bill();
            List<Billitem> curBillItems = new ArrayList();
            BillManager billMgr = new BillManager(em);
            BillitemManager billitemMgr = new BillitemManager(em);

            
            if (request.getParameter("bid") == null) {
                throw new Exception("An error has occured.");
            } else {
                String bidE = request.getParameter("bid");
                String bid = encrypt.UNDecode(bidE);
                
                
                curBill = billMgr.findBillByID(bid);               
                curBillItems = billitemMgr.findItemListByBillNo(curBill.getBillNo());
                
                session.setAttribute("curBill", curBill);
                session.setAttribute("curBillItems", curBillItems);
                response.sendRedirect("/hosteller/rental/billStatement");
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/rental/viewSelectedReceiptDetails");
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
