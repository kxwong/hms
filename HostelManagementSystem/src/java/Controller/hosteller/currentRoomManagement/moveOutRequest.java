package Controller.hosteller.currentRoomManagement;

import Model.Hosteller;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
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

public class moveOutRequest extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            Hosteller hosteller = (Hosteller) session.getAttribute("curHosteller");
            Roombooking roomBooking = new Roombooking();
            RoombookingManager roomBookingMgr = new RoombookingManager(em);
            Date curDate = new Date();

            List<Roombooking> pendingRoomBookingList = roomBookingMgr.retrievePendingBooking("Pending", hosteller);
            List<Roombooking> approvedRoomBookingList = roomBookingMgr.retrievePendingBooking("Approved", hosteller);

            if (pendingRoomBookingList.size() > 0) {
                throw new Exception("You still have pending room booking, please wait for it to complete before proceed for another booking");
            } else if (approvedRoomBookingList.size() > 0) {
                throw new Exception("You still have a room booking that have not been paid, please check it.");
            } else {
                roomBooking.setRequestNo(roomBookingMgr.generateID());
                roomBooking.setRequestType("Move Out");
                roomBooking.setRequestDate(curDate);
                roomBooking.setUpdateDate(curDate);
                roomBooking.setStatus("Pending");
                roomBooking.setRequestRoom(hosteller.getStayRoom());
                roomBooking.setRequestBy(hosteller);
            }

            utx.begin();
            boolean successRoombooking = roomBookingMgr.addRoombooking(roomBooking);
            utx.commit();

            if (successRoombooking == true) {
                String successMsg = "Move out request made, your request ID is " + roomBooking.getRequestNo() + ". Please wait for admin approval.";
                session.setAttribute("bookSuccess", "true");
                session.setAttribute("message", successMsg);
                response.sendRedirect("/retrieveCurrentRoomStatus");
            } else {
                throw new Exception("An error has occured.");
            }

        } catch (Exception ex) {
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
