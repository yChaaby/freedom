/**
 * La classe User représente un utilisateur dans le système, avec des attributs pour le nom d'utilisateur, la date de naissance,
 * le type d'utilisateur, les opinions, les degrés d'influence et les abonnés. Elle est sérialisable et peut être utilisée
 * dans des contextes distants via Java RMI.
 *
 * Champs:
 * - String username: Le nom d'utilisateur.
 * - Date bday: La date de naissance de l'utilisateur.
 * - UserType userType: Le type de l'utilisateur (influenceur, penseur critique, etc.).
 * - HashMap<String, OpinionTopic> opinions: Les opinions de l'utilisateur sur différents sujets.
 * - HashMap<String, Double> influenceDegree: Les degrés d'influence de l'utilisateur sur d'autres utilisateurs.
 * - List<String> followrs: La liste des abonnés de l'utilisateur.
 *
 * Constructeur:
 * - User(String username, Date bday, UserType userType): Initialise une instance de User avec un nom d'utilisateur, une date de naissance et un type d'utilisateur.
 *
 * Méthodes:
 * - List<String> getFollowrs(): Retourne la liste des abonnés.
 * - UserType getUserType(): Retourne le type de l'utilisateur.
 * - void addFollower(String follower): Ajoute un abonné à l'utilisateur.
 * - void setUserType(UserType userType): Définit le type de l'utilisateur.
 * - boolean isFirstInteraction(User user): Vérifie si c'est la première interaction avec un autre utilisateur.
 * - boolean hasOpinionAbout(Topic t): Vérifie si l'utilisateur a une opinion sur un sujet donné.
 * - void addInfluenceDegree(String username, Double degree): Ajoute un degré d'influence pour un utilisateur donné.
 * - HashMap<String, OpinionTopic> getOpinions(): Retourne les opinions de l'utilisateur.
 * - double getInfluenceDegree(User user): Retourne le degré d'influence sur un autre utilisateur.
 * - OpinionTopic getOpinion(Topic t): Retourne l'opinion de l'utilisateur sur un sujet donné.
 * - void displayOpinions(): Affiche les opinions de l'utilisateur.
 * - boolean equals(Object o): Vérifie l'égalité avec un autre objet.
 * - int hashCode(): Retourne le hash code de l'utilisateur.
 * - void setOpinions(HashMap<String, OpinionTopic> opinions): Définit les opinions de l'utilisateur.
 * - void addOpinion(OpinionTopic op): Ajoute une opinion à l'utilisateur.
 * - String getUsername(): Retourne le nom d'utilisateur.
 * - void setUsername(String username): Définit le nom d'utilisateur.
 * - Date getBday(): Retourne la date de naissance.
 * - void setBday(Date bday): Définit la date de naissance.
 */
package models;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class User  implements Serializable, Remote {
    private String username;
    private Date bday;
    private UserType userType;
    private HashMap<String ,OpinionTopic> opinions;
    private HashMap<String ,Double> influenceDegree;
    private List<String> followrs;

    public List<String> getFollowrs() {
        return followrs;
    }

    public User(String username, Date bday, UserType userType) throws RemoteException {
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

