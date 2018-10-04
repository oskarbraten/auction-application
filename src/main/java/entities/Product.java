package entities;

import java.util.ArrayList;

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
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToMany(mappedBy = "product")
	private ArrayList<Auction> auctions;

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
	public ArrayList<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(ArrayList<Auction> auctions) {
		this.auctions = auctions;
	}
}