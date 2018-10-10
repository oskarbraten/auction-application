package ejb;

import entities.Auction;
import entities.Bid;
import entities.User;
import entities.Product;
import soap.AuctionServer;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class implementing the Browse Auction Use Case
 */

@WebService(endpointInterface = "soap.AuctionServer")
public class AuctionsEJB implements AuctionServer {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    @EJB
    AuctionDAO auctionDao;

    //Get All unfinished Auctions
    public ArrayList<Auction> getAuctions() { // No security implemented, should be implemented here
        ArrayList<Auction> auctionList = new ArrayList<>();
        List<Auction> temp = auctionDao.runningAuctions();
        int size = temp.size();

        for (int i = 0; i <size; i++){ //Try catch block or move to separate helper class?
            auctionList.add(temp.get(i));
        }
        return auctionList;
    }

    //Get a specific auction
    public Auction auction(String id) {
        int idInt = Integer.parseInt(id);
        return auctionDao.auction(idInt);
    }

    // Create a new auction
    public boolean newAuction(Product product, double startingPrice, double buyoutPrice, long startTime, long length) {
        Auction a = new Auction();
        a.setProduct(product);
        a.setStartingPrice(startingPrice);
        a.setBuyoutPrice(buyoutPrice);
        a.setStartTime(startTime); // should work if null
        a.setLength(length);

       return auctionDao.persistAuction(a); // cascade return
    }

    //Publish an auction
    public boolean publishAuction(String id){
        int idInt = Integer.parseInt(id);
        Auction a = em.find(Auction.class, idInt);
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();
        a.setStartTime(now);

        return auctionDao.updateAuction(a); // cascade return
    }

    //Get a specific users auctions
    public ArrayList<Auction> auctions(String usersId){
        int userId = Integer.parseInt(usersId);

        ArrayList<Auction> auctionsList = new ArrayList<>();
        List<Auction> temp = auctionDao.usersAuctions(userId);
        int size = temp.size();
        for (int i = 0; i <size; i++){ //Try catch block or move to separate helper class?
            auctionsList.add(temp.get(i));
        }
        return auctionsList;
    }

    //Get all bids from an auction
    public ArrayList<Bid> auctionBids(String id) {
        int idInt = Integer.parseInt(id);

        ArrayList<Bid> bidList = new ArrayList<>();
        List<Bid> temp = auctionDao.allBidsFromAuction(idInt);
        int size = temp.size();
        for (int i = 0; i <size; i++){ //Try catch block or move to separate helper class?
            bidList.add(temp.get(i));
        }
        return bidList;
    }

    //get a specific known bid from an auction
    public Bid auctionBid(String aid, String bid) {
        int auctionId = Integer.parseInt(aid);
        int bidId = Integer.parseInt(bid);

        return auctionDao.bidFromAuction(auctionId, bidId);
    }

    //TODO move to participateEJB
    //Place a bid
    public Bid placeBid(String id, String userIdString, String amountString) {
        int auctionId = Integer.parseInt(id);
        Auction auction = em.find(Auction.class, auctionId);
        int userId = Integer.parseInt(userIdString);
        User user = em.find(User.class, userId);
        double amount = Double.parseDouble(amountString);

        // If: no auction or no user or the bid is a negative amount
        if (auction == null || user == null || amount <= 0 ) {
            return null; //Illegal action
        }

        Bid bid = new Bid(auction, user, amount);
        auction.getBids().add(bid);

        em.persist(auction);
        user.getBids().add(bid);
        em.persist(user);
        em.persist(bid);

        return bid;
    }
}
