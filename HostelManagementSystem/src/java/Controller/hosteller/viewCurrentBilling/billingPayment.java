package Controller.hosteller.viewCurrentBilling;

import Controller.DateToString;
import Model.Bill;
import Model.BillManager;
import Model.Billitem;
import Model.BillitemManager;
import Model.Hosteller;
import Model.Receipt;
import Model.ReceiptManager;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class billingPayment extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            BillManager billMgr = new BillManager(em);
            Hosteller curHosteller = new Hosteller();
            Date curDate = new Date();

            if ((Bill) session.getAttribute("selectedBill") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                Bill selectedBill = (Bill) session.getAttribute("selectedBill");
                String bid = selectedBill.getBillNo();
                int overdueDay = (Integer) session.getAttribute("overdueDay");
                selectedBill = billMgr.findBillByID(bid);
                String status = "Paid";
                if (overdueDay > 0) {
                    BillitemManager billItemManager = new BillitemManager(em);
                    Billitem penalty = new Billitem();
                    penalty.setDescription("Overdue penalty for " + overdueDay + " day");
                    penalty.setFee(BigDecimal.valueOf((overdueDay / 7 + 1) * 50));
                    selectedBill.setTotalAmount(selectedBill.getTotalAmount().add(penalty.getFee()));

                    penalty.setBillItemNo(billItemManager.generateID());
                    penalty.setBillNo(selectedBill);
                    status = "Overdue";
                    try {
                        utx.begin();
                        billItemManager.addBillitem(penalty);
                        utx.commit();
                    } catch (Exception ex) {
                        throw new Exception("Issue overdue item fail");
                    }
                }
                curHosteller = (Hosteller) session.getAttribute("curHosteller");
                selectedBill.setPaidDate(curDate);
                selectedBill.setStatus(status);
                utx.begin();
                boolean successBill = billMgr.updateBill(selectedBill);
                utx.commit();
                if (RoomBookingFromBill(selectedBill) != null) {
                    updateRoombookingDetails(RoomBookingFromBill(selectedBill), "Paid");
                }
                issueReceipt(selectedBill);
                List<Bill> billList = billMgr.findAllExceptStatusByHosteller(curHosteller, "Paid");

                if (successBill == true) {
                    session.setAttribute("successMsg", "Your bill has been paid successfully.");
                    response.sendRedirect("/retrieveCurrentBillingList");
                } else {
                    throw new Exception("Payment failed, please try again.");
                }
            }
        } catch (Exception ex) {
            session.setAttribute("errMsg", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveCurrentBillingList");
        }
    }

    protected Roombooking RoomBookingFromBill(Bill bill) throws Exception {
        RoombookingManager roombookingManager = new RoombookingManager(em);
        return roombookingManager.findByBillNo(bill.getBillNo());
    }

    protected void updateRoombookingDetails(Roombooking roomBooking, String status) throws Exception {
        RoombookingManager roomBookingManager = new RoombookingManager(em);
        roomBooking.setStatus(status);
        roomBooking.setUpdateDate(new Date());
        try {
            utx.begin();
            roomBookingManager.updateRoombookingDetails(roomBooking);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
    }

    protected void issueReceipt(Bill bill) throws Exception {
        ReceiptManager receiptManager = new ReceiptManager(em);
        BillManager billManager = new BillManager(em);

        Receipt receipt = new Receipt();
        receipt.setReceiptNo(receiptManager.generateID(new Date()));
        receipt.setDescription("Payment of " + bill.getDescription());
        receipt.setGenerateDate(new Date());
        receipt.setPaidDate(bill.getPaidDate());
        receipt.setBill(bill);
        try {
            utx.begin();
            receiptManager.addReceipt(receipt);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }

        bill.setStatus("Paid");
        bill.setReceiptNo(receipt);

        try {
            utx.begin();
            billManager.updateBillStatus(bill);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
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
