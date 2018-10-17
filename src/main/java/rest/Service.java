package rest;

import ejb.AuctionManager;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/auctions")
public class Service {

    @EJB
    AuctionManager auctionManager;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Auction> getActiveAuctions() {

        return auctionManager.getActiveAuctions();

    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Auction getAuction(@PathParam("id") int id) {

        return auctionManager.getActiveAuction(id);

    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{id}/bids")
    public List<Bid> auctionBids(@PathParam("id") int id) throws AuctionApplicationException {

        Auction auction = auctionManager.getActiveAuction(id);
        return auction.getBids();

    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{aid}/bids/{bid}")
    public Bid getAuctionBid(@PathParam("aid") int auctionId, @PathParam("bid") int bidId) {

        return auctionManager.getActiveAuctionBid(auctionId, bidId);

    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes("application/x-www-form-urlencoded")
    @Path("{id}/bids")
    public Bid placeBid(
            @PathParam("id") int auctionId,
            @FormParam("userId") String username,
            @FormParam("amount") double amount
    ) {

        return auctionManager.placeBid(auctionId, username, amount);

    }
}
