package controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "accountController")
@RequestScoped
public class AccountController implements Serializable {
}
