/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kxwong
 */
@Entity
@Table(name = "contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c")
    , @NamedQuery(name = "Contact.findByHostellerID", query = "SELECT c FROM Contact c WHERE c.hostellerID = :hostellerID")
    , @NamedQuery(name = "Contact.findByAddress", query = "SELECT c FROM Contact c WHERE c.address = :address")
    , @NamedQuery(name = "Contact.findByPostcode", query = "SELECT c FROM Contact c WHERE c.postcode = :postcode")
    , @NamedQuery(name = "Contact.findByCity", query = "SELECT c FROM Contact c WHERE c.city = :city")
    , @NamedQuery(name = "Contact.findByState", query = "SELECT c FROM Contact c WHERE c.state = :state")
    , @NamedQuery(name = "Contact.findByCountry", query = "SELECT c FROM Contact c WHERE c.country = :country")
    , @NamedQuery(name = "Contact.findByMobilePhone", query = "SELECT c FROM Contact c WHERE c.mobilePhone = :mobilePhone")
    , @NamedQuery(name = "Contact.findByHomePhone", query = "SELECT c FROM Contact c WHERE c.homePhone = :homePhone")
    , @NamedQuery(name = "Contact.findByEmail", query = "SELECT c FROM Contact c WHERE c.email = :email")})
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "HostellerID")
    private String hostellerID;
    @Size(max = 60)
    @Column(name = "Address")
    private String address;
    @Size(max = 10)
    @Column(name = "Postcode")
    private String postcode;
    @Size(max = 40)
    @Column(name = "City")
    private String city;
    @Size(max = 30)
    @Column(name = "State")
    private String state;
    @Size(max = 30)
    @Column(name = "Country")
    private String country;
    @Column(name = "MobilePhone")
    private BigInteger mobilePhone;
    @Column(name = "HomePhone")
    private BigInteger homePhone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "Email")
    private String email;
    @JoinColumn(name = "HostellerID", referencedColumnName = "HostellerID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Hosteller hosteller;

    public Contact() {
    }

    public Contact(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public String getHostellerID() {
        return hostellerID;
    }

    public void setHostellerID(String hostellerID) {
        this.hostellerID = hostellerID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigInteger getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(BigInteger mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public BigInteger getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(BigInteger homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Hosteller getHosteller() {
        return hosteller;
    }

    public void setHosteller(Hosteller hosteller) {
        this.hosteller = hosteller;
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
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        if ((this.hostellerID == null && other.hostellerID != null) || (this.hostellerID != null && !this.hostellerID.equals(other.hostellerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Contact[ hostellerID=" + hostellerID + " ]";
    }
    
}
