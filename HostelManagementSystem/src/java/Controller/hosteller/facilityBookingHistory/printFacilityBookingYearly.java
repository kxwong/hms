/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.hosteller.facilityBookingHistory;

import Controller.DateToString;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Hosteller;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author kxwong
 */
public class printFacilityBookingYearly extends HttpServlet {
    
    @PersistenceContext
    EntityManager em;
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         HttpSession session = request.getSession();
         
        try {
            Hosteller curHosteller = new Hosteller();
            List<Facilitybooking> faciBookingList = new ArrayList();
            List<Facilitybooking> tempofaciBookingList = new ArrayList();
            FacilitybookingManager faciBookingMgr = new FacilitybookingManager(em);
            SimpleDateFormat formatYearMonth = new SimpleDateFormat("MM-yyyy");
            Date selectedDate = new Date();
            DateToString dateToString = new DateToString();
            
            if(request.getParameter("year")==null||session.getAttribute("curHosteller")==null){
                throw new Exception("Please select a year before proceed.");
            }else{
                curHosteller = (Hosteller)session.getAttribute("curHosteller");
                String dateStr = request.getParameter("year");
                
                if(!dateToString.IsMonthYearValid(dateStr)){
                    throw new Exception("Please insert valid month and year");
                }
                                             
                selectedDate = formatYearMonth.parse(dateStr);
                
                tempofaciBookingList = faciBookingMgr.findAllByHosteller(curHosteller);
                
                
                for(int i=0;i<tempofaciBookingList.size();i++){
                    if((selectedDate.getYear()+1900)==(tempofaciBookingList.get(i).getRequestTime().getYear()+1900) && selectedDate.getMonth()==tempofaciBookingList.get(i).getRequestTime().getMonth() ){
                        faciBookingList.add(tempofaciBookingList.get(i));
                    }                   
                }  
                
                if(faciBookingList.size()==0){
                    throw new Exception("No booking record found in that year and month.");
                }
                
                session.setAttribute("faciBookingList", faciBookingList);
                response.sendRedirect("/hosteller/facilities/faciBookingYearlyStatement");                
                
            }                                           
        }catch(Exception ex){
            String errMsg = ex.getMessage();
            session.setAttribute("errMsg", errMsg);
            response.sendRedirect("/hosteller/facilities/facilityRecord");
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
