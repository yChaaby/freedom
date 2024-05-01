package models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private String username;
    private Date bday;

    public User(String username, Date bday) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.bday = bday;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBday() {
        return bday;
    }

    public void setBday(Date bday) {
        this.bday = bday;
    }
}
