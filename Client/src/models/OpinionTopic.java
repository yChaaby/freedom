package models;

import java.io.Serializable;

public class OpinionTopic implements Serializable {
    private User user;
    private Topic topic;
    private double ox;

    public OpinionTopic(User user, Topic topic, double ox) {
        this.user = user;
        this.topic = topic;
        this.ox = ox;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public double getOx() {
        return ox;
    }

    public void setOx(double ox) {
        this.ox = ox;
    }
}
