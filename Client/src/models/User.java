package models;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.*;

public class User implements Serializable, Remote {

    private String username;
    private Date bday;
    private UserType userType;
    private HashMap<String ,OpinionTopic> opinions;
    private HashMap<String ,Double> influenceDegree;
    private List<String> followrs;

    public List<String> getFollowrs() {
        return followrs;
    }

    public User(String username, Date bday, UserType userType) {
        this.username = username;
        this.bday = bday;
        this.userType = userType;
        this.opinions = new HashMap<>();
        this.influenceDegree = new HashMap<>();
        this.followrs = new ArrayList<>();
    }
    public UserType getUserType() {
        return userType;
    }
    public void addFollower(String follower) {
        followrs.add(follower);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public boolean isFirstInteraction(User user){
        return !this.influenceDegree.containsKey(user.getUsername());
    }
    public boolean hasOpinionAbout(Topic t){
        return this.opinions.containsKey(t.getIdTopic());
    }
    public void addInfluenceDegree(String username, Double degree) {
        this.influenceDegree.put(username,degree);
    }

    public HashMap<String, OpinionTopic> getOpinions() {
        return opinions;
    }
    public double getInfluenceDegree(User user){
        return influenceDegree.get(user.getUsername());
    }

    public OpinionTopic getOpinion(Topic t){
        return opinions.get(t.getIdTopic());
    }

    public void displayOpinions() {
        HashMap<String,OpinionTopic> map = this.opinions;
        if (map.isEmpty()) {
            System.out.println("The map is empty, you can add some opinions !");
            return;
        }
        System.out.println("Opinions Contents :");
        for (Map.Entry<String, OpinionTopic> entry : map.entrySet()) {
            System.out.println("Topic: " + entry.getKey() + ", Opinion: " + entry.getValue().getOx());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    public void setOpinions(HashMap<String, OpinionTopic> opinions) {
        this.opinions = opinions;
    }
    public void addOpinion(OpinionTopic op) {
        opinions.put(op.getTopic().getIdTopic(), op);
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
