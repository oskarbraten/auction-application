package ejb;

import entities.User;
import misc.ApplicationConstants;
import misc.Utils;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;


@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    UserManager userManager;

    private static final long serialVersionUID = 1L;

    public String login(String username, String password) throws IOException {

        User user = userManager.getUser(username);

        if (user.getPassword().equals(password)) {
            HttpSession session = Utils.getSession();
            session.setAttribute(ApplicationConstants.USERNAME, username);

            return ApplicationConstants.INDEX + "?faces-redirect=true";
        } else {
            return ApplicationConstants.LOGIN;
        }

    }

    public String logout() {
        HttpSession session = Utils.getSession();
        session.invalidate();
        return ApplicationConstants.LOGIN;
    }

    public void redirect() throws IOException {

        HttpSession session = Utils.getSession();

        if (session.getAttribute(ApplicationConstants.USERNAME) == null) {
            Utils.getResponse().sendRedirect(ApplicationConstants.LOGIN + ".xhtml");
        }

    }

    public String getUsername() {
        HttpSession session = Utils.getSession();
        return (String) session.getAttribute(ApplicationConstants.USERNAME);
    }

}