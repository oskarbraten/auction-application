package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Persistent class for the Person database table. Class and system entity
 * defining the act of making a review on a sold item.
 */

@Entity(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "feedback")
    private Auction auction;

    @OneToOne
    private Person person;

    private String text;
    private Integer sellerRating;
    private Integer productRating;

    public Feedback() {
    }

    public Feedback(Auction auction, Person person, String text, int sellerRating, int productRating) {
        this.auction = auction;
        this.person = person;
        this.text = text;
        this.sellerRating = sellerRating;
        this.productRating = productRating;
    }

    /**
     * Data services
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @XmlTransient
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

    public Integer getProductRating() {
        return productRating;
    }

    public void setProductRating(Integer productRating) {
        this.productRating = productRating;
    }
}
