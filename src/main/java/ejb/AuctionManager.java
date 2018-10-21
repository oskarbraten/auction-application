package ejb;

import ejb.dao.AuctionDAO;
import ejb.dao.ProductDAO;
import ejb.dao.UserDAO;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;
import entities.Product;
import entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Named(value = "auctionManager")
public class AuctionManager {

    @EJB
    AuctionDAO auctionDAO;

    @EJB
    ProductDAO productDAO;

    @EJB
    UserDAO userDAO;

    public List<Auction> getAllAuctions() {

        return auctionDAO.findAllAuctions();

    }

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

    public Auction getActiveAuction(int id) {

        Auction auction = auctionDAO.find(id);

        if (auction != null && auction.isPublished() && !auction.isComplete()) {
            return auction;
        }

        throw new AuctionApplicationException("Auction was not found.", Response.Status.NOT_FOUND);

    }

    public Bid getActiveAuctionBid(int auctionId, int bidId) {

        this.getActiveAuction(auctionId); // will throw if the auction does not exist or is not active.

        Bid bid = auctionDAO.findBid(auctionId, bidId);

        if (bid == null) {
            throw new AuctionApplicationException("Bid was not found.", Response.Status.NOT_FOUND);
        }

        return bid;

    }

    public Bid placeBid(int auctionId, String username, double amount) {

        Auction auction = auctionDAO.find(auctionId);

        if (auction == null || !auction.isPublished() || auction.isComplete()) {
            throw new AuctionApplicationException("A published Auction with 'auctionId' was not found.", Response.Status.NOT_FOUND);
        }

        Bid highestBid = auction.findHighestBid();

        if (highestBid != null && amount <= highestBid.getAmount()) {
            throw new AuctionApplicationException("Amount must be larger than highest bid.", Response.Status.BAD_REQUEST);
        } else if (amount < auction.getStartingPrice()) {
            throw new AuctionApplicationException("Amount must be larger than starting price.", Response.Status.BAD_REQUEST);
        }

        User user = userDAO.find(username);

        if (user == null) {
            throw new AuctionApplicationException("User with specified 'userId' was not found.", Response.Status.NOT_FOUND);
        }

        Bid bid = new Bid(auction, user, amount);

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
