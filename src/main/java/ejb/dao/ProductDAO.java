package ejb.dao;

import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

@Stateless
public class ProductDAO {

    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    public Product find(int id) {

        try {
            return em.find(Product.class, id);
        } catch (IllegalArgumentException e) {
            throw new AuctionApplicationException("Invalid PK for Product supplied.", Response.Status.BAD_REQUEST);
        }


    }

}
