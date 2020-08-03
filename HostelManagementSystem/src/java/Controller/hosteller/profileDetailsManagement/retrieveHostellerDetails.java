
package Controller.hosteller.profileDetailsManagement;

import Model.HostelManager;
import Model.Hosteller;
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

public class retrieveHostellerDetails extends HttpServlet {
    
    @PersistenceContext
    EntityManager em;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        
        try {

            Hosteller curHosteller = (Hosteller)session.getAttribute("curHosteller");           
            HostelManager hostelMgr = new HostelManager(em);           
            List<String> locationList = new ArrayList();           
            locationList = hostelMgr.finaAllLocation();
           
            session.setAttribute("curHosteller", curHosteller);
            session.setAttribute("locationList", locationList);
            response.sendRedirect("/hosteller/account/updateProfile");
                       
        }catch(Exception ex){
            String message = ex.getMessage();
            session.setAttribute("updateSuccess", "false");
            session.setAttribute("message", message);
            response.sendRedirect("/hosteller/account/accountManagement");       
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
