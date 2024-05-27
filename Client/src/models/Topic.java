/**
 * La classe Topic représente un sujet sur lequel des opinions peuvent être exprimées.
 * Elle est sérialisable, permettant ainsi son utilisation dans des contextes de transmission d'objets.
 *
 * Champs:
 * - String idTopic: L'identifiant unique du sujet.
 *
 * Constructeur:
 * - Topic(String s): Initialise une instance de Topic avec un identifiant unique.
 *
 * Méthodes:
 * - String getIdTopic(): Retourne l'identifiant du sujet.
 * - void setIdTopic(String idTopic): Définit l'identifiant du sujet.
 */
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
