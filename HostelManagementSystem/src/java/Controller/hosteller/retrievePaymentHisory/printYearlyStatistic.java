
package Controller.hosteller.retrievePaymentHisory;

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

public class printYearlyStatistic extends HttpServlet {
    
    @PersistenceContext
    EntityManager em;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            
            List<Bill> tempoBillList = new ArrayList();
            List<Bill> billList = new ArrayList();
            BillManager billMgr = new BillManager(em);
            Hosteller curHosteller = new Hosteller();
            
            if(request.getParameter("yearSelected")==null||session.getAttribute("curHosteller")==null){
                throw new Exception("Please select a year before proceed.");
            }else{
                int year = Integer.parseInt(request.getParameter("yearSelected"));
                curHosteller = (Hosteller)session.getAttribute("curHosteller");
                tempoBillList = billMgr.findAllByHosteller(curHosteller);
                for(int i=0;i<tempoBillList.size();i++){
                    if((tempoBillList.get(i).getIssueDate().getYear()+1900)==year){
                        billList.add(tempoBillList.get(i));
                    }
                }
                session.setAttribute("billList", billList);               
                response.sendRedirect("/hosteller/rental/billYearlyStatement");                               
            }
            
        }catch(Exception ex){
            response.sendRedirect("/hostellerSide/rental/monthlyPaymentHistory");
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
