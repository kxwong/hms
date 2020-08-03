package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.FloorplanManager;
import Model.Room;
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

public class initializeAddRoomStep1 extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            String floorplanID;
            try {
                String enFloorplanID = request.getParameter("p");
                Crypto crypto = new Crypto();
                floorplanID = crypto.FPDecode(enFloorplanID);
            } catch (Exception ex) {
                throw new Exception("Selected floorplan is not existed");
            }
            FloorplanManager floorplanManager = new FloorplanManager(mgr);
            Floorplan floorplan = floorplanManager.findFloorplanByID(floorplanID);
            if (floorplan != null) {
                List<Room> roomList = new ArrayList<>();
                for (int i = 0; i < floorplan.getRoomList().size(); i++) {
                    if (floorplan.getRoomList().get(i).getStatus().toLowerCase().equals("available")) {
                        roomList.add(floorplan.getRoomList().get(i));
                    }
                }
                if (roomList.size() < floorplan.getRoomCapacity()) {
                    session.setAttribute("floorplan", floorplan);
                    session.setAttribute("roomList", roomList);
                    response.sendRedirect("/admin/hostel/addRoomStep1");
                } else {
                    throw new Exception("Current floorplan has already reach the limit of the room quantity");
                }
            } else {
                throw new Exception("Selected floorplan is not existed");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/retrieveFloorplanListing");
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
