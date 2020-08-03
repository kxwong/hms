package Controller.admin.billManagement;

import Controller.Crypto;
import Controller.DateToString;
import Controller.Email;
import Model.Bill;
import Model.BillManager;
import Model.Billitem;
import Model.BillitemManager;
import Model.Receipt;
import Model.ReceiptManager;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateBill extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String status = (String) request.getParameter("status");
            String remark = (String) request.getParameter("remark");
            if (remark == null) {
                remark = "";
            }
            BillManager billManager = new BillManager(mgr);
            Bill bill;
            if (billManager.findBillByID(((Bill) session.getAttribute("bill")).getBillNo()) != null) {
                bill = billManager.findBillByID(((Bill) session.getAttribute("bill")).getBillNo());
                if (bill.getStatus().toLowerCase().equals("pending")) {
                    switch (status.toLowerCase()) {
                        case "cancelled":
                            bill.setStatus("Cancelled");
                            bill.setRemark(remark);
                            utx.begin();
                            billManager.updateBillStatus(bill);
                            utx.commit();
                            break;
                        default:
                            throw new Exception("Update fail. Status can not update to " + status);
                    }
                } 
                Email email = new Email(bill.getIssueTo().getLastName(),bill.getIssueTo().getContact().getEmail(),"\n\nYour bill status has been updated. Please log in to view the latest status.");
                session.setAttribute("success", "Bill is updated successfully");
                response.sendRedirect("/retrieveBillDetails?p=" + crypto.BEncode(bill.getBillNo()));
            } else {
                throw new Exception("Update fail. Unable to find bill details.");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveBillDetails?p=" + crypto.BEncode(((Bill) session.getAttribute("bill")).getBillNo()));
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
