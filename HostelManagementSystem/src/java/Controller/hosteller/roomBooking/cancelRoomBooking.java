
package Controller.hosteller.roomBooking;

import Model.Bill;
import Model.BillManager;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class cancelRoomBooking extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            Roombooking curRoomBooking = new Roombooking();
            RoombookingManager roombookingMgr = new RoombookingManager(em);
            Bill curBill = new Bill();
            BillManager billMgr = new BillManager(em);
            Date curDate = new Date();

            if (request.getParameter("bid") == null) {
                throw new Exception("An error has occured. Cancellation failed.");
            } else {
                String bookingID = request.getParameter("bid");
                curRoomBooking = roombookingMgr.findRoombookingByID(bookingID);

                if (curRoomBooking.getBillNo() != null) {
                    curBill = curRoomBooking.getBillNo();
                    curBill.setStatus("Cancelled");
                    utx.begin();
                    boolean billUpdate = billMgr.updateBill(curBill);
                    utx.commit();
                    curRoomBooking.setBillNo(curBill);
                }

                curRoomBooking.setStatus("Cancelled");
                curRoomBooking.setUpdateDate(curDate);

                utx.begin();
                boolean roomBookingUpdate = roombookingMgr.updateRoombooking(curRoomBooking);
                utx.commit();

                if (roomBookingUpdate == true) {
                    String successMsg = "Request ID " + curRoomBooking.getRequestNo() + " is cancelled.";
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
