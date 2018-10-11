package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Persistent class for the Product database table. Class defining the sales
 * item system entity.
 */

@Entity(name = "product")
@XmlRootElement
public class Product {

	/** Variables */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
	private List<Auction> auctions;

	private String name;
	private String description;
	private String features;
	private String picture;

	/** Constructors */
	public Product() {
	}

	public Product(String name, String description, String features, String picture, User user) {
		this.name = name;
		this.description = description;
		this.features = features;
		this.picture = picture;
		this.user = user;

		this.auctions = new ArrayList<Auction>();
	}
	
	/** Data services */
	public double getRating(){
		double rating = 0;
		int numbOfRatings = 0;

		Iterator aIt = auctions.iterator();
		if(aIt.hasNext()) {
			while (aIt.hasNext()) {
				Auction a = (Auction) aIt.next();
				Feedback f = a.getFeedback();
				if (f != null || f.getProductRating() != null) { //Feedback exists and a rating exists
					rating += f.getProductRating();
					numbOfRatings++;
				}
			}
		}else {
			return -1; //No rating exist
		}

		return (rating/numbOfRatings);
	}

	public double getSellersProductRating(){
		double rating = 0;
		int numbOfRatings = 0;

		Iterator aIt = auctions.iterator();

		if(aIt.hasNext()) {
			while (aIt.hasNext()) {
				Auction a = (Auction) aIt.next();
				Feedback f = a.getFeedback();
				if (f != null || f.getSellerRating() != null) { //Feedback exists and a rating exists
					rating += f.getSellerRating();
					numbOfRatings++;
				}
			}
		} else {
			return -1; //No rating exist
		}

		return (rating/numbOfRatings);
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@XmlTransient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@XmlTransient
	public List<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(ArrayList<Auction> auctions) {
		this.auctions = auctions;
	}
}