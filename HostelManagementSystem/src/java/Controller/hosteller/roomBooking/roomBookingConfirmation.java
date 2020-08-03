package Controller.hosteller.roomBooking;

import Model.Hosteller;
import Model.HostellerManager;
import Model.Room;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

public class roomBookingConfirmation extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            if (session.getAttribute("roomSelected") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                String requestType = request.getParameter("requestType");
                Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");
                Room roomSelected = (Room) session.getAttribute("roomSelected");
                Roombooking roomBooking = new Roombooking();
                RoombookingManager roomBookingMgr = new RoombookingManager(em);
                HostellerManager hostellerMgr = new HostellerManager(em);
                List<Hosteller> hostellerList = hostellerMgr.findByRoom(roomSelected.getRoomNo());

                Date currentDate = new Date();

                List<Roombooking> pendingRoomBookingList = roomBookingMgr.retrievePendingBooking("Pending", curHosteller);
                List<Roombooking> approvedRoomBookingList = roomBookingMgr.retrievePendingBooking("Approved", curHosteller);

                if (roomSelected.equals(curHosteller.getStayRoom())) {
                    throw new Exception("You are currently stay in this room.");
                } else if (approvedRoomBookingList.size() > 0) {
                    throw new Exception("You still have a room booking that have not been paid, please check it.");
                } else if (pendingRoomBookingList.size() > 0) {
                    throw new Exception("You still have pending room booking, please wait for it to complete before proceed for another booking");
                }else if(roomSelected.getCapacity().equals(hostellerList.size())){
                    throw new Exception("The room is already full, please book another room.");
                } 
                else {
                    String requestNo = roomBookingMgr.generateID();
                    roomBooking.setRequestNo(requestNo);
                    roomBooking.setRequestType(requestType);
                    roomBooking.setRequestDate(currentDate);
                    roomBooking.setUpdateDate(currentDate);
                    roomBooking.setStatus("Pending");
                    roomBooking.setRequestRoom(roomSelected);
                    roomBooking.setRequestBy(curHosteller);
                }

                utx.begin();
                boolean roomBookingSuccess = roomBookingMgr.addRoombooking(roomBooking);
                utx.commit();

                if (roomBookingSuccess == false) {
                    throw new Exception("Room booking failed, please try again");
                } else {
                    String successMsg = "Room booking success, your room booking request ID is " + roomBooking.getRequestNo() + ". Please wait for admin approval.";
                    session.setAttribute("bookSuccess", "true");
                    session.setAttribute("message", successMsg);
                    response.sendRedirect("/retrieveCurrentRoomStatus");
                }
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
