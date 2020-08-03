/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a")
    , @NamedQuery(name = "Attachment.findAllOrderDesc", query = "SELECT a FROM Attachment a ORDER BY a.attachID DESC")
    , @NamedQuery(name = "Attachment.findByAttachID", query = "SELECT a FROM Attachment a WHERE a.attachID = :attachID")})
public class Attachment implements Serializable {

    @Size(max = 100)
    @Column(name = "Header")
    private String header;
    @Lob
    @Column(name = "File")
    private byte[] file;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "AttachID")
    private String attachID;
    @ManyToMany(mappedBy = "attachmentList")
    private List<Issue> issueList;
    @ManyToMany(mappedBy = "attachmentList")
    private List<Announcement> announcementList;

    public Attachment() {
    }

    public Attachment(String attachID) {
        this.attachID = attachID;
    }

    public String getAttachID() {
        return attachID;
    }

    public void setAttachID(String attachID) {
        this.attachID = attachID;
    }


    @XmlTransient
    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    @XmlTransient
    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachID != null ? attachID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.attachID == null && other.attachID != null) || (this.attachID != null && !this.attachID.equals(other.attachID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Attachment[ attachID=" + attachID + " ]";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    
}
