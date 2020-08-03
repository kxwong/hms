package Controller.hosteller.facilitiesBooking;

import Model.Facility;
import Model.FacilityManager;
import Model.Facilitybooking;
import Model.FacilitybookingManager;
import Model.Hosteller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class bookFacility extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            SimpleDateFormat formatSelectDate = new SimpleDateFormat("yyyy-MM-dd EEEEEE");
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aaaaa");

            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                try {
                    FacilityManager facilityMgr = new FacilityManager(em);
                    Facilitybooking facilityBooking = new Facilitybooking();
                    FacilitybookingManager facilityBookingMgr = new FacilitybookingManager(em);
                    List<Facilitybooking> currentList = new ArrayList();
                    Date currentDate = new Date();
                    Date selectedDate = new Date();
                    Date selectedTime = new Date();
                    Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");

                    if (request.getParameter("sid") == null && request.getParameter("bookCapacity") == null && request.getParameter("selectedDate") == null && request.getParameter("selectedTime") == null) {
                        throw new Exception("An error has occured, please reselect again.");
                    } else {
                        short bookCapacity = Short.parseShort(request.getParameter("bookCapacity"));
                        Facility selectedFacility = facilityMgr.findFacilityByID(request.getParameter("sid"));
                        facilityBooking.setBookingID(facilityBookingMgr.generateID());
                        selectedDate = formatSelectDate.parse(request.getParameter("selectedDate"));
                        selectedTime = formatTime.parse(request.getParameter("selectedTime"));
                        selectedDate.setHours(selectedTime.getHours());
                        selectedDate.setMinutes(selectedTime.getMinutes());
                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        String dateStr = formatDate.format(selectedDate);

                        List<Facilitybooking> facilityBookingList = facilityBookingMgr.findBookingByFacility(selectedFacility);

                        int counter = 0;

                        for (int j = 0; j < facilityBookingList.size(); j++) {
                            if (formatDate.format(facilityBookingList.get(j).getBookTime()).equals(formatDate.format(selectedDate))) {
                                counter += facilityBookingList.get(j).getBookQuantity();
                            }
                        }

                        int remainingAmt = selectedFacility.getCapacity() - counter;

                        if (remainingAmt < bookCapacity) {
                            throw new Exception("Sorry, not enough spaces for your booking. Left " + " " + remainingAmt + " more spaces");
                        }

                        currentList = facilityBookingMgr.findAllByHosteller(curHosteller);

                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getBookTime().equals(selectedDate)) {
                                throw new Exception("You already have a same booking at the same time.");
                            }
                        }

                        facilityBooking.setBookTime(selectedDate);
                        facilityBooking.setBookQuantity(bookCapacity);
                        facilityBooking.setRequestTime(currentDate);
                        facilityBooking.setUpdateTime(currentDate);
                        facilityBooking.setStatus("Pending");
                        facilityBooking.setBookBy(curHosteller);
                        facilityBooking.setBookFacility(selectedFacility);

                        utx.begin();
                        boolean facilityBookingSuccess = facilityBookingMgr.addFacilitybooking(facilityBooking);
                        utx.commit();

                        if (facilityBookingSuccess == true) {
                            session.setAttribute("successMsg", "Your facility booking has been successfully made, your booking ID is " + facilityBooking.getBookingID());
                            response.sendRedirect("/retrieveAllBookingRecord");
                        } else {
                            throw new Exception("Facility booking failed, please try again.");
                        }

                    }
                } catch (Exception ex) {
                    String errMsg = ex.getMessage();
                    session.setAttribute("errMsg", errMsg);
                    response.sendRedirect("/hosteller/facilities/facilityRecord");
                }
            }

        } catch (Exception ex) {
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
