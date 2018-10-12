package ejb;

import entities.Bid;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Database Access Object
 * Handles all database transactions for User and Product
 * <p>
 * For a users address or rating, ask that user
 */

@Stateless
public class BidDAO {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    /**
     * Persists a user
     */
    public boolean persist(Bid b) {
        try {
            em.persist(b);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
