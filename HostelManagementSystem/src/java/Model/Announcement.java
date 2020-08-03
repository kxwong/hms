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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "announcement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Announcement.findAll", query = "SELECT a FROM Announcement a")
    , @NamedQuery(name = "Announcement.findAllByDesc", query = "SELECT a FROM Announcement a ORDER BY a.date DESC")
    , @NamedQuery(name = "Announcement.findAllOrderDesc", query = "SELECT a FROM Announcement a ORDER BY a.announceID DESC")
    , @NamedQuery(name = "Announcement.findAllOrderByDateDesc", query = "SELECT a FROM Announcement a ORDER BY a.date DESC")
    , @NamedQuery(name = "Announcement.findByAnnounceID", query = "SELECT a FROM Announcement a WHERE a.announceID = :announceID")
    , @NamedQuery(name = "Announcement.findByTitle", query = "SELECT a FROM Announcement a WHERE a.title = :title")
    , @NamedQuery(name = "Announcement.findByContent", query = "SELECT a FROM Announcement a WHERE a.content = :content")
    , @NamedQuery(name = "Announcement.findByDate", query = "SELECT a FROM Announcement a WHERE a.date = :date")})
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "AnnounceID")
    private String announceID;
    @Size(max = 100)
    @Column(name = "Title")
    private String title;
    @Size(max = 3000)
    @Column(name = "Content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinTable(name = "announceattachment", joinColumns = {
        @JoinColumn(name = "AnnounceID", referencedColumnName = "AnnounceID")}, inverseJoinColumns = {
        @JoinColumn(name = "AttachID", referencedColumnName = "AttachID")})
    @ManyToMany
    private List<Attachment> attachmentList;

    public Announcement() {
    }

    public Announcement(String announceID) {
        this.announceID = announceID;
    }

    public Announcement(String announceID, Date date) {
        this.announceID = announceID;
        this.date = date;
    }

    public String getAnnounceID() {
        return announceID;
    }

    public void setAnnounceID(String announceID) {
        this.announceID = announceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlTransient
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (announceID != null ? announceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) object;
        if ((this.announceID == null && other.announceID != null) || (this.announceID != null && !this.announceID.equals(other.announceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Announcement[ announceID=" + announceID + " ]";
    }
    
}
