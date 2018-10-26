package entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Party {

    @Id
    private String username;
    private String partyName;

    public Party() {}

    public Party(String username, String partyName) {
        this.username = username;
        this.partyName = partyName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
