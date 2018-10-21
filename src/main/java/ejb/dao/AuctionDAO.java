package ejb.dao;

import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class AuctionDAO {

    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    public Auction find(int id) {

        try {
            return em.find(Auction.class, id);
        } catch (IllegalArgumentException e) {
            throw new AuctionApplicationException("Invalid PK for Auction supplied.", Response.Status.BAD_REQUEST);
        }

    }

    public Bid findBid(int auctionId, int bidId) {

        Query query = em.createQuery("SELECT b FROM bid b WHERE b.id = ?1 AND b.auction.id = ?2", Bid.class)
                .setParameter(1, bidId)
                .setParameter(2, auctionId);

        try {
            return (Bid) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<Auction> findAllAuctions() {

        Query query = em.createQuery("SELECT a from auction a");

        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public List<Auction> findActiveAuctions() {

        // TODO: write a proper SQL query.

        List<Auction> auctions = this.findAllAuctions();

        auctions.removeIf(item -> {
            if (item.isComplete() || !item.isPublished()) {
                return true;
            }
            return false;
        });

        return auctions;
    }
}
