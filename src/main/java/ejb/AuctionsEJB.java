package ejb;

import entities.Auction;
import entities.Bid;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Class implementing the Browse Auction Use Case
 */

@Stateless //Bean does not need to be connected to a user, I think
public class AuctionsEJB {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    //Get All Auctions
    public List<Auction> getAuctions() { // No security
        Query query = em.createQuery("SELECT a from auction a");
        return query.getResultList();
    }

    //Get a specific auction
    public Auction auction(String id) {
        int idInt = Integer.parseInt(id);
        return em.find(Auction.class, idInt);
    }

    //Get a specific users auctions
    public Auction auctions(User user){
        //TODO Implement method boddy
        return null;
    }

    public List<Bid> auctionBids(String id) {
        int idInt = Integer.parseInt(id);
        Auction auction = em.find(Auction.class, idInt);

        if (auction != null) {
            return auction.getBids();
        } else {
            return null;
        }

    }

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

    public Bid placeBid(String id, String userIdString, String amountString) {
        int auctionId = Integer.parseInt(id);
        Auction auction = em.find(Auction.class, auctionId);
        int userId = Integer.parseInt(userIdString);
        User user = em.find(User.class, userId);
        double amount = Double.parseDouble(amountString);

        if (auction == null || user == null) {
            return null;
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
