
package Controller.hosteller.retrievePaymentHisory;

import Controller.Crypto;
import Model.Billitem;
import Model.BillitemManager;
import Model.Receipt;
import Model.ReceiptManager;
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

public class retrieveSelectedReceiptDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();

        try {

            Receipt selectedReceipt = new Receipt();
            ReceiptManager receiptMgr = new ReceiptManager(em);
            List<Billitem> curBillItemList = new ArrayList();
            BillitemManager billItemMgr = new BillitemManager(em);

            if (request.getParameter("rid") == null || request.getParameter("bid") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                selectedReceipt = receiptMgr.findReceiptByID(encrypt.UNDecode(request.getParameter("rid")));
                curBillItemList = billItemMgr.findItemListByBillNo(encrypt.UNDecode(request.getParameter("bid")));
                
                session.setAttribute("curBillItemList", curBillItemList);
                session.setAttribute("selectedReceipt", selectedReceipt);
                response.sendRedirect("/hosteller/rental/viewSelectedReceiptDetails");
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/rental/viewPaymentHistory");
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
