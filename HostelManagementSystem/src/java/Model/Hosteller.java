package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "hosteller")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hosteller.findAll", query = "SELECT h FROM Hosteller h")
    , @NamedQuery(name = "Hosteller.findByEntCardNo", query = "SELECT h FROM Hosteller h WHERE h.entCardNo.entCardNo = :entCardNo")
    , @NamedQuery(name = "Hosteller.findHostellerByRegreq", query = "SELECT h FROM Hosteller h WHERE h.regReqNo.requestNo = :requestNo")
    , @NamedQuery(name = "Hosteller.findByAccount", query = "SELECT h FROM Hosteller h WHERE h.username = :username")
    , @NamedQuery(name = "Hosteller.findByRoom", query = "SELECT h FROM Hosteller h WHERE h.stayRoom.roomNo = :roomNo")
    , @NamedQuery(name = "Hosteller.findByHostellerID", query = "SELECT h FROM Hosteller h WHERE h.hostellerID = :hostellerID")
    , @NamedQuery(name = "Hosteller.findByIdentificationNo", query = "SELECT h FROM Hosteller h WHERE h.identificationNo = :identificationNo")
    , @NamedQuery(name = "Hosteller.findByFirstName", query = "SELECT h FROM Hosteller h WHERE h.firstName = :firstName")
    , @NamedQuery(name = "Hosteller.findByMiddleName", query = "SELECT h FROM Hosteller h WHERE h.middleName = :middleName")
    , @NamedQuery(name = "Hosteller.findByLastName", query = "SELECT h FROM Hosteller h WHERE h.lastName = :lastName")
    , @NamedQuery(name = "Hosteller.findByDoB", query = "SELECT h FROM Hosteller h WHERE h.doB = :doB")
    , @NamedQuery(name = "Hosteller.findByGender", query = "SELECT h FROM Hosteller h WHERE h.gender = :gender")
    , @NamedQuery(name = "Hosteller.findByNationality", query = "SELECT h FROM Hosteller h WHERE h.nationality = :nationality")
    , @NamedQuery(name = "Hosteller.findByStatus", query = "SELECT h FROM Hosteller h WHERE h.status = :status")
    , @NamedQuery(name = "Hosteller.findByRemark", query = "SELECT h FROM Hosteller h WHERE h.remark = :remark")})
public class Hosteller implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "HostellerID")
    private String hostellerID;
    @Size(max = 20)
    @Column(name = "IdentificationNo")
    private String identificationNo;
    @Size(max = 40)
    @Column(name = "FirstName")
    private String firstName;
    @Size(max = 40)
    @Column(name = "MiddleName")
    private String middleName;
    @Size(max = 40)
    @Column(name = "LastName")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DoB")
    @Temporal(TemporalType.TIMESTAMP)
    private Date doB;
    @Size(max = 1)
    @Column(name = "Gender")
    private String gender;
    @Size(max = 30)
    @Column(name = "Nationality")
    private String nationality;
    @Lob
    @Column(name = "Image")
    private byte[] image;
    @Size(max = 10)
    @Column(name = "Status")
    private String status;
    @Size(max = 100)
    @Column(name = "Remark")
    private String remark;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "hosteller")
    private Empdetails empdetails;
    @OneToMany(mappedBy = "issueBy")
    private List<Issue> issueList;
    @OneToMany(mappedBy = "requestBy")
    private List<Roombooking> roombookingList;
    @OneToMany(mappedBy = "issueTo")
    private List<Bill> billList;
    @OneToMany(mappedBy = "bookBy")
    private List<Facilitybooking> facilitybookingList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "hosteller")
    private Contact contact;
    @JoinColumn(name = "RegReqNo", referencedColumnName = "RequestNo")
    @OneToOne
    private Registrationreq regReqNo;
    @JoinColumn(name = "EntCardNo", referencedColumnName = "EntCardNo")
    @OneToOne
    private Entcard entCardNo;
    @JoinColumn(name = "Username", referencedColumnName = "Username")
    @OneToOne
    private Account username;
    @JoinColumn(name = "StayRoom", referencedColumnName = "RoomNo")
    @ManyToOne
    private Room stayRoom;

    public Hosteller() {
    }

    public Hosteller(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public Hosteller(String hostellerID, Date doB) {
        this.hostellerID = hostellerID;
        this.doB = doB;
    }

    public String getHostellerID() {
        return hostellerID;
    }

    public void setHostellerID(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDoB() {
        return doB;
    }

    public void setDoB(Date doB) {
        this.doB = doB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public Empdetails getEmpdetails() {
        return empdetails;
    }

    public void setEmpdetails(Empdetails empdetails) {
        this.empdetails = empdetails;
    }

    @XmlTransient
    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    @XmlTransient
    public List<Roombooking> getRoombookingList() {
        return roombookingList;
    }

    public void setRoombookingList(List<Roombooking> roombookingList) {
        this.roombookingList = roombookingList;
    }

    @XmlTransient
    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @XmlTransient
    public List<Facilitybooking> getFacilitybookingList() {
        return facilitybookingList;
    }

    public void setFacilitybookingList(List<Facilitybooking> facilitybookingList) {
        this.facilitybookingList = facilitybookingList;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Registrationreq getRegReqNo() {
        return regReqNo;
    }

    public void setRegReqNo(Registrationreq regReqNo) {
        this.regReqNo = regReqNo;
    }

    public Entcard getEntCardNo() {
        return entCardNo;
    }

    public void setEntCardNo(Entcard entCardNo) {
        this.entCardNo = entCardNo;
    }

    public Account getUsername() {
        return username;
    }

    public void setUsername(Account username) {
        this.username = username;
    }

    public Room getStayRoom() {
        return stayRoom;
    }

    public void setStayRoom(Room stayRoom) {
        this.stayRoom = stayRoom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hostellerID != null ? hostellerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hosteller)) {
            return false;
        }
        Hosteller other = (Hosteller) object;
        if ((this.hostellerID == null && other.hostellerID != null) || (this.hostellerID != null && !this.hostellerID.equals(other.hostellerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Hosteller[ hostellerID=" + hostellerID + " ]";
    }

}
