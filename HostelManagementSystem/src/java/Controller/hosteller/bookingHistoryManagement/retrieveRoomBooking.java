
package Controller.hosteller.bookingHistoryManagement;

import Controller.Crypto;
import Model.Hosteller;
import Model.Roombooking;
import Model.RoombookingManager;
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

public class retrieveRoomBooking extends HttpServlet {
    
    @PersistenceContext
    EntityManager em;  
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Crypto encrypt = new Crypto();
        
        try {
            Hosteller curHosteller = (Hosteller)session.getAttribute("curHosteller");
            List<Roombooking> roomBookingList = new ArrayList<Roombooking>();
            RoombookingManager roombookingMgr = new RoombookingManager(em);
            
            if(request.getParameter("ob")!=null&&request.getParameter("seq")!=null){
                String orderBy = encrypt.UNDecode(request.getParameter("ob"));
                String seq = encrypt.UNDecode(request.getParameter("seq"));
                seq = seq.toUpperCase();
                 
                roomBookingList = em.createQuery("SELECT r FROM Roombooking r WHERE r.requestBy = :requestBy " + "ORDER BY " + orderBy + " " + seq).setParameter("requestBy", curHosteller).getResultList();               
                
                session.setAttribute("seq", seq.toLowerCase());                
            }else{
                roomBookingList = roombookingMgr.retrieveAllBookingByHosteller(curHosteller); 
            }
                                                                               
            session.setAttribute("roomBookingList", roomBookingList);
            response.sendRedirect("/hosteller/room/roomBookingRecord");
            
            
        }catch(Exception ex){
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
