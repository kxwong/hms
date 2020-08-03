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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "conversation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conversation.findAll", query = "SELECT c FROM Conversation c")
    , @NamedQuery(name = "Conversation.findByCaseOrderByTime", query = "SELECT c FROM Conversation c WHERE c.caseNumber.caseNo = :caseNumber ORDER BY c.time ASC")
    , @NamedQuery(name = "Conversation.findByCaseOrderIDDesc", query = "SELECT c FROM Conversation c WHERE c.caseNumber.caseNo = :caseNumber ORDER BY c.contentID Desc")
    , @NamedQuery(name = "Conversation.findAllByCaseID", query = "SELECT c FROM Conversation c WHERE c.caseNumber = :caseNumber")
    , @NamedQuery(name = "Conversation.findByContentID", query = "SELECT c FROM Conversation c WHERE c.contentID = :contentID")
    , @NamedQuery(name = "Conversation.findByContent", query = "SELECT c FROM Conversation c WHERE c.content = :content")
    , @NamedQuery(name = "Conversation.findByTime", query = "SELECT c FROM Conversation c WHERE c.time = :time")})
public class Conversation implements Serializable {

    @Column(name = "Time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "ContentID")
    private String contentID;
    @Size(max = 200)
    @Column(name = "Content")
    private String content;
    @JoinColumn(name = "CaseNumber", referencedColumnName = "CaseNo")
    @ManyToOne
    private Issue caseNumber;
    @JoinColumn(name = "ReplyBy", referencedColumnName = "Username")
    @ManyToOne
    private Account replyBy;

    public Conversation() {
    }

    public Conversation(String contentID) {
        this.contentID = contentID;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Issue getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Issue caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Account getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(Account replyBy) {
        this.replyBy = replyBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contentID != null ? contentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conversation)) {
            return false;
        }
        Conversation other = (Conversation) object;
        if ((this.contentID == null && other.contentID != null) || (this.contentID != null && !this.contentID.equals(other.contentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Conversation[ contentID=" + contentID + " ]";
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
}
