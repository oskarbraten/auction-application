package ejb.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException
public class AuctionApplicationException extends WebApplicationException {

    public AuctionApplicationException(String message, Response.Status statusCode) {
        super(message, statusCode);
    }

}
