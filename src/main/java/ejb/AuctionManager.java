package ejb;

import ejb.dao.AuctionDAO;
import ejb.dao.ProductDAO;
import ejb.dao.UserDAO;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;
import entities.Person;
import entities.Product;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Named(value = "auctionManager")
@RolesAllowed("users")
public class AuctionManager {

    @EJB
    AuctionDAO auctionDAO;

    @EJB
    ProductDAO productDAO;

    @EJB
    UserDAO userDAO;

    @Inject
    @JMSConnectionFactory("jms/dat250/ConnectionFactory")
    @JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
    private JMSContext context;

    @Resource(lookup = "jms/dat250/Topic")
    private Topic topic;

    @Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
    public void checkForPurchases() throws Exception {
        List<Auction> auctions = getAllAuctions();
        if (auctions != null && !auctions.isEmpty()) {
            for (Auction auction : auctions) {

                if (auction.isComplete() && auction.getHighestBid() != null) {

                    Message message = context.createMessage();

                    message.setStringProperty("productName", auction.getProduct().getName());
                    message.setStringProperty("username", auction.getHighestBid().getPerson().getUsername());

                    context.createProducer().setProperty("topicUser", "dweet").send(topic, message);
                }

            }
        }
    }

    public List<Auction> getAllAuctions() {

        return auctionDAO.findAllAuctions();

    }

    @PermitAll
    public List<Auction> getActiveAuctions() {

        return auctionDAO.findActiveAuctions();

    }

    public Auction getAuction(int id) {

        Auction auction = auctionDAO.find(id);

        if (auction == null) {
            throw new AuctionApplicationException("Auction was not found.", Response.Status.NOT_FOUND);
        }

        return auction;

    }

    public Bid getAuctionBid(int auctionId, int bidId) {

        Bid bid = auctionDAO.findBid(auctionId, bidId);

        if (bid == null) {
            throw new AuctionApplicationException("Bid was not found.", Response.Status.NOT_FOUND);
        }

        return bid;

    }

    public Auction getPublishedAuction(int id) {

        Auction auction = auctionDAO.find(id);

        if (auction != null && auction.isPublished()) {
            return auction;
        }

        throw new AuctionApplicationException("Auction was not found.", Response.Status.NOT_FOUND);

    }

    public Bid placeBid(int auctionId, String username, double amount) {

        Auction auction = auctionDAO.find(auctionId);

        if (auction == null || !auction.isPublished() || auction.isComplete()) {
            throw new AuctionApplicationException("A published Auction with 'auctionId' was not found.", Response.Status.NOT_FOUND);
        }

        Bid highestBid = auction.getHighestBid();

        if (highestBid != null && amount <= highestBid.getAmount()) {
            throw new AuctionApplicationException("Amount must be larger than highest bid.", Response.Status.BAD_REQUEST);
        } else if (amount < auction.getStartingPrice()) {
            throw new AuctionApplicationException("Amount must be larger than starting price.", Response.Status.BAD_REQUEST);
        } else if (amount >= auction.getBuyoutPrice()) {
            throw new AuctionApplicationException("Amount must be less than buyout price.", Response.Status.BAD_REQUEST);
        }

        Person person = userDAO.find(username);

        if (person == null) {
            throw new AuctionApplicationException("Person with specified 'userId' was not found.", Response.Status.NOT_FOUND);
        }

        Bid bid = new Bid(auction, person, amount);

        return bid;

    }

    public Bid buyout(int auctionId, String username) {

        Auction auction = auctionDAO.find(auctionId);

        if (auction == null || !auction.isPublished() || auction.isComplete()) {
            throw new AuctionApplicationException("A published Auction with 'auctionId' was not found.", Response.Status.NOT_FOUND);
        }

        Person person = userDAO.find(username);

        if (person == null) {
            throw new AuctionApplicationException("Person with specified 'userId' was not found.", Response.Status.NOT_FOUND);
        }

        Bid bid = new Bid(auction, person, auction.getBuyoutPrice());
        return bid;

    }

    public Auction publishAuction(int id) {

        Auction auction = this.getAuction(id);

        auction.publish();
        return auction;

    }

    public Auction createAuction(int productId, double startingPrice, double buyoutPrice, long startTime, long length) {

        Product product = productDAO.find(productId);

        if (product == null) {
            throw new AuctionApplicationException("Product with specified 'productId' was not found.", Response.Status.NOT_FOUND);
        }

        if (startingPrice < 0) {
            throw new AuctionApplicationException("Starting price must be larger than zero.", Response.Status.BAD_REQUEST);
        }

        if (buyoutPrice < startingPrice) {
            throw new AuctionApplicationException("Buyout price must be larger than starting price", Response.Status.BAD_REQUEST);
        }

        if (length > 0) {
            throw new AuctionApplicationException("Length can't be smaller than or equal to zero you silly goose.", Response.Status.BAD_REQUEST);
        }

        return new Auction(product, startingPrice, buyoutPrice, startTime, length);

    }

}
