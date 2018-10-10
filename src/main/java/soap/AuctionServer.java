package soap;

import entities.Auction;
import entities.Bid;
import entities.Product;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.util.ArrayList;

@WebService
@SOAPBinding(style = Style.RPC)
public interface AuctionServer {

    @WebMethod
    //Get All unfinished Auctions
    ArrayList<Auction> getAuctions();

    @WebMethod
    //Get a specific auction
    Auction auction(String id);

    @WebMethod
    // Create a new auction
    boolean newAuction(Product product, double startingPrice, double buyoutPrice, long startTime, long length);

    @WebMethod
    //Publish an auction
    boolean publishAuction(String id);

    @WebMethod
    //Get a specific users auctions
    ArrayList<Auction> auctions(String usersId);

    @WebMethod
    //Get all bids from an auction
    ArrayList<Bid> auctionBids(String id);

    @WebMethod
    //get a specific known bid from an auction
    Bid auctionBid(String aid, String bid);

    @WebMethod
    //Place a bid
    Bid placeBid(String id, String userIdString, String amountString);
}