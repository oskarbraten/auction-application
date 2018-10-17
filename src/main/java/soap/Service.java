package soap;

import ejb.AuctionManager;
import entities.Auction;
import entities.Bid;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;


@WebService(serviceName = "Service")
public class Service {

    @EJB
    AuctionManager auctionManager;

    @WebMethod
    public List<Auction> getAllAuctions() {

        return auctionManager.getAllAuctions();

    }

    @WebMethod
    public List<Auction> getActiveAuctions() {

        List<Auction> activeAuctions = auctionManager.getActiveAuctions();

        if (activeAuctions != null) {
            return activeAuctions;
        } else {
            return new ArrayList<>();
        }

    }

    @WebMethod
    public Auction getAuction(int id) {

        Auction auction = auctionManager.getAuction(id);

        if (auction != null) {
            return auction;
        } else {
            return new Auction(); // empty response.
        }

    }

    @WebMethod
    public List<Bid> getAuctionBids(int auctionId) {

        Auction auction = auctionManager.getAuction(auctionId);

        if (auction != null) {
            return auction.getBids();
        } else {
            return new ArrayList<>(); // empty response.
        }

    }

    @WebMethod
    public Bid placeBid(int auctionId, String username, double amount) {

        return auctionManager.placeBid(auctionId, username, amount);

    }

    @WebMethod
    public Auction createAuction(int productId, double startingPrice, double buyoutPrice, long startTime, long length) {

        return auctionManager.createAuction(productId, startingPrice, buyoutPrice, startTime, length);

    }

    @WebMethod
    public Auction publishAuction(int id) {

        return auctionManager.publishAuction(id);

    }

}
