package ejb;

import ejb.dao.UserDAO;
import ejb.exceptions.AuctionApplicationException;
import entities.Person;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.core.Response;

@Stateless
@Named(value = "userManager")
@RolesAllowed("users")
public class UserManager {
    @EJB
    UserDAO userDAO;

    public Person getUser(String username) {
        Person person = userDAO.find(username);

        if (person == null) {
            throw new AuctionApplicationException("Person was not found.", Response.Status.NOT_FOUND);
        }

        return person;
    }
}
