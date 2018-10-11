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
 * For address, ask a user
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
    public User findUser(int id){
        return em.find(User.class, id);
    }
}
