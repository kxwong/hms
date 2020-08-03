package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.Roommapping;
import Model.RoommappingPK;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class storeRoomStep1 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            String selectType = request.getParameter("selectType");
            String coordinate = request.getParameter("coordinate");
            if (selectType == null && selectType.equals("")) {
                throw new Exception("No room mapping type is found");
            } else if (coordinate == null || coordinate.equals("")) {
                throw new Exception("No highligted area is found");
            } else {
                if (IsPolCoorValid(coordinate)) {
                    String coordinateList[] = coordinate.split("_");
                    if (!(coordinateList.length >= 3 && coordinateList.length <= 9)) {
                        throw new Exception("Highligted area is not valid");
                    }
                } else {
                    throw new Exception("Highligted area is not valid");
                }
                session.setAttribute("coordinate", coordinate);
                response.sendRedirect("/admin/hostel/addRoomStep2");
            }

        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/initializeAddRoomStep1?p=" + crypto.FPEncode(((Floorplan) session.getAttribute("floorplan")).getFloorplanID()));
        }
    }

    protected boolean IsPolCoorValid(String coordinate) {
        boolean valid = true;
        try {
            String coordinateList[] = coordinate.split("_");
            for (int i = 0; i < coordinateList.length; i++) {
                int x = Integer.parseInt(coordinateList[i].split(",")[0]);
                int y = Integer.parseInt(coordinateList[i].split(",")[1]);
            }
        } catch (Exception ex) {
            valid = false;
        }
        return valid;
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
