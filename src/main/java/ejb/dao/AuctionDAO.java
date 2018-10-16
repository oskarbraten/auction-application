package ejb.dao;

import entities.Auction;
import entities.Bid;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Stateless
public class AuctionDAO {

    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    public Auction find(int id) {

        return em.find(Auction.class, id);

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

        long now = new Date().getTime();

        // TODO: extend to check if auction is completed (isFinished || isBoughtOut)
        Query query = em.createQuery("SELECT a from auction a WHERE a.startTime != null AND a.startTime < ?1 AND (a.startTime + a.length) > ?1 ", Auction.class).setParameter(1, now);
        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }
}
