/**
 * La classe OpinionTopic représente une opinion sur un sujet spécifique exprimée par un utilisateur.
 * Elle est sérialisable et peut être utilisée dans un contexte distant via Java RMI.
 *
 * Champs:
 * - User user: L'utilisateur qui exprime l'opinion.
 * - Topic topic: Le sujet sur lequel l'opinion est exprimée.
 * - double ox: La valeur de l'opinion exprimée.
 *
 * Constructeur:
 * - OpinionTopic(User user, Topic topic, double ox): Initialise une instance de OpinionTopic avec un utilisateur, un sujet et une valeur d'opinion.
 *
 * Méthodes:
 * - String toString(): Retourne une représentation sous forme de chaîne de caractères de l'objet OpinionTopic.
 * - User getUser(): Retourne l'utilisateur qui exprime l'opinion.
 * - void setUser(User user): Définit l'utilisateur qui exprime l'opinion.
 * - Topic getTopic(): Retourne le sujet de l'opinion.
 * - void setTopic(Topic topic): Définit le sujet de l'opinion.
 * - double getOx(): Retourne la valeur de l'opinion.
 * - void setOx(double ox): Définit la valeur de l'opinion.
 */

package models;

import java.io.Serializable;
import java.rmi.Remote;

public class OpinionTopic implements Serializable, Remote {
    private User user;
    private Topic topic;
    private double ox;

    public OpinionTopic(User user, Topic topic, double ox) {
        this.user = user;
        this.topic = topic;
        this.ox = ox;
    }

    @Override
    public String toString() {
        return "OpinionTopic{" +
                ", topic=" + topic.getIdTopic() +
                ", ox=" + ox +
                '}';
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
