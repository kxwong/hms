package Controller.hosteller.facilitiesBooking;

import Model.Facility;
import Model.FacilityManager;
import Model.Hosteller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class retrieveAllFacilitiesWithinBranch extends HttpServlet {

    @PersistenceContext
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {

            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                Hosteller curHosteller = (Hosteller) session.getAttribute("curHosteller");
                List<Facility> tempoFacilityList = new ArrayList<Facility>();
                List<Facility> indoorFacilityList = new ArrayList<Facility>();
                List<Facility> outdoorFacilityList = new ArrayList<Facility>();
                FacilityManager facilityMgr = new FacilityManager(em);

                tempoFacilityList = facilityMgr.findAll();

                for (int i = 0; i < tempoFacilityList.size(); i++) {
                    if (tempoFacilityList.get(i).getHostelID().getLocation().equals(curHosteller.getEmpdetails().getBranch()) && tempoFacilityList.get(i).getStatus().equals("Available")) {

                        if (tempoFacilityList.get(i).getCategory().toLowerCase().equals("indoor")) {
                            indoorFacilityList.add(tempoFacilityList.get(i));
                        } else {
                            outdoorFacilityList.add(tempoFacilityList.get(i));
                        }

                    }
                }

                List<String> tempoIndoorFacilityNameList = new ArrayList();

                for (int j = 0; j < indoorFacilityList.size(); j++) {
                    String facilityName = indoorFacilityList.get(j).getDescription().split("@")[0];
                    if (!(tempoIndoorFacilityNameList.contains(facilityName))) {
                        tempoIndoorFacilityNameList.add(facilityName); //get all the name
                    }
                }

                List<Facility> newIndoorFacilityList = new ArrayList();
                int counter = 0;
                for (int z = 0; z < tempoIndoorFacilityNameList.size(); z++) {
                    for (int k = 0; k < indoorFacilityList.size(); k++) {

                        if (tempoIndoorFacilityNameList.get(z).equals(indoorFacilityList.get(k).getDescription().split("@")[0])) {
                            newIndoorFacilityList.add(indoorFacilityList.get(k));
                            counter += 1;
                        }
                        if (counter == 1) {
                            counter = 0;
                            break;
                        }
                    }
                }
                

                List<String> tempoOutdoorFacilityNameList = new ArrayList();

                for (int j = 0; j < outdoorFacilityList.size(); j++) {
                    String facilityName = outdoorFacilityList.get(j).getDescription().split("@")[0];
                    if (!(tempoOutdoorFacilityNameList.contains(facilityName))) {
                        tempoOutdoorFacilityNameList.add(facilityName); //get all the name
                    }
                }

                List<Facility> newOutdoorFacilityList = new ArrayList();
                int counter1 = 0;
                for (int z = 0; z < tempoOutdoorFacilityNameList.size(); z++) {
                    for (int k = 0; k < outdoorFacilityList.size(); k++) {

                        if (tempoOutdoorFacilityNameList.get(z).equals(outdoorFacilityList.get(k).getDescription().split("@")[0])) {
                            newOutdoorFacilityList.add(outdoorFacilityList.get(k));
                            counter += 1;
                        }
                        if (counter == 1) {
                            counter = 0;
                            break;
                        }
                    }
                }
                
                session.setAttribute("facilityOutdoorList", newOutdoorFacilityList);
                session.setAttribute("facilityIndoorList", newIndoorFacilityList);
                response.sendRedirect("/hosteller/facilities/facilitiesTypeOverview");

            }

        } catch (Exception ex) {
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
