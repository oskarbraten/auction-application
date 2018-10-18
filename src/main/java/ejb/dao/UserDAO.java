package ejb.dao;

import ejb.exceptions.AuctionApplicationException;
import entities.Product;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Database Access Object
 * For a users address or rating, ask that user
 */

@Stateless
public class UserDAO {

    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    public List<User> findAllUsers() {

        Query query = em.createQuery("SELECT u FROM user u");

        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public User find(String username) {

        try {
            return em.find(User.class, username);
        } catch (IllegalArgumentException e) {
            throw new AuctionApplicationException("Invalid PK for User supplied.", Response.Status.BAD_REQUEST);
        }

    }

    public boolean persist(User u) {

        try {
            em.persist(u);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
