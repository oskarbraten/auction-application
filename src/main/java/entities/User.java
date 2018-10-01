package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Persistent class for the User database table. Class defining the main system
 * user entity.
 */

@Entity(name = "\"user\"")
public class User {
	
	/** Variables */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String email;
	private String name;
	private String phone;

	@OneToMany(mappedBy = "user")
	private List<Product> products;
	
	@OneToMany(mappedBy = "user")
	private List<Bid> bids;

	/** Constructors */
	public User() {}

	public User(String email, String name, String phone) {
		this.email = email;
		this.name = name;
		this.phone = phone;
		
		this.products = new ArrayList<Product>();
		this.bids = new ArrayList<Bid>();
	}
	
	
	
	/** Data services */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@XmlTransient
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@XmlTransient
	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

}

