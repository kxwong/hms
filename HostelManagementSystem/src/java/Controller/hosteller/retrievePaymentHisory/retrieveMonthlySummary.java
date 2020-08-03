
package Controller.hosteller.retrievePaymentHisory;

import Model.Bill;
import Model.BillManager;
import Model.Hosteller;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

public class retrieveMonthlySummary extends HttpServlet {

    @PersistenceContext
    EntityManager em;    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();

        List<Bill> billList = new ArrayList();
        BillManager billMgr = new BillManager(em);    
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM");
        
        try {           
            
            Hosteller curHosteller = (Hosteller)session.getAttribute("curHosteller");
            
            billList = billMgr.findAllByHosteller(curHosteller);
            
            List<String> dateStrList = new ArrayList();
            
            
            for(int i=0;i<billList.size();i++){
                if(!(dateStrList.contains(formatDate.format(billList.get(i).getIssueDate())))){
                    dateStrList.add(formatDate.format(billList.get(i).getIssueDate()));
                }                             
            }           
            
            session.setAttribute("billList", billList);
            session.setAttribute("dateList", dateStrList);
            response.sendRedirect("/hosteller/rental/monthlyPaymentHistory");                                               
        }catch(Exception ex){
            String errMsg = ex.getMessage();
            session.setAttribute("bookSuccess", "false");
            session.setAttribute("message", errMsg);
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
