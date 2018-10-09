/**package soap;

import entities.Auction;
import entities.Bid;
import entities.Product;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.util.List;

@WebService
@SOAPBinding(style = Style.RPC)
public interface AuctionServer {
    //Get All Auctions
    @WebMethod
    List<Auction> getAuctions();

    //Get a specific auction
    @WebMethod Auction auction(String id);
    // Create a new auction

    @WebMethod boolean newAuction(Product product, double startingPrice, double buyoutPrice, long startTime, long length);

    //Publish an auction
    @WebMethod boolean publishAuction();

    //Get a specific users auctions
    @WebMethod List<Auction> auctions(String usersId);

    //TODO move this to?
    //Get all bids from an auction
    @WebMethod List<Bid> auctionBids(String id);

    //TODO move this to?
    //get a specific known bid from an auction
    @WebMethod Bid auctionBid(String aid, String bid);

    //TODO move to participateEJB
    //Place a bid
    @WebMethod Bid placeBid(String id, String userIdString, String amountString);
}
*/