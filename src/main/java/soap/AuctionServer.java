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

    //Get All unfinished Auctions
    @WebMethod ArrayList<Auction> getAuctions();

    //Get a specific auction
    @WebMethod Auction auction(String id);

    // Create a new auction
    @WebMethod boolean newAuction(Product product, double startingPrice, double buyoutPrice, long startTime, long length);

    //Publish an auction
    @WebMethod boolean publishAuction(String id);

    //Get a specific users auctions
    @WebMethod ArrayList<Auction> auctions(String usersId);

    //Get all bids from an auction
    @WebMethod ArrayList<Bid> auctionBids(String id);

    //get a specific known bid from an auction
    @WebMethod Bid auctionBid(String aid, String bid);

    //Place a bid
    @WebMethod Bid placeBid(String id, String userIdString, String amountString);
}