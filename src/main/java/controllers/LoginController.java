package controllers;

import ejb.UserManager;
import entities.Party;
import entities.Person;
import misc.ApplicationConstants;
import misc.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;


@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    UserManager userManager;

    private static final long serialVersionUID = 1L;

    public String login(String username, String password) {

        FacesContext context = Utils.getContext();
        HttpServletRequest request = Utils.getRequest();

        try {
            request.login(username, password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", null));
            return ApplicationConstants.LOGIN_REDIRECT;
        }

        Principal principal = request.getUserPrincipal();

        Person person = userManager.getUser(principal.getName());

        HttpSession session = Utils.getSession();
        session.setAttribute(ApplicationConstants.USER, person);


        if (request.isUserInRole(ApplicationConstants.ADMIN_PARTY)) { // For future use:
            return ApplicationConstants.INDEX_REDIRECT;
        }

        return ApplicationConstants.INDEX_REDIRECT;

    }

    public String logout() {

        HttpSession session = Utils.getSession();
        session.invalidate();

        try {
            Utils.getRequest().logout();
        } catch (ServletException e) {
            Utils.getContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", null));
            return ApplicationConstants.LOGIN_REDIRECT;
        }

        // Successfully logged out, redirect to login page.
        return ApplicationConstants.LOGIN_REDIRECT;
    }

    public Person getUser() throws IOException {

        Person user = (Person) Utils.getSession().getAttribute(ApplicationConstants.USER);

        if (Utils.getRequest().isUserInRole(ApplicationConstants.USER_PARTY) && user != null) {
            return user;
        } else {
            Utils.getResponse().sendRedirect(ApplicationConstants.LOGIN + ".xhtml");
            return null;
        }
    }

//    public Person getUser() {
//        HttpSession session = Utils.getSession();
//        return (Person) session.getAttribute(ApplicationConstants.USER);
//    }

}