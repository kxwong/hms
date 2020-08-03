/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "issue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Issue.findAll", query = "SELECT i FROM Issue i")
    , @NamedQuery(name = "Issue.findCategory", query = "SELECT distinct(i.category) FROM Issue i ORDER BY i.category ASC" )
    , @NamedQuery(name = "Issue.findAllByDateRange", query = "SELECT i FROM Issue i WHERE i.issueDate BETWEEN :startDate AND :enddate ORDER BY i.issueDate")
    , @NamedQuery(name = "Issue.findAllByDateRangeCategory", query = "SELECT i FROM Issue i WHERE i.issueDate BETWEEN :startDate AND :enddate AND i.category = :category ORDER BY i.issueDate")
    , @NamedQuery(name = "Issue.findAllByStatus", query = "SELECT i FROM Issue i WHERE i.status = :status")
    , @NamedQuery(name = "Issue.findAllByHosteller", query = "SELECT i FROM Issue i WHERE i.issueBy = :issueBy")
    , @NamedQuery(name = "Issue.findIssueDate", query = "SELECT distinct(i.issueDate) FROM Issue i ORDER BY i.issueDate ASC" )
    , @NamedQuery(name = "Issue.findUpdateDate", query = "SELECT distinct(i.updateDate) FROM Issue i ORDER BY i.updateDate ASC" )
    , @NamedQuery(name = "Issue.findByCaseNo", query = "SELECT i FROM Issue i WHERE i.caseNo = :caseNo")
    , @NamedQuery(name = "Issue.findByTitle", query = "SELECT i FROM Issue i WHERE i.title = :title")
    , @NamedQuery(name = "Issue.findByCategory", query = "SELECT i FROM Issue i WHERE i.category = :category")
    , @NamedQuery(name = "Issue.findByIssueType", query = "SELECT i FROM Issue i WHERE i.issueType = :issueType")
    , @NamedQuery(name = "Issue.findByIssueDate", query = "SELECT i FROM Issue i WHERE i.issueDate = :issueDate")
    , @NamedQuery(name = "Issue.findByUpdateDate", query = "SELECT i FROM Issue i WHERE i.updateDate = :updateDate")
    , @NamedQuery(name = "Issue.findByStatus", query = "SELECT i FROM Issue i WHERE i.status = :status")})
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CaseNo")
    private String caseNo;
    @Size(max = 100)
    @Column(name = "Title")
    private String title;
    @Size(max = 100)
    @Column(name = "Category")
    private String category;
    @Size(max = 100)
    @Column(name = "IssueType")
    private String issueType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IssueDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 10)
    @Column(name = "Status")
    private String status;
    @JoinTable(name = "issueattachment", joinColumns = {
        @JoinColumn(name = "CaseNo", referencedColumnName = "CaseNo")}, inverseJoinColumns = {
        @JoinColumn(name = "AttachID", referencedColumnName = "AttachID")})
    @ManyToMany
    private List<Attachment> attachmentList;
    @JoinColumn(name = "IssueBy", referencedColumnName = "HostellerID")
    @ManyToOne
    private Hosteller issueBy;
    @OneToMany(mappedBy = "caseNumber")
    private List<Conversation> conversationList;

    public Issue() {
    }

    public Issue(String caseNo) {
        this.caseNo = caseNo;
    }

    public Issue(String caseNo, Date issueDate, Date updateDate) {
        this.caseNo = caseNo;
        this.issueDate = issueDate;
        this.updateDate = updateDate;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Hosteller getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(Hosteller issueBy) {
        this.issueBy = issueBy;
    }

    @XmlTransient
    public List<Conversation> getConversationList() {
        return conversationList;
    }

    public void setConversationList(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caseNo != null ? caseNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.caseNo == null && other.caseNo != null) || (this.caseNo != null && !this.caseNo.equals(other.caseNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Issue[ caseNo=" + caseNo + " ]";
    }
    
}
