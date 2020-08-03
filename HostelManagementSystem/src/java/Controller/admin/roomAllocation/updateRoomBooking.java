package Controller.admin.roomAllocation;

import Controller.Crypto;
import Controller.DateToString;
import Controller.Email;
import Model.Bill;
import Model.BillManager;
import Model.Billitem;
import Model.BillitemManager;
import Model.Entcard;
import Model.EntcardManager;
import Model.Hosteller;
import Model.HostellerManager;
import Model.Receipt;
import Model.ReceiptManager;
import Model.Room;
import Model.RoomManager;
import Model.Roombooking;
import Model.RoombookingManager;
import java.io.IOException;
import java.math.BigDecimal;
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

public class updateRoomBooking extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        Roombooking currentRoomBooking = (Roombooking) session.getAttribute("roomBooking");
        try {
            RoombookingManager roomBookingManager = new RoombookingManager(mgr);
            String roomBookingNo = currentRoomBooking.getRequestNo();
            if (roomBookingManager.findRoombookingByID(roomBookingNo) != null) {
                String status = (String) request.getParameter("status");
                String remark = (String) request.getParameter("remark");
                String success = "";
                String previousStatus = currentRoomBooking.getStatus().toLowerCase();
                if (!previousStatus.equals(status.toLowerCase())) {
                    switch (previousStatus) {
                        case "pending":
                            switch (status.toLowerCase()) {
                                case "approved":
                                    if (currentRoomBooking.getRequestType().toLowerCase().equals("new entry")) {
                                        currentRoomBooking.setBillNo(issueBill(currentRoomBooking));
                                    } else if (currentRoomBooking.getRequestType().toLowerCase().equals("room allocation")) {
                                        currentRoomBooking.setBillNo(issueBill(currentRoomBooking));
                                        if (currentRoomBooking.getBillNo().getTotalAmount().doubleValue() < 0) {
                                            remark = "Please get the remain deposit at hostel department counter";
                                            status = "Assigned";
                                        } else if (currentRoomBooking.getBillNo().getTotalAmount().doubleValue() == 0) {
                                            currentRoomBooking.getBillNo().setStatus("Paid");
                                            remark = "There are no remaining deposit from your previous room booking.";
                                            status = "Assigned";
                                        }
                                    } else if (currentRoomBooking.getRequestType().toLowerCase().equals("move out")) {
                                        status = "Assigned";
                                        remark = "Please get the remain deposit at hostel department counter";
                                        moveout(currentRoomBooking);
                                    }
                                    success = "Update Success. Room booking has been approved";
                                    break;
                                case "rejected":
                                    success = "Update Success. Room booking has been rejected";
                                    break;
                                default:
                                    throw new Exception("Update fail.Unexpected status is selected");
                            }
                            break;
                        case "paid":
                            switch (status.toLowerCase()) {
                                case "assigned":
                                    String entCardNo = request.getParameter("entCardNo");
                                    if (!entCardNo.equals("")) {
                                        assignRoom(currentRoomBooking);
                                        EntcardManager entcardManager = new EntcardManager(mgr);

                                        if (entcardManager.findEntcardByID(entCardNo) == null) {
                                            issueCardToHosteller(issueCard(entCardNo), currentRoomBooking.getRequestBy());
                                        } else {
                                            if (currentRoomBooking.getRequestBy().getEntCardNo().getEntCardNo().equals(entCardNo)) {
                                                issueCardToHosteller(entcardManager.findEntcardByID(entCardNo), currentRoomBooking.getRequestBy());
                                            } else {
                                                throw new Exception("Card has been assigned");
                                            }
                                        }

                                        status = "Assigned";
                                        success = "Update Success. Room has been assigned.";
                                    } else {
                                        throw new Exception("Required input value for entrance card no");
                                    }
                                    break;
                                default:
                                    throw new Exception("Update fail.Unexpected status is selected");
                            }
                            break;
                        default:
                            throw new Exception("Update fail.Unexpected status is selected");
                    }

                } else {
                    throw new Exception("Update fail with same status selected");
                }
                currentRoomBooking.setStatus(status);
                currentRoomBooking.setRemark(remark);
                currentRoomBooking.setUpdateDate(new Date());
                utx.begin();
                roomBookingManager.updateRoombookingDetails(currentRoomBooking);
                utx.commit();
                Email email = new Email(currentRoomBooking.getRequestBy().getLastName(), currentRoomBooking.getRequestBy().getContact().getEmail(), "\n\nYour room booking status is updated. Please log in to view the latest status.");
                session.setAttribute("success", success);
                session.setAttribute("roomBooking", roomBookingManager.findRoombookingByID(roomBookingNo));
                response.sendRedirect("/retrieveRoomBookingDetails?p=" + crypto.RBEncode(currentRoomBooking.getRequestNo()));
            } else {
                throw new Exception("Selected room booking is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveRoomBookingDetails?p=" + crypto.RBEncode(currentRoomBooking.getRequestNo()));
        }
    }

    protected void moveout(Roombooking booking) throws Exception {
        HostellerManager hostellerManager = new HostellerManager(mgr);
        Hosteller hosteller = booking.getRequestBy();
        EntcardManager entCardManager = new EntcardManager(mgr);
        Hosteller tempoHosteller = hostellerManager.findHostellerByID(hosteller.getHostellerID());
        hosteller.setStayRoom(null);
        hosteller.setEntCardNo(null);
        try {
            utx.begin();
            hostellerManager.updateHosteller(hosteller);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
        try {
            utx.begin();
            entCardManager.deleteEntcard(tempoHosteller.getEntCardNo().getEntCardNo());
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
    }

    protected Entcard issueCard(String entCardNo) throws Exception {
        EntcardManager entCardManager = new EntcardManager(mgr);
        if (entCardManager.findEntcardByID(entCardNo) != null) {
            throw new Exception("Current entrance card is assigned");
        }
        Entcard newEntCard = new Entcard();
        newEntCard.setEntCardNo(entCardNo);
        newEntCard.setIssueTime(new Date());
        try {
            utx.begin();
            entCardManager.addEntcard(newEntCard);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
        return newEntCard;
    }

    protected void issueCardToHosteller(Entcard entCard, Hosteller hosteller) throws Exception {
        HostellerManager hostellerManager = new HostellerManager(mgr);
        EntcardManager entCardManager = new EntcardManager(mgr);
        Hosteller tempoHosteller = hostellerManager.findHostellerByID(hosteller.getHostellerID());
        if (hosteller.getEntCardNo() != null) {
            if (!hosteller.getEntCardNo().getEntCardNo().equals(entCard.getEntCardNo())) {
                hosteller.setEntCardNo(null);
                try {
                    utx.begin();
                    hostellerManager.updateHosteller(hosteller);
                    utx.commit();
                } catch (Exception ex) {
                    throw new Exception("Update fail");
                }
                try {
                    utx.begin();
                    entCardManager.deleteEntcard(tempoHosteller.getEntCardNo().getEntCardNo());
                    utx.commit();
                } catch (Exception ex) {
                    throw new Exception("Update fail");
                }
            }
        }
        hosteller.setEntCardNo(entCard);
        try {
            utx.begin();
            hostellerManager.updateHostellerEntCard(hosteller);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
    }

    protected Bill issueBill(Roombooking roomBooking) throws Exception {
        BillitemManager billItemManager = new BillitemManager(mgr);
        BillManager billManager = new BillManager(mgr);
        RoomManager roomManager = new RoomManager(mgr);

        DateToString dateString = new DateToString();
        Billitem rentalFee = new Billitem();
        rentalFee.setDescription("Rental Fee For " + dateString.ToStringMonth(new Date().getMonth()) 
                + String.valueOf(new Date().getYear() + 1900));
        rentalFee.setFee(BigDecimal.valueOf(roomBooking.getRequestRoom().getRentalFee()));
        Billitem deposit = new Billitem();
        deposit.setDescription("Deposit For 2 Month");
        deposit.setFee(rentalFee.getFee().multiply(BigDecimal.valueOf(2)));
        Billitem previousDeposit = new Billitem();
        if (roomBooking.getRequestType().toLowerCase().equals("room allocation")) {
            Room room = roomManager.findRoomByID(roomBooking.getRequestBy().getStayRoom().getRoomNo());
            double lastTimeDeposit = room.getRentalFee() * 2 * -1;
            previousDeposit.setDescription("Previous Deposit");
            previousDeposit.setFee(BigDecimal.valueOf(lastTimeDeposit));
        }

        Bill bill = new Bill();
        bill.setBillNo(billManager.generateID(new Date()));
        bill.setDescription("Room application for Room No " + roomBooking.getRequestRoom().getRoomNo());
        if (roomBooking.getRequestType().toLowerCase().equals("room allocation")) {
            Room room = roomManager.findRoomByID(roomBooking.getRequestBy().getStayRoom().getRoomNo());
            bill.setDescription("Room allocation from Room No " + room.getRoomNo() + " to Room No " 
                    + roomBooking.getRequestRoom().getRoomNo());
        }
        bill.setIssueDate(new Date());
        Date dueDate = new Date();
        dueDate.setDate(dueDate.getDate() + 7);
        bill.setDueDate(dateString.DateToEndDate(dueDate));
        bill.setTotalAmount(rentalFee.getFee().add(deposit.getFee()));
        if (roomBooking.getRequestType().toLowerCase().equals("room allocation")) {
            bill.setTotalAmount(bill.getTotalAmount().add(previousDeposit.getFee()));
        }

        bill.setStatus("Pending");
        bill.setIssueTo(roomBooking.getRequestBy());
        try {
            utx.begin();
            billManager.addBill(bill);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }

        rentalFee.setBillItemNo(billItemManager.generateID());
        rentalFee.setBillNo(bill);
        try {
            utx.begin();
            billItemManager.addBillitem(rentalFee);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }

        deposit.setBillItemNo(billItemManager.generateID());
        deposit.setBillNo(bill);
        try {
            utx.begin();
            billItemManager.addBillitem(deposit);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
        }
        if (roomBooking.getRequestType().toLowerCase().equals("room allocation")) {
            previousDeposit.setBillItemNo(billItemManager.generateID());
            previousDeposit.setBillNo(bill);
            try {
                utx.begin();
                billItemManager.addBillitem(previousDeposit);
                utx.commit();
            } catch (Exception ex) {
                throw new Exception("Update fail");
            }
        }
        return bill;
    }


    protected void assignRoom(Roombooking roomBooking) throws Exception {
        HostellerManager hostellerManager = new HostellerManager(mgr);
        Hosteller hosteller = roomBooking.getRequestBy();
        hosteller.setStayRoom(roomBooking.getRequestRoom());
        try {
            utx.begin();
            hostellerManager.updateHostellerRoom(hosteller);
            utx.commit();
        } catch (Exception ex) {
            throw new Exception("Update fail");
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
