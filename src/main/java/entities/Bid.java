package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Persistent class for the Person database table. Class and system entity
 * defining the act of placing a bid on an item for sale.
 */

@Entity(name = "bid")
@XmlRootElement
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Auction auction;

    @ManyToOne
    private Person person;

    private Double amount;

    public Bid() {
    }

    public Bid(Auction auction, Person person, Double amount) {
        this.auction = auction;
        this.person = person;
        this.amount = amount;

        person.getBids().add(this);

        auction.getBids().add(this);
        auction.updateHighestBid();
    }

    public boolean isPurchase() {
        return this.auction.isComplete() && (this.auction.getHighestBid() == this);
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
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @XmlTransient
    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}
