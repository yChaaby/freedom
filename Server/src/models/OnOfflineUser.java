package models;

public class OnOfflineUser {
    User user=null;
    boolean isonline=false;
    public OnOfflineUser(User user) {
        this.user = user;
        this.isonline=false;
    }

}
