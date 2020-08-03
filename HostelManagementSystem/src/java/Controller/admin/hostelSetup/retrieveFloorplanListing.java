package Controller.admin.hostelSetup;

import Model.FloorplanManager;
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

public class retrieveFloorplanListing extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("locationList");
        session.removeAttribute("buildingList");
        FloorplanManager floorplanManager = new FloorplanManager(mgr);
        List<List<String>> allBuildingList = new ArrayList<>();
        List<String> locationList = floorplanManager.findAllLocation();
        for (int i = 0; i < locationList.size(); i++) {
            List<String> tempoBuildingList = floorplanManager.findAllBuilding(locationList.get(i));
            allBuildingList.add(tempoBuildingList);
        }
        session.setAttribute("floorplanList", floorplanManager.findAllOrderByFloorAsc());
        session.setAttribute("locationList", locationList);
        session.setAttribute("buildingList", allBuildingList);
        response.sendRedirect("/admin/hostel/floorplanlisting");
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
