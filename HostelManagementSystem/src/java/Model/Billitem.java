/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "billitem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Billitem.findAll", query = "SELECT b FROM Billitem b")
    , @NamedQuery(name = "Billitem.findItemListByBillNo", query = "SELECT b FROM Billitem b WHERE b.billNo.billNo = :billNo")
    , @NamedQuery(name = "Billitem.findByBillItemNo", query = "SELECT b FROM Billitem b WHERE b.billItemNo = :billItemNo")
    , @NamedQuery(name = "Billitem.findByDescription", query = "SELECT b FROM Billitem b WHERE b.description = :description")
    , @NamedQuery(name = "Billitem.findAllOrderDesc", query = "SELECT b FROM Billitem b ORDER BY b.billItemNo desc")
    , @NamedQuery(name = "Billitem.findByFee", query = "SELECT b FROM Billitem b WHERE b.fee = :fee")})
public class Billitem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "BillItemNo")
    private String billItemNo;
    @Size(max = 40)
    @Column(name = "Description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Fee")
    private BigDecimal fee;
    @JoinColumn(name = "BillNo", referencedColumnName = "BillNo")
    @ManyToOne
    private Bill billNo;

    public Billitem() {
    }

    public Billitem(String billItemNo) {
        this.billItemNo = billItemNo;
    }

    public String getBillItemNo() {
        return billItemNo;
    }

    public void setBillItemNo(String billItemNo) {
        this.billItemNo = billItemNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Bill getBillNo() {
        return billNo;
    }

    public void setBillNo(Bill billNo) {
        this.billNo = billNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billItemNo != null ? billItemNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billitem)) {
            return false;
        }
        Billitem other = (Billitem) object;
        if ((this.billItemNo == null && other.billItemNo != null) || (this.billItemNo != null && !this.billItemNo.equals(other.billItemNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Billitem[ billItemNo=" + billItemNo + " ]";
    }
    
}
