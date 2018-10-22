package controllers;

import ejb.AuctionManager;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;
import misc.ApplicationConstants;
import misc.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;

@Named(value = "auctionController")
@RequestScoped
public class AuctionController implements Serializable {

    @EJB
    AuctionManager auctionManager;

    public Auction getPublishedAuction(int id) {
        try {
            return auctionManager.getPublishedAuction(id);
        } catch (Exception e) {
            try {
                Utils.getResponse().sendRedirect(ApplicationConstants.INDEX + ".xhtml");
                return null;

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }
    }

    public void placeBid(Auction auction, String username, String amountString) {

        int id = auction.getId();

        try {

            double amount;

            try {
                amount = Double.parseDouble(amountString);
            } catch (Exception e) {
                throw new AuctionApplicationException("Amount must be a valid double.", Response.Status.BAD_REQUEST);
            }

            Bid bid = auctionManager.placeBid(id, username, amount);
            Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + id);

        } catch (AuctionApplicationException exception) {

            try {
                Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + id + "&error=" + exception.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void buyout(Auction auction, String username) {

        int id = auction.getId();

        try {

            auctionManager.buyout(id, username);

            Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + id);

        } catch (AuctionApplicationException exception) {

            try {
                Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + id + "&error=" + exception.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
