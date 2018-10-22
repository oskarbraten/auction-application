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

    @Id
    private String username;
    private String password; // TODO: add proper security.

	@OneToOne(cascade = CascadeType.PERSIST)
	private Address address;

	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Product> products;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private List<Bid> bids;

	private String email;
	private String name;
	private String phone;

	public User() {}

	public User(String username, String password, String email, String name, String phone, Address address) {
	    this.username = username;
	    this.password = password;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
		
		this.products = new ArrayList<>();
		this.bids = new ArrayList<>();
	}

	public List<Auction> findAuctions() {
	    ArrayList<Auction> auctions = new ArrayList<>();
	    for (Product p : this.products) {
	        auctions.addAll(p.getAuctions());
        }

        return auctions;
    }

    /** Data services */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

	public Address getAddress() {return address;}

	public void setAddress(Address address) {this.address = address;}

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

