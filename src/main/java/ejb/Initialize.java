package ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.*;
import misc.ApplicationConstants;

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
		Person person1 = new Person("test", "test", "test@test.no", "Test Testosteron", "43214321", address1);
		Person person2 = new Person("meow", "meow", "seller@meow.cat", "Meow Cat", "12341234", address2);

		/** Create groups */
		Party user1Party = new Party(person1.getUsername(), ApplicationConstants.USER_PARTY);
		Party user2Party = new Party(person2.getUsername(), ApplicationConstants.USER_PARTY);

		/** Products */
		// Product(String name, String description, String features, String picture, Person user)
		Product bitcoinPick = new Product("Bitcoin Mining Pick", "Pickaxe for bitcoin mining. Get you pick and start mining TODAY!",
				"Ergonomic design, Efficient form, ECO Friendly", "https://image.shutterstock.com/image-vector/bitcoin-cryptocurrency-mining-pickaxe-minimal-450w-675558532.jpg",
                person2);
		Product velocipede = new Product("Velocipede", "Experience the JOY of historic cycling. Guaranteed to give you an unforgettable experience. Health insurance not included..",
				"Has wheels, Purple finish", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/The_American_Velocipede.jpg/225px-The_American_Velocipede.jpg", person1);

		/** Auctions */
		Auction auction1 = new Auction(bitcoinPick, 10, 79, 1000 * 60 * 30);
		Auction auction2 = new Auction(bitcoinPick, 20, 129.99, new Date().getTime(), 1000 * 60 * 60);
		Auction auction3 = new Auction(velocipede, 79, 179, new Date().getTime() + 1000 * 10, 1000 * 60 * 60);
		
		/** Persist elements */
		em.persist(person1);
		em.persist(person2);

		em.persist(user1Party);
		em.persist(user2Party);

	}
}
