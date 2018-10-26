package controllers;

import ejb.AuctionManager;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Person;
import misc.ApplicationConstants;
import misc.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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

    public String placeBid(Auction auction, Person user, String amountString) {

        int id = auction.getId();

        try {

            double amount;

            try {
                amount = Double.parseDouble(amountString);
            } catch (Exception e) {
                throw new AuctionApplicationException("Amount must be a valid double.", Response.Status.BAD_REQUEST);
            }

            auctionManager.placeBid(id, user.getUsername(), amount);

            return ApplicationConstants.AUCTION_REDIRECT + "&id=" + id;

        } catch (AuctionApplicationException exception) {

            Utils.getContext().addMessage("placeBidForm:amount", new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
            return null;

        }

    }

    public String buyout(Auction auction, Person user) {

        int id = auction.getId();

        try {

            auctionManager.buyout(id, user.getUsername());

            return ApplicationConstants.AUCTION_REDIRECT + "&id=" + id;

        } catch (AuctionApplicationException exception) {

            Utils.getContext().addMessage("placeBidForm:amount", new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), null));
            return null;

        }

    }
}
