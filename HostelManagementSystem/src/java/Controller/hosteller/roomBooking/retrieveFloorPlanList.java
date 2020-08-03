
package Controller.hosteller.roomBooking;

import Model.*;
import java.io.IOException;
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

public class retrieveFloorPlanList extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            Hosteller currentHosteller = (Hosteller)session.getAttribute("curHosteller");
            FloorplanManager floorplanMgr = new FloorplanManager(em);
            RoomManager roomMgr = new RoomManager(em);
            Empdetails empdetails = new Empdetails();
            empdetails = currentHosteller.getEmpdetails();
            String location = empdetails.getBranch();  
            List<Floorplan> floorPlanList = floorplanMgr.findByLocation(currentHosteller.getEmpdetails().getBranch());
            List<Room> roomList = roomMgr.findRoomListByLocation(location);
            currentHosteller = (Hosteller)session.getAttribute("curHosteller");
            if(floorPlanList.size()==0){
                throw new Exception ("Currently there are no hostel floorplan available for this location.");
            }else{
                session.setAttribute("floorPlanList", floorPlanList);
                session.setAttribute("roomList", roomList);
                response.sendRedirect("/hosteller/room/floorPlanListing");                              
            }
                    
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
