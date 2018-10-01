package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Persistent class for the User database table. Class and system entity
 * defining the act of selling a product.
 */

@Entity(name = "auction")
@XmlRootElement
public class Auction {
	
	/** Variables */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Product product;
	
	
	@OneToMany(mappedBy = "auction")
	private List<Bid> bids;
	
	@OneToOne
	private Feedback feedback;

	private Double startingPrice;
	private Double buyoutPrice;
	private Long startTime;
	private Long length;

	/** Constructor */
	public Auction() {
	}

	public Auction(Product product, double startingPrice, double buyoutPrice, long length) {
		this.product = product;
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.length = length;
		
		this.bids = new ArrayList<>();
	}

	public Auction(Product product, double startingPrice, double buyoutPrice, long startTime, long length) {
		this.product = product;
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.startTime = startTime;
		this.length = length;
		
		this.bids = new ArrayList<>();
	}

	/** Data services */
	public boolean isPublished() {
		Date now = new Date(); //Not thread safe! Unnecessary resource allocation?
		if (startTime == null) { //No start time has been set
			return false;
		}else if (startTime > now.getTime()) { //If the start date is after (>) current date
			return false;
		} else { //The start date is in the past so it's published.
			return true;
		}
	}

	public boolean isfinished() {
		Date now = new Date(); //Not thread safe! Unnecessary resource allocation?
		if (startTime == null) { //No start time has been set, not started means not finished
			return false;
		}else if (startTime + length > now.getTime()) { //If the current time is less than start time plus running time
			return false;
		} else { //The auction has started and run for it's intended time
			return true;
		}
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	@XmlTransient
	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	@XmlTransient
	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}

	public Double getBuyoutPrice() {
		return buyoutPrice;
	}

	public void setBuyoutPrice(Double buyoutPrice) {
		this.buyoutPrice = buyoutPrice;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	};
	
}
