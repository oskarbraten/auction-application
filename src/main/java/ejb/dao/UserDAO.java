package ejb.dao;

import ejb.exceptions.AuctionApplicationException;
import entities.Person;

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

    public List<Person> findAllUsers() {

        Query query = em.createQuery("SELECT u FROM user u");

        try {
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    public Person find(String username) {

        try {
            return em.find(Person.class, username);
        } catch (IllegalArgumentException e) {
            throw new AuctionApplicationException("Invalid PK for Person supplied.", Response.Status.BAD_REQUEST);
        }

    }

    public boolean persist(Person u) {

        try {
            em.persist(u);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
