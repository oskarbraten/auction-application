package ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Auction;
import entities.Bid;
import entities.Product;
import entities.User;

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
		
		/** New Users */
		User player1 = new User("test@test.no", "Test Testosteron", "43214321");
		User player2 = new User("seller@meow.cat", "Meow Cat", "12341234");
		
		/** Products */
		// Product(String name, String description, String features, String picture, User user)
		Product bitcoinPick = new Product("Bitcoin Mining Pick", "Pickaxe for bitcoin mining. Get you pick and start mining TODAY", 
				"Egronomic design, Eficient form, ECO Freindly", "https://image.shutterstock.com/image-vector/bitcoin-cryptocurrency-mining-pickaxe-minimal-450w-675558532.jpg",
				player2);
		Product velocipede = new Product("Velocipede", "Experience the JOY of historic cycling. Guaranteed to give you an unforgetable experience, Health inshurance not included",
				"Has weels, Purple finish", "https://cdn3.vectorstock.com/i/1000x1000/88/62/velocipede-vector-948862.jpg", player1);
		
		/** Auctions */
		// Auction(Product product, double startingPrice, double buyoutPrice, long length) {
		// Auction(Product product, double startingPrice, double buyoutPrice, long startTime, long length)
		Auction auction1 = new Auction(bitcoinPick, 10, 12.89, 24);
		Auction auction2 = new Auction(bitcoinPick, 12.89, 12.89,27092018, 24);
		Auction auction3 = new Auction(velocipede, 79, 179, 27092018, 48);
		
		/** Bids */
		// Bid(Auction auction, User user, Double amount)
		Bid bid1 = new Bid(auction2, player1, 12.00);
		Bid bid2 = new Bid(auction2, player1, 12.89);
		
		/** Feedback */
		
		/** Persist elements */
		
		em.persist(player1);
		em.persist(player2);
		
		em.persist(bitcoinPick);
		em.persist(velocipede);
		
		em.persist(auction1);
		em.persist(auction2);
		em.persist(auction3);
		
		em.persist(bid1);
		em.persist(bid2);
		
		
//		try {
			
//		} catch (NamingException e) {// ONE TRUE Exception handler
//			if (Desktop.isDesktopSupported()) {
//			    try {
//					Desktop.getDesktop().browse(new URI("https://stackoverflow.com/search?q=[js] + " + e.getMessage()));
//				} catch (IOException e1) {
//					System.out.println("Give up and go home!");
//				} catch (URISyntaxException e1) {
//					System.out.println("Give up and go home!");
//				} 
//			}
//			else {
//			System.out.println("Give up and go home!");
//			// e.printStackTrace();
//			}
//		}
		
//		catch (JMSException e) {
//			if (Desktop.isDesktopSupported()) {
//			    try {
//					Desktop.getDesktop().browse(new URI("https://stackoverflow.com/search?q=[js] + " + e.getMessage()));
//				} catch (IOException e1) {
//					System.out.println("Give up and go home!");
//				} catch (URISyntaxException e1) {
//					System.out.println("Give up and go home!");
//				} 
//			}
//			else {
//			System.out.println("Give up and go home!");
//			// e.printStackTrace();
//			}
//		}
	}
}
