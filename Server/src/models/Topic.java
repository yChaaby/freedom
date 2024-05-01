package models;

import java.io.Serializable;

public class Topic implements Serializable {
    private String idTopic;

    public Topic(String s) {
        this.idTopic = s;
    }

    public String getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(String idTopic) {
        this.idTopic = idTopic;
    }
}
