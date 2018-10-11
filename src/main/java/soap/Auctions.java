package soap;

import ejb.AuctionDAO;
import ejb.ProductDAO;
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
    AuctionDAO auctionDao;

    @EJB
    ProductDAO productDAO;

    @WebMethod
    public List<Auction> getAllAuctions() {
        return auctionDao.getAllAuctions();
    }


    @WebMethod
    public List<Auction> getActiveAuctions() {
        List<Auction> activeAuctions = auctionDao.getActiveAuctions();

        if (activeAuctions != null) {
            return activeAuctions;
        } else {
            return new ArrayList<>();
        }

    }

    @WebMethod
    public Auction findAuction(int id) {
        Auction auction = auctionDao.find(id);

        if (auction != null) {
            return auction;
        } else {
            return new Auction(); // empty response.
        }
    }

    @WebMethod
    public List<Bid> getAuctionBids(int auctionId) {
        return auctionDao.getAllBidsFromAuction(auctionId);
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

        Auction auction = auctionDao.find(auctionId);

        //User user = userDao.find(userId);
        User user = new User();


        // If no auction or no user or the bid is a negative amount:
        if (auction == null || user == null || amount <= 0) {
            return false;
        }

        Bid highestBid = auction.findHighestBid();

        if (amount <= highestBid.getAmount()) {
            return false;
        }

        Bid bid = new Bid(auction, user, amount);
        auction.getBids().add(bid);

        return auctionDao.persist(auction);
    }

    // Create a new auction
    @WebMethod
    public boolean createAuction(int productId, double startingPrice, double buyoutPrice, long startTime, long length) {

        Product product = productDAO.find(productId);

        if (product == null) {
            return false;
        }

        Auction auction = new Auction(product, startingPrice, buyoutPrice, startTime, length);

        return auctionDao.persist(auction);
    }

    //Publish an auction
    private boolean publishAuction(String id) {
        int idInt = Integer.parseInt(id);
        Auction a = auctionDao.find(idInt);
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();
        a.setStartTime(now);

        return auctionDao.updateAuction(a); // cascade return
    }

    //Get all bids from an auction
    private List<Bid> auctionBids(String id) {
        int idInt = Integer.parseInt(id);

        return auctionDao.getAllBidsFromAuction(idInt);
    }

    //get a specific known bid from an auction
    private Bid auctionBid(String aid, String bid) {
        int auctionId = Integer.parseInt(aid);
        int bidId = Integer.parseInt(bid);

        return auctionDao.bidFromAuction(auctionId, bidId);
    }

}
