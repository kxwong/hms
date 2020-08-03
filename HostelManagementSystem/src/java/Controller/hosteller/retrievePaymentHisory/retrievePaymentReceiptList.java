package Controller.hosteller.retrievePaymentHisory;

import Controller.Crypto;
import Model.Bill;
import Model.BillManager;
import Model.Hosteller;
import Model.ReceiptManager;
import Model.Receipt;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrievePaymentReceiptList extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM");
            Crypto encrypt = new Crypto();

            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured");
            } else {
                Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");
                String date = encrypt.UNDecode(request.getParameter("date"));
                ReceiptManager receiptMgr = new ReceiptManager(em);
                BillManager billMgr = new BillManager(em);
                List<Receipt> receiptList = new ArrayList<Receipt>();
                List<Bill> billList = billMgr.findAllByHosteller(curHosteller);

                if (request.getParameter("ob") != null && request.getParameter("seq") != null) {
                    String orderBy = encrypt.UNDecode(request.getParameter("ob"));
                    String seq = encrypt.UNDecode(request.getParameter("seq"));
                    seq = seq.toUpperCase();

                    receiptList = em.createQuery("SELECT r FROM Receipt r WHERE r.bill.issueTo = :issueTo " + "ORDER BY " + orderBy + " " + seq).setParameter("issueTo", curHosteller).getResultList();
                    session.setAttribute("seq", seq.toLowerCase());

                } else {
                    List<Receipt> tempoList = receiptMgr.findAll();
                    for (int i = 0; i < tempoList.size(); i++) {
                        for (int j = 0; j < billList.size(); j++) {                           
                            if(billList.get(j).getReceiptNo()!=null && billList.get(j).getReceiptNo().equals(tempoList.get(i))){                                
                                String billDate = formatDate.format(billList.get(j).getIssueDate());
                                if(billDate.equals(date)){
                                    receiptList.add(tempoList.get(i));
                                }                                                               
                            }
                        }
                    }
                }
                session.setAttribute("selectedMonth", date);
                session.setAttribute("receiptList", receiptList);
                response.sendRedirect("/hosteller/rental/viewPaymentHistory");
            }

        } catch (Exception ex) {
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
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
