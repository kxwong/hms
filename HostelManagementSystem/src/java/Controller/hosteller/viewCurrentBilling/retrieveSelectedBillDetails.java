
package Controller.hosteller.viewCurrentBilling;

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

public class retrieveSelectedBillDetails extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();
        
        try {

            Bill selectedBill = new Bill();
            BillManager billMgr = new BillManager(em);
            List<Billitem> billItemList = new ArrayList<Billitem>();
            BillitemManager billItemMgr = new BillitemManager(em);           
            
            String billNo = encrypt.UNDecode(request.getParameter("bno"));
            selectedBill = billMgr.findBillByID(billNo);
            if(selectedBill == null){
                throw new Exception("An error has occured, please try again.");
            }else{   
                billItemList = billItemMgr.findItemListByBillNo(selectedBill.getBillNo());  
            }
            session.setAttribute("selectedBill", selectedBill);
            session.setAttribute("billItemList", billItemList);
            response.sendRedirect("/hosteller/rental/viewSelectedBillingDetails");

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/rental/viewCurrentBilling");
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
