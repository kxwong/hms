package Controller.admin.facilityManagement;

import Controller.DateToString;
import Model.Facility;
import Model.FacilityManager;
import Model.Hostel;
import Model.HostelManager;
import java.io.IOException;
import java.util.Base64;
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

public class storeFacility extends HttpServlet {

    @PersistenceContext
    EntityManager mgr;
    @Resource
    UserTransaction utx;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            DateToString dateString = new DateToString();
            HostelManager hostelManager = new HostelManager(mgr);
            FacilityManager facilityManager = new FacilityManager(mgr);
            String hostelID = request.getParameter("hostel");
            String building = request.getParameter("building");
            String location = request.getParameter("location");
            String catdesc = request.getParameter("catdesc");
            String desc = request.getParameter("desc");
            if(desc == null){
                desc = "";
            }
            String category = request.getParameter("category");
            String operatingDay = request.getParameter("operatingDay");
            String reminder = request.getParameter("reminder");
            Date operatingStartHour;
            Date operatingEndHour;
            int capacity;
            byte[] imageByte;
            String extenstion = request.getParameter("imageByte").split("/")[1].split(";")[0];
            try {
                operatingStartHour = dateString.HourMinToDate(request.getParameter("operatingStartHour"));
                operatingEndHour = dateString.HourMinToDate(request.getParameter("operatingEndHour"));
                capacity = Integer.parseInt(request.getParameter("capacity"));
                imageByte = Base64.getDecoder().decode(request.getParameter("imageByte").split(",")[1]);
            } catch (Exception ex) {
                throw new Exception("Wrong format for input field");
            }
            if (building.equals("") || location.equals("") || catdesc.equals("") || category.equals("") || reminder.equals("") || operatingDay.equals("") || operatingStartHour.equals("") || operatingEndHour.equals("") || imageByte.equals("") || capacity <= 0 || imageByte.length <= 0) {
                throw new Exception("Please fill out all the field");
            } else if ((catdesc.equals("Badminton Court") || catdesc.equals("Tennis Court")) && desc.equals("")) {
                throw new Exception("Please fill out court desc field");
            } else if ((catdesc.equals("Badminton Court") || catdesc.equals("Tennis Court")) && desc.length() > 20) {
                throw new Exception("Court desciption word count should not more than 20");
            } else if (reminder.length() > 200) {
                throw new Exception("Reminder word count should not more than 200");
            } else if (!category.equals("Indoor") && !category.equals("Outdoor")) {
                throw new Exception("Unknown category selected");
            } else if (!operatingDay.equals("All days") && !operatingDay.equals("Weekdays") && !operatingDay.equals("Weekends")) {
                throw new Exception("Unknown operating day selected");
            } else if (!operatingDay.equals("All days") && !operatingDay.equals("Weekdays") && !operatingDay.equals("Weekends")) {
                throw new Exception("Unknown operating day selected");
            } else if (capacity >= 100) {
                throw new Exception("People capacity should between 1 - 99");
            } else if (operatingStartHour.equals(operatingEndHour)) {
                throw new Exception("Operating time can not be same");
            } else if (operatingStartHour.getMinutes() != 0 && operatingStartHour.getMinutes() != 30) {
                throw new Exception("Operating start time minute should only allow 30 minutes interval");
            } else if (operatingEndHour.getMinutes() != 0 && operatingEndHour.getMinutes() != 30) {
                throw new Exception("Operating end time minute should only allow 30 minutes interval");
            } else if (imageByte.length > 1070000) {
                throw new Exception("Facility image should not exceed 1 mb");
            } else if (!checkImageFile(extenstion)) {
                throw new Exception("Facility image with extension ." + extenstion + " is not allowed");
            } else {
                Hostel hostel = hostelManager.findHostelByID(hostelID);
                if (hostel == null) {
                    if (hostelManager.hostelExisted(location, building)) {
                        hostel = hostelManager.getExistedHostel(location, building);
                    } else {
                        hostel = new Hostel();
                        hostel.setHostelID(hostelManager.generateID());
                        hostel.setBuilding(building);
                        hostel.setLocation(location);
                        utx.begin();
                        hostelManager.addHostel(hostel);
                        utx.commit();
                    }
                }
                Facility facility = new Facility();
                facility.setFacilityID(facilityManager.generateID());
                facility.setDescription(desc.equals("") ? catdesc : (catdesc + "@Court " + desc));
                facility.setCapacity(capacity);
                facility.setCategory(category);
                facility.setReminder(reminder);
                facility.setOperatingDay(operatingDay);
                facility.setStartHour(operatingStartHour);
                facility.setEndHour(operatingEndHour);
                facility.setImage(imageByte);
                facility.setStatus("Available");
                facility.setHostelID(hostel);
                utx.begin();
                facilityManager.addFacility(facility);
                utx.commit();
                session.setAttribute("success", "New facility is addedd successfully");
                response.sendRedirect("/retrieveFacilityListing");
            }
        } catch (Exception ex) {
            session.setAttribute("error", ex.getLocalizedMessage());
            response.sendRedirect("/initializeAddNewFacility");
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
