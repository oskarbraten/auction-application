package ejb;

import entities.Address;
import entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Database Access Object
 * Handles all database transactions for User and Product
 *
 * For a users address or rating, ask that user
 */

@Stateless
public class UserDAO {

    /* Entity Manager*/
    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    /** Returns all users */
    public List<User> findAllUsers(){
        Query query = em.createQuery("SELECT u from user u");
        try {
            return query.getResultList();
        } catch (Exception e){
            return null;
        }
    }

    /** Returns a user */
    public User find(int id){
        return em.find(User.class, id);
    }

    public boolean uppdateUser(User u){
        try {
            em.getTransaction().begin();
            em.merge(u);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Persists a user */
    public boolean persist(User u){
        try {
            em.persist(u);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
