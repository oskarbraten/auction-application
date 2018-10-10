package ejb;

import entities.Auction;
import entities.Bid;
import entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Database Access Object
 * Handles all database transactions
 */

@Stateless
public class AuctionDAO {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    /** Returns All unfinished Auctions */
    public List<Auction> runningAuctions() {
        Date date = new Date(); // Thread safety problem
        long now = date.getTime();

        Query query = em.createQuery("SELECT a from auction a WHERE a.startTime != null AND a.startTime < ?1 AND (a.startTime + a.length) > ?1 ", Auction.class)
                .setParameter(1, now);
        try {
            return query.getResultList();
        } catch (Exception e){
            return null;
        }
    }

    /** Returns a specific auction */
    public Auction auction(int id) {
        return em.find(Auction.class, id);
    }

    /** Add an auction to the database */
    public boolean persistAuction(Auction a) {
        try {
            em.persist(a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Update the information stored in the database for an Auction object */
    public boolean updateAuction(Auction a){
        try {
            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns a list of all the auction a user have created */
    public List<Auction> usersAuctions(int userId){
        // Select all actions from all products from user
        Query query = em.createQuery("SELECT a from auction a WHERE a.product.user.id = ?1", Auction.class)
                .setParameter(1, userId);
        try {
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    /** Returns all bids connected to an auction */
    public List<Bid> allBidsFromAuction(int id) {
        Auction auction = em.find(Auction.class, id);

        if (auction != null) { // If auction exist
            return auction.getBids();
        } else {
            return null;
        }
    }

    /** Return a known bid from a known auction */
    public Bid bidFromAuction(int auctionId, int bidId) {
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
}
