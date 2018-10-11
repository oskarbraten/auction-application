package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
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
	
	@OneToMany(mappedBy = "auction", cascade = CascadeType.PERSIST)
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
		product.getAuctions().add(this);
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.length = length;
		this.bids = new ArrayList<>();
	}

	public Auction(Product product, double startingPrice, double buyoutPrice, long startTime, long length) {
		this.product = product;
        product.getAuctions().add(this);
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.startTime = startTime;
		this.length = length;
		this.bids = new ArrayList<>();
	}

	/** Data services */
	public boolean isPublished() {

		long now =  new Date().getTime();

		if (startTime == null) { //No start time has been set
			return false;
		}else if (startTime > now) { //If the start date is after (>) current date
			return false;
		} else { //The start date is in the past so it's published.
			return true;
		}
	}

	public boolean isFinished() {
		Date now = new Date(); //Not thread safe! Unnecessary resource allocation?
		if (startTime == null) { //No start time has been set, not started means not finished
			return false;
		}else if (startTime + length > now.getTime()) { //If the current time is less than start time plus running time
			return false;
		} else { //The auction has started and run for it's intended time
			return true;
		}
	}

    /**
     * returns the highest bid value
     *
     * @return Bid
     */
    public Bid findHighestBid() {

        double highestBid = Double.MIN_VALUE;
        Bid bid = null;

        for (int i = 0; i < bids.size(); i++) {

            if (highestBid < bids.get(i).getAmount()) {

                highestBid = bids.get(i).getAmount();
                bid = bids.get(i);
            }
        }

        return bid;
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
	}
}
