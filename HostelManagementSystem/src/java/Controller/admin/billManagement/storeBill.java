package Controller.admin.billManagement;

import Controller.DateToString;
import Controller.Email;
import Model.Bill;
import Model.BillManager;
import Model.Billitem;
import Model.BillitemManager;
import Model.Hosteller;
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

public class storeBill extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            List<Hosteller> hostellerList = (List) session.getAttribute("issueList");
            int count = 0;
            for (int i = 0; i < hostellerList.size(); i++) {
                try {
                    if (((String) request.getParameter(hostellerList.get(i).getHostellerID())).equals("on")) {
                        count++;
                    }
                } catch (Exception ex) {
                }
            }
            if (count > 0) {
                for (int i = 0; i < hostellerList.size(); i++) {
                    try {
                        if (((String) request.getParameter(hostellerList.get(i).getHostellerID())).equals("on")) {
                            issueBill(hostellerList.get(i));
                            Email email = new Email(hostellerList.get(i).getLastName(), hostellerList.get(i).getContact().getEmail(), "\n\nYour next month rental bill has been issued. Please log in to view the latest status.");
                        }
                    } catch (Exception ex) {
                    }
                }
                session.setAttribute("success", "Bill issued successfully");
                response.sendRedirect("/retrieveBillListing?t=0");
            } else {
                throw new Exception("No issue target is selected");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveBillListing?t=0");
        }
    }

    protected void issueBill(Hosteller hosteller) throws Exception {
        BillitemManager billItemManager = new BillitemManager(mgr);
        Billitem rentalFee = new Billitem();
        rentalFee.setDescription("Rental Fee of " + hosteller.getStayRoom().getRoomNo());
        rentalFee.setFee(BigDecimal.valueOf(hosteller.getStayRoom().getRentalFee()));
        Billitem utilityFee = new Billitem();
        utilityFee.setDescription("Utility Fee");
        utilityFee.setFee(BigDecimal.valueOf(hosteller.getStayRoom().getRentalFee() * 0.1));

        BillManager billManager = new BillManager(mgr);
        DateToString dateString = new DateToString();
        Date today = new Date();
        Bill bill = new Bill();
        bill.setBillNo(billManager.generateID(today));
        bill.setIssueDate(today);
        Date tempoDate = new Date();
        tempoDate.setMonth(tempoDate.getMonth() + 1);
        bill.setDescription(dateString.ToStringMonth(tempoDate.getMonth()) + " " + String.valueOf(tempoDate.getYear() + 1900) + " Rental Fee");
        Date dueDate = new Date();
        dueDate.setMonth(dueDate.getMonth() + 1);
        dueDate.setDate(7);
        bill.setDueDate(dateString.DateToEndDate(dueDate));
        bill.setIssueTo(hosteller);
        bill.setStatus("Pending");
        bill.setTotalAmount(rentalFee.getFee().add(utilityFee.getFee()));

        try {
            utx.begin();
            billManager.addBill(bill);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Issue bill fail");
        }
        rentalFee.setBillItemNo(billItemManager.generateID());
        rentalFee.setBillNo(bill);
        try {
            utx.begin();
            billItemManager.addBillitem(rentalFee);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Issue bill item fail");
        }

        utilityFee.setBillItemNo(billItemManager.generateID());
        utilityFee.setBillNo(bill);
        try {
            utx.begin();
            billItemManager.addBillitem(utilityFee);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Issue bill item fail");
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
