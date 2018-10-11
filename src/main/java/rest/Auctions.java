package rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entities.Auction;
import entities.Bid;
import entities.User;

//To test rest operations use the url http://localhost:8080/AuctionApplication/rest/tweets

@Path("/auctions")
@Stateless
public class Auctions {

	@PersistenceContext(unitName = "AuctionApplicationPU")
	private EntityManager em;

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Auction> getAuctions() {
		Query query = em.createQuery("SELECT a from auction a");
		return query.getResultList();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{id}")
	public Auction auction(@PathParam("id") String id) {

		int idInt = Integer.parseInt(id);
		return em.find(Auction.class, idInt);

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{id}/bids")
	public List<Bid> auctionBids(@PathParam("id") String id) {

		int idInt = Integer.parseInt(id);
		Auction auction = em.find(Auction.class, idInt);

		if (auction != null) {
			return auction.getBids();
		} else {
			return null;
		}

	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{aid}/bids/{bid}")
	public Bid auctionBid(@PathParam("aid") String aid, @PathParam("bid") String bid) {

		int auctionId = Integer.parseInt(aid);
		int bidId = Integer.parseInt(bid);

		// when creating the query, treat the objects as java objects (i.e. bid.auction.id)
		Query query = em.createQuery("SELECT b FROM bid b WHERE b.id = ?1 AND b.auction.id = ?2", Bid.class)
				.setParameter(1, bidId)
				.setParameter(2, auctionId);

		try {
			return (Bid) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}


	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes("application/x-www-form-urlencoded")
	@Path("{id}/bids")
	public Bid createUser(
			@PathParam("id") String id,
			@FormParam("userId") String userIdString,
			@FormParam("amount") String amountString
			) {

		int auctionId = Integer.parseInt(id);
		Auction auction = em.find(Auction.class, auctionId);

		int userId = Integer.parseInt(userIdString);
		User user = em.find(User.class, userId);

		double amount = Double.parseDouble(amountString);

		if (auction == null || user == null) {
			return null;
		}

		Bid bid = new Bid(auction, user, amount);

		auction.getBids().add(bid);
		em.persist(auction);

		user.getBids().add(bid);
		em.persist(user);

		em.persist(bid);

		return bid;

	}
}
