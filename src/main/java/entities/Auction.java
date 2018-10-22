package entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Persistent class for the User database table. Class and system entity
 * defining the act of selling a product.
 */

@Entity(name = "auction")
@XmlRootElement
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Product product;
	
	@OneToMany(mappedBy = "auction", cascade = CascadeType.PERSIST)
	private List<Bid> bids;

	@OneToOne
	private Bid highestBid;
	
	@OneToOne
	private Feedback feedback;

	private Double startingPrice;
	private Double buyoutPrice;

	private Long startTime;
	private Long length;

	public Auction() {}

	public Auction(Product product, double startingPrice, double buyoutPrice, long length) {
		this.product = product;
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.length = length;
		this.bids = new ArrayList<>();

        product.getAuctions().add(this);
	}

	public Auction(Product product, double startingPrice, double buyoutPrice, long startTime, long length) {
		this.product = product;
		this.startingPrice = startingPrice;
		this.buyoutPrice = buyoutPrice;
		this.startTime = startTime;
		this.length = length;
		this.bids = new ArrayList<>();

        product.getAuctions().add(this);
	}

	/** Data services */

    public void publish() {
        this.startTime = new Date().getTime();
    }

	public boolean isPublished() {
		long now =  new Date().getTime();
		return (startTime != null && startTime <= now); // no start time has been set, or start time is after (>) current time
	}

	public boolean isFinished() {
        long now =  new Date().getTime();
		return !(startTime == null || startTime > now || startTime + length > now); // not started, or not finished
	}

	public boolean isBoughtOut() {
		return (highestBid != null && highestBid.getAmount() == buyoutPrice );
	}

	public boolean isComplete() {
	    return (isFinished() || isBoughtOut());
    }

    public void updateHighestBid() {

        double highestBid = Double.MIN_VALUE;
        Bid bid = null;

        for (int i = 0; i < bids.size(); i++) {

            if (highestBid < bids.get(i).getAmount()) {

                highestBid = bids.get(i).getAmount();
                bid = bids.get(i);
            }

        }

        this.highestBid = bid;

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

    public Bid getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Bid highestBid) {
        this.highestBid = highestBid;
    }
}
