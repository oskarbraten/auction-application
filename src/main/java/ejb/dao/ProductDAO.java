package ejb.dao;

import entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProductDAO {

    @PersistenceContext(unitName = "AuctionApplicationPU")
    private EntityManager em;

    public Product find(int id) {

        return em.find(Product.class, id);

    }

}
