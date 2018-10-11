package soap;

import ejb.AuctionDAO;
import ejb.ProductDAO;
import ejb.UserDAO;
import entities.Auction;
import entities.Bid;
import entities.Product;
import entities.User;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebService(serviceName = "Auctions")
public class Auctions {

    @EJB
    AuctionDAO auctionDAO;

    @EJB
    ProductDAO productDAO;

    @EJB
    UserDAO userDAO;

    @WebMethod
    public List<Auction> getAllAuctions() {
        return auctionDAO.getAllAuctions();
    }


    @WebMethod
    public List<Auction> getActiveAuctions() {
        List<Auction> activeAuctions = auctionDAO.getActiveAuctions();

        if (activeAuctions != null) {
            return activeAuctions;
        } else {
            return new ArrayList<>();
        }

    }

    @WebMethod
    public Auction findAuction(int id) {
        Auction auction = auctionDAO.find(id);

        if (auction != null) {
            return auction;
        } else {
            return new Auction(); // empty response.
        }
    }

    @WebMethod
    public List<Bid> getAuctionBids(int auctionId) {
        return auctionDAO.getAllBidsFromAuction(auctionId);
    }

    /**
     * Places a bid on an auction.
     *
     * @param auctionId
     * @param userId
     * @param amount
     * @return boolean
     */
    @WebMethod
    public boolean placeBid(int auctionId, int userId, double amount) {

        Auction auction = auctionDAO.find(auctionId);

        User user = userDAO.find(userId);


        // If no auction or no user or the bid is a negative amount:
        if (auction == null || user == null || amount <= 0) {
            return false;
        }

        if (!auction.isPublished() || auction.isFinished()) {
            return false;
        }

        Bid highestBid = auction.findHighestBid();

        if (highestBid != null && amount <= highestBid.getAmount()) {
            return false;
        }

        Bid bid = new Bid(auction, user, amount);

        auction.getBids().add(bid);
        user.getBids().add(bid);

        return (auctionDAO.persist(auction) && userDAO.persist(user));
    }

    // Create a new auction
    @WebMethod
    public boolean createAuction(int productId, double startingPrice, double buyoutPrice, long startTime, long length) {

        Product product = productDAO.find(productId);

        if (product == null) {
            return false;
        }

        Auction auction = new Auction(product, startingPrice, buyoutPrice, startTime, length);

        return auctionDAO.persist(auction);
    }

    //Publish an auction
    private boolean publishAuction(String id) {
        int idInt = Integer.parseInt(id);
        Auction a = auctionDAO.find(idInt);
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();
        a.setStartTime(now);

        return auctionDAO.updateAuction(a); // cascade return
    }

    //Get all bids from an auction
    private List<Bid> auctionBids(String id) {
        int idInt = Integer.parseInt(id);

        return auctionDAO.getAllBidsFromAuction(idInt);
    }

    //get a specific known bid from an auction
    private Bid auctionBid(String aid, String bid) {
        int auctionId = Integer.parseInt(aid);
        int bidId = Integer.parseInt(bid);

        return auctionDAO.bidFromAuction(auctionId, bidId);
    }

}
