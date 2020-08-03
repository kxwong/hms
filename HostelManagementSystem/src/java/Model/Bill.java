/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "bill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bill.findAll", query = "SELECT b FROM Bill b")
    , @NamedQuery(name = "Bill.findAllByDateRange", query = "SELECT b FROM Bill b WHERE b.issueDate BETWEEN :startDate AND :enddate ORDER BY b.issueDate")
    , @NamedQuery(name = "Bill.findByRoomBooking", query = "SELECT b FROM Bill b WHERE b.roombooking.requestNo = :requestNo")
    , @NamedQuery(name = "Bill.findAllByHosteller", query = "SELECT b FROM Bill b WHERE b.issueTo = :issueTo")
    , @NamedQuery(name = "Bill.findAllMonthYear", query = "SELECT distinct b.issueDate FROM Bill b WHERE b.issueTo = :issueTo")
    , @NamedQuery(name = "Bill.findAllByStatusByHosteller", query = "SELECT b FROM Bill b WHERE b.issueTo = :issueTo AND b.status != :status")
    , @NamedQuery(name = "Bill.findIssueDate", query = "SELECT distinct(b.issueDate) FROM Bill b ORDER BY b.issueDate ASC" )
    , @NamedQuery(name = "Bill.findDueDate", query = "SELECT distinct(b.dueDate) FROM Bill b ORDER BY b.dueDate ASC" )
    , @NamedQuery(name = "Bill.findByBillNo", query = "SELECT b FROM Bill b WHERE b.billNo = :billNo")
    , @NamedQuery(name = "Bill.findByDescription", query = "SELECT b FROM Bill b WHERE b.description = :description")
    , @NamedQuery(name = "Bill.findByIssueDate", query = "SELECT b FROM Bill b WHERE b.issueDate = :issueDate")
    , @NamedQuery(name = "Bill.findByDueDate", query = "SELECT b FROM Bill b WHERE b.dueDate = :dueDate")
    , @NamedQuery(name = "Bill.findByTotalAmount", query = "SELECT b FROM Bill b WHERE b.totalAmount = :totalAmount")
    , @NamedQuery(name = "Bill.findByStatus", query = "SELECT b FROM Bill b WHERE b.status = :status")
    , @NamedQuery(name = "Bill.findAllOrderDesc", query = "SELECT b FROM Bill b ORDER BY b.billNo desc")
    , @NamedQuery(name = "Bill.findByRemark", query = "SELECT b FROM Bill b WHERE b.remark = :remark")})
public class Bill implements Serializable {

    @Column(name = "PaidDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "BillNo")
    private String billNo;
    @Size(max = 100)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IssueDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DueDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TotalAmount")
    private BigDecimal totalAmount;
    @Size(max = 10)
    @Column(name = "Status")
    private String status;
    @Size(max = 100)
    @Column(name = "Remark")
    private String remark;
    @OneToOne(mappedBy = "billNo")
    private Roombooking roombooking;
    @JoinColumn(name = "ReceiptNo", referencedColumnName = "ReceiptNo")
    @OneToOne
    private Receipt receiptNo;
    @JoinColumn(name = "IssueTo", referencedColumnName = "HostellerID")
    @ManyToOne
    private Hosteller issueTo;
    @OneToMany(mappedBy = "billNo")
    private List<Billitem> billitemList;

    public Bill() {
    }

    public Bill(String billNo) {
        this.billNo = billNo;
    }

    public Bill(String billNo, Date issueDate, Date dueDate) {
        this.billNo = billNo;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlTransient
    public Roombooking getRoombooking() {
        return roombooking;
    }

    public void setRoombooking(Roombooking roombooking) {
        this.roombooking = roombooking;
    }

    public Receipt getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(Receipt receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Hosteller getIssueTo() {
        return issueTo;
    }

    public void setIssueTo(Hosteller issueTo) {
        this.issueTo = issueTo;
    }

    @XmlTransient
    public List<Billitem> getBillitemList() {
        return billitemList;
    }

    public void setBillitemList(List<Billitem> billitemList) {
        this.billitemList = billitemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billNo != null ? billNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.billNo == null && other.billNo != null) || (this.billNo != null && !this.billNo.equals(other.billNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Bill[ billNo=" + billNo + " ]";
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }
    
}
