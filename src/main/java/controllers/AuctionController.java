package controllers;

import ejb.AuctionManager;
import ejb.exceptions.AuctionApplicationException;
import entities.Auction;
import entities.Bid;
import misc.ApplicationConstants;
import misc.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

@Named(value = "auctionController")
@SessionScoped
public class AuctionController implements Serializable {

    @EJB
    AuctionManager auctionManager;

    private BigDecimal amount;
    private Auction auction;

    public void setActiveAuction(int id) {
        try {
            this.auction = auctionManager.getActiveAuction(id);
        } catch (Exception e) {
            try {
                Utils.getResponse().sendRedirect(ApplicationConstants.INDEX + ".xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void placeBid(String username) {

        double amount = this.amount.doubleValue();
        this.amount = null;

        try {

            Bid bid = auctionManager.placeBid(auction.getId(), username, amount);

            this.auction = auctionManager.getActiveAuction(auction.getId());

            Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + auction.getId());

        } catch (AuctionApplicationException exception) {

            try {
                Utils.getResponse().sendRedirect(ApplicationConstants.AUCTION + ".xhtml?id=" + auction.getId() + "&error=" + exception.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}
