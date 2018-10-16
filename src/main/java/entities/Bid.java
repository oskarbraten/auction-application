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
 * Persistent class for the User database table. Class and system entity
 * defining the act of placing a bid on an item for sale.
 */

@Entity(name = "bid")
@XmlRootElement
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "auction_id")
	private Auction auction;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private Double amount;

	public Bid() {}

	public Bid(Auction auction, User user, Double amount) {
		this.auction = auction;
		this.user = user;
		this.amount = amount;

		user.getBids().add(this);
        auction.getBids().add(this);
	}

	/** Data services */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
