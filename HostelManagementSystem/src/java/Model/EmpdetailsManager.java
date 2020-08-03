
package Model;

import javax.annotation.Resource;
import javax.persistence.*;

public class EmpdetailsManager {
    
    @PersistenceContext
    EntityManager mgr;
    @Resource
    Query query;
    
    public EmpdetailsManager (EntityManager mgr){
        this.mgr=mgr;
    }
    
    public boolean addEmpdetails (Empdetails empdetails){
        mgr.persist(empdetails);
        return true;
    }
    
    public Empdetails findEmpdetailsByID(String id){
        Empdetails empdetails = mgr.find(Empdetails.class, id);
        return empdetails;
    }
    
    public boolean deleteEmpdetails(String id) {
        Empdetails empdetails = findEmpdetailsByID(id);
        if (empdetails != null) {
            mgr.remove(empdetails);
            return true;
        }
        return false;
    }
    
    public boolean updateEmpdetails(Empdetails empdetails) throws Exception {
        Empdetails tempoEmpdetails = findEmpdetailsByID(empdetails.getHostellerID());
        if (tempoEmpdetails != null) {
            tempoEmpdetails.setBranch(empdetails.getBranch());
            tempoEmpdetails.setDepartment(empdetails.getDepartment());
            tempoEmpdetails.setWorkerID(empdetails.getWorkerID());
            return true;
        }
        return false;
    }
    
}
