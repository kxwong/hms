/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "receipt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Receipt.findAll", query = "SELECT r FROM Receipt r")
    , @NamedQuery(name = "Receipt.findAllOrderDesc", query = "SELECT r FROM Receipt r ORDER BY r.receiptNo desc")
    , @NamedQuery(name = "Receipt.findByReceiptNo", query = "SELECT r FROM Receipt r WHERE r.receiptNo = :receiptNo")
    , @NamedQuery(name = "Receipt.findByDescription", query = "SELECT r FROM Receipt r WHERE r.description = :description")
    , @NamedQuery(name = "Receipt.findByGenerateDate", query = "SELECT r FROM Receipt r WHERE r.generateDate = :generateDate")
    , @NamedQuery(name = "Receipt.findByPaidDate", query = "SELECT r FROM Receipt r WHERE r.paidDate = :paidDate")})
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "ReceiptNo")
    private String receiptNo;
    @Size(max = 100)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GenerateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generateDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PaidDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;
    @OneToOne(mappedBy = "receiptNo")
    private Bill bill;

    public Receipt() {
    }

    public Receipt(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Receipt(String receiptNo, Date generateDate, Date paidDate) {
        this.receiptNo = receiptNo;
        this.generateDate = generateDate;
        this.paidDate = paidDate;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    @XmlTransient
    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receiptNo != null ? receiptNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Receipt)) {
            return false;
        }
        Receipt other = (Receipt) object;
        if ((this.receiptNo == null && other.receiptNo != null) || (this.receiptNo != null && !this.receiptNo.equals(other.receiptNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Receipt[ receiptNo=" + receiptNo + " ]";
    }

}
