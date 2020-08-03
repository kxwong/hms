package Controller.admin.hostelSetup;

import Controller.Crypto;
import Model.Floorplan;
import Model.FloorplanManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

public class updateFloorplan extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Crypto crypto = new Crypto();
        try {
            FloorplanManager floorplanManager = new FloorplanManager(mgr);
            int peopleQty;
            int roomQty;
            int floor;
            byte[] imageByte;
            String extenstion = request.getParameter("imageByte").split("/")[1].split(";")[0];
            try {
                peopleQty = Integer.parseInt(request.getParameter("peopleQty"));
                roomQty = Integer.parseInt(request.getParameter("roomQty"));
                floor = Integer.parseInt(request.getParameter("floor"));
                imageByte = Base64.getDecoder().decode(request.getParameter("imageByte").split(",")[1]);
            } catch (Exception ex) {
                throw new Exception("Wrong format for input field");
            }
            if (imageByte.equals("") || peopleQty <= 0 || roomQty <= 0 || floor <= 0 || imageByte.length <= 0) {
                throw new Exception("Please fill out required field");
            } else if (roomQty > peopleQty) {
                throw new Exception("People capacity should more than or equal to room capacity");
            } else if (peopleQty >= 100) {
                throw new Exception("People capacity should between 1 - 99");
            } else if (roomQty >= 100) {
                throw new Exception("Room capacity should between 1 - 99");
            } else if (floor >= 100) {
                throw new Exception("Floor should between 1 - 99");
            } else if (imageByte.length > 1070000) {
                throw new Exception("Floorplan image should not exceed 1 mb");
            } else if (!checkImageFile(extenstion)) {
                throw new Exception("Floorplan image with extension ." + extenstion + " is not allowed");
            } else {
                Floorplan floorplan = floorplanManager.findFloorplanByID(((Floorplan) session.getAttribute("floorplan")).getFloorplanID());
                if (floorplan.getFloor() != floor) {
                    if (floorplanManager.floorExisted(floorplan.getHostelID(), floor)) {
                        throw new Exception("Floor " + floor + " of hostel located at " + floorplan.getHostelID().getLocation() + " " + floorplan.getHostelID().getBuilding() + " is already existed");
                    }
                }
                boolean imageChange = !(Arrays.toString(imageByte).equals(Arrays.toString(floorplan.getImage())));
                floorplan.setPeopleCapacity(peopleQty);
                floorplan.setRoomCapacity(roomQty);
                floorplan.setFloor(floor);
                floorplan.setImage(imageByte);
                utx.begin();
                floorplanManager.updateFloorplan(floorplan, imageChange);
                utx.commit();
                session.removeAttribute("floorplan");
                session.setAttribute("success", "Floorplan is updated successfully");
                response.sendRedirect("/retrieveFloorplanDetails?p=" + crypto.FPEncode(floorplan.getFloorplanID()));
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/initializeEditFloorplan?p=" + crypto.FPEncode(((Floorplan) session.getAttribute("floorplan")).getFloorplanID()));
        }
    }

    protected boolean checkImageFile(String ext) {
        String imageExt[] = {"jpg", "jpeg", "bmp", "gif", "png"};
        boolean imageMatchExtension = false;
        for (int i = 0; i < imageExt.length; i++) {
            if (imageExt[i].equals(ext)) {
                imageMatchExtension = true;
            }
        }
        return imageMatchExtension;
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
