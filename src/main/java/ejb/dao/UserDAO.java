package ejb.dao;

import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

        Query query = em.createQuery("SELECT u from user u");

        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public User find(int id) {

        return em.find(User.class, id);

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
