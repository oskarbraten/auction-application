package ejb;

import entities.Auction;
import entities.Bid;
import entities.User;
import entities.Product;
//import soap.AuctionServer;

import javax.ejb.Stateless;
//import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Class implementing the Browse Auction Use Case
 */

@Stateless //Bean does not need to be connected to a user, I think
//@WebService(endpointInterface = "soap.AuctionServer")
public class AuctionsEJB /*implements AuctionServer*/ {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;


    //Get All unfinished Auctions
    public List<Auction> getAuctions() { // No security
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();

        Query query = em.createQuery("SELECT a from auction a WHERE " +
                "a.startTime != null AND a.startTime < ?1 AND (a.startTime + a.length) > ?1 ", Auction.class)
                .setParameter(1, now);
        return query.getResultList();
    }

    //Get a specific auction
    public Auction auction(String id) {
        int idInt = Integer.parseInt(id);
        return em.find(Auction.class, idInt);
    }

    // Create a new auction
    public boolean newAuction(Product product, double startingPrice, double buyoutPrice, long startTime, long length) {
        Auction a = new Auction();
        a.setProduct(product);
        a.setStartingPrice(startingPrice);
        a.setBuyoutPrice(buyoutPrice);
        a.setStartTime(startTime); // should work if null
        a.setLength(length);

        try{
            em.persist(a);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    //Publish an auction
    public boolean publishAuction(String id){
        int idInt = Integer.parseInt(id);
        Auction a = em.find(Auction.class, idInt);
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();
        a.setStartTime(now);
       try {
           em.getTransaction().begin();
           em.merge(a);
           em.getTransaction().commit();
           return true;
       } catch (Exception e) {
           return false;
       }
    }

    //Get a specific users auctions
    public List<Auction> auctions(String usersId){
        int userId = Integer.parseInt(usersId);

        // Select all actions from all products from user
        Query query = em.createQuery("SELECT a from auction a WHERE a.product.user.id = ?1", Auction.class)
                .setParameter(1, userId);
        try {
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    //TODO move this to?
    //Get all bids from an auction
    public List<Bid> auctionBids(String id) {
        int idInt = Integer.parseInt(id);
        Auction auction = em.find(Auction.class, idInt);

        if (auction != null) {
            return auction.getBids();
        } else {
            return null;
        }

    }

    //TODO move this to?
    //get a specific known bid from an auction
    public Bid auctionBid(String aid, String bid) {
        int auctionId = Integer.parseInt(aid);
        int bidId = Integer.parseInt(bid);

        // when creating the query, treat the objects as java objects (i.e. bid.auction.id)
        Query query = em.createQuery("SELECT b FROM bid b WHERE b.id = ?1 AND b.auction.id = ?2", Bid.class)
                .setParameter(1, bidId)
                .setParameter(2, auctionId);

        try {
            return (Bid) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
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
