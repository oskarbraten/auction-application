package ejb;

import ejb.dao.UserDAO;
import ejb.exceptions.AuctionApplicationException;
import entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.core.Response;

@Stateless
@Named(value = "userManager")
public class UserManager {
    @EJB
    UserDAO userDAO;

    public User getUser(String username) {
        User user = userDAO.find(username);

        if (user == null) {
            throw new AuctionApplicationException("User was not found.", Response.Status.NOT_FOUND);
        }

        return user;
    }
}
