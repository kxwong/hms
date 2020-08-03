package Controller.admin.visitorManagement;

import Model.Entcard;
import Model.EntcardManager;
import Model.Entrecord;
import Model.EntrecordManager;
import Model.Room;
import Model.RoomManager;
import Model.Visitor;
import Model.VisitorManager;
import java.io.IOException;
import java.math.BigInteger;
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

public class storeVisitor extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String name = request.getParameter("name");
            String icNo = request.getParameter("icNo");
            String country = request.getParameter("countryCode");
            String contact = request.getParameter("contactNo");
            String entryReason = request.getParameter("entryReason");
            String roomID = request.getParameter("room");
            String entCardNo = request.getParameter("entCard");
            BigInteger contactNo;
            RoomManager roomManager = new RoomManager(mgr);
            Room room = roomManager.findRoomByID(roomID);
            try {
                contactNo = BigInteger.valueOf(Long.parseLong(country.replace("+", "").concat(contact)));
            } catch (Exception ex) {
                throw new Exception("Contact number must be numeric value");
            }
            if (name.equals("") || icNo.equals("") || contact.equals("") || country.equals("") || entryReason.equals("") || roomID.equals("")|| entCardNo.equals("")) {
                throw new Exception("Please fill in all input field");
            } else if (name.length() > 50) {
                throw new Exception("Name should not over 50 character");
            } else if (icNo.length() > 20) {
                throw new Exception("Identification number should not over 20 character");
            } else if (entryReason.length() > 100) {
                throw new Exception("Entry reason should not over 100 character");
            } else if (entCardNo.length() > 30) {
                throw new Exception("Entrance card no should not over 30 character");
            } else if (String.valueOf(contactNo).length() > 20) {
                throw new Exception("Contact number should not over 20 character");
            } else if (room.equals(null)) {
                throw new Exception("Visit room is not existing.");
            } else {
                EntcardManager entCardManager = new EntcardManager(mgr);
                if (entCardManager.findEntcardByID(entCardNo) != null) {
                    if (entCardManager.findEntcardByID(entCardNo).getHosteller() != null) {
                        throw new Exception("Current entrance card is assigned");
                    } else {
                        Entcard tempoCard = entCardManager.findEntcardByID(entCardNo);
                        Entcard tempoNewCard = new Entcard();
                        tempoNewCard.setEntCardNo(tempoCard.getVisitor().getVisitorID() + "_" + tempoCard.getEntCardNo());
                        tempoNewCard.setIssueTime(tempoCard.getIssueTime());
                        utx.begin();
                        entCardManager.addEntcard(tempoNewCard);
                        utx.commit();
                        EntrecordManager entRecordManager = new EntrecordManager(mgr);
                        List<Entrecord> entrecordList = entRecordManager.findByEntCard(tempoCard.getEntCardNo());
                        for (Entrecord tempoRecord : entrecordList) {
                            tempoRecord.setEntCardNo(tempoNewCard);
                            utx.begin();
                            entRecordManager.updateEntrecord(tempoRecord);
                            utx.commit();
                        }
                        VisitorManager visitorManager = new VisitorManager(mgr);
                        Visitor visitor = tempoCard.getVisitor();
                        visitor.setEntCardNo(tempoNewCard);
                        utx.begin();
                        visitorManager.updateVisitor(visitor);
                        utx.commit();
                        utx.begin();
                        entCardManager.deleteEntcard(tempoCard.getEntCardNo());
                        utx.commit();
                    }
                }
                Entcard newEntCard = new Entcard();
                newEntCard.setEntCardNo(entCardNo);
                newEntCard.setIssueTime(new Date());
                utx.begin();
                entCardManager.addEntcard(newEntCard);
                utx.commit();
                VisitorManager visitorManager = new VisitorManager(mgr);
                Visitor visitor = new Visitor();
                visitor.setVisitorID(visitorManager.generateID());
                visitor.setName(name);
                visitor.setIdentificationNo(icNo);
                visitor.setMobilePhone(contactNo);
                visitor.setEntryReason(entryReason);
                visitor.setVisitRoom(room);
                visitor.setEntCardNo(newEntCard);
                utx.begin();
                visitorManager.addVisitor(visitor);
                utx.commit();
                session.setAttribute("success", "Visitor is registered successfully");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
        }
        response.sendRedirect("/guard/visitor/registerVisitor");
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
