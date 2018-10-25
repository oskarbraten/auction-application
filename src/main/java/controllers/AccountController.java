package controllers;

import entities.Bid;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "accountController")
@RequestScoped
public class AccountController {
    public boolean isPurchase(Bid bid) {
        return bid.isPurchase();
    }
}
