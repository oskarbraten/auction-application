package entities;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *Persistence class for address database table. Class and system entity.
 * Defining a users place of residence for shipping and billing purposes.
 */

@Entity(name = "address")
@XmlRootElement
public class Address {

    /** Variables */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // @OneToOne(mappedBy = "address")
    // private User user;

    private String street;
    private String city;
    private String county;
    private String postalCode; // Maybe change to String?
    private String country; // Using ISO3166-1 letter codes to make it a locale enum type

    /** Constructor */
    public Address(){

    }
    public Address(/*User user,*/ String street, String city, String county, String postalCode, String country){
        //this.user = user;
        this.street = street;
        this.city = city;
        this.county = county;
        this.postalCode = postalCode;
        this.country = country;
    }

    /** Data services */
    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    //@XmlTransient
    //public User getUser() {return user; }

    //public void setUser(User user) { this.user = user;}

    public String getStreet() {return street;}

    public void setStreet(String street) {this.street = street;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getCounty() {return county;}

    public void setCounty(String county) {this.county = county;}

    public String getPostalCode() {return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getCountry() {return country;}

    public void setCountry(String country) {this.country = country;}
}