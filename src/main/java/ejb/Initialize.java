package ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.*;

import java.util.Date;

/**
 * Singleton JE Bean creating and persisting a test dataset at application startup
 */

@Singleton
@Startup
public class Initialize {
	
	@PersistenceContext(unitName="AuctionApplicationPU")
	private EntityManager em;
	
	@PostConstruct
	public void initialize() {

		/** New address */
		Address address1 = new Address("Hab 1", "Halley", "Queen Elizabet Land" , "BIQQ 1ZZ", "ATA");
		Address address2 = new Address("Sesame Street -5", "Sin City", "Lost County" , "BBQ4U", "SJM");

		/** New Users */
		User player1 = new User("test@test.no", "Test Testosteron", "43214321", address1);
		User player2 = new User("seller@meow.cat", "Meow Cat", "12341234", address2);

		/** Products */
		// Product(String name, String description, String features, String picture, User user)
		Product bitcoinPick = new Product("Bitcoin Mining Pick", "Pickaxe for bitcoin mining. Get you pick and start mining TODAY",
				"Ergonomic design, Efficient form, ECO Friendly", "https://image.shutterstock.com/image-vector/bitcoin-cryptocurrency-mining-pickaxe-minimal-450w-675558532.jpg",
				player2);
		Product velocipede = new Product("Velocipede", "Experience the JOY of historic cycling. Guaranteed to give you an unforgetable experience, Health inshurance not included",
				"Has wheels, Purple finish", "https://cdn3.vectorstock.com/i/1000x1000/88/62/velocipede-vector-948862.jpg", player1);

		/** Auctions */
		// Auction(Product product, double startingPrice, double buyoutPrice, long length) {
		// Auction(Product product, double startingPrice, double buyoutPrice, long startTime, long length)
		Auction auction1 = new Auction(bitcoinPick, 10, 12.89, 24);
		Auction auction2 = new Auction(bitcoinPick, 12.89, 12.89, new Date().getTime(), 1000 * 60 * 60);
		Auction auction3 = new Auction(velocipede, 79, 179, new Date().getTime() + 1000 * 60 * 2, 1000 * 60 * 60);
		
		/** Feedback */
		
		/** Persist elements */
		
		em.persist(player1);
		em.persist(player2);

	}
}
