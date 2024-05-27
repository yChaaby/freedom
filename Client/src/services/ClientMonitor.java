/**
 * L'interface ClientMonitor définit les méthodes que les objets distants doivent implémenter pour permettre la gestion des interactions
 * entre les utilisateurs, les opinions et les sujets dans un système RMI (Remote Method Invocation).
 *
 * Méthodes:
 * - void displayMessage(String msg): Affiche un message reçu.
 * - User getUser(): Retourne l'utilisateur associé au client monitor.
 * - void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor): Envoie une opinion à un autre client monitor.
 * - void propose(Topic t): Propose un nouveau sujet de discussion.
 * - void addFollower(String username): Ajoute un abonné à l'utilisateur.
 * - double requestProof(): Demande une preuve pour valider une opinion.
 * - double answer_the_call(): Demande à l'utilisateur s'il souhaite répondre à un appel.
 * - int accepts_communication(String username): Demande à l'utilisateur s'il souhaite accepter une communication avec un autre utilisateur.
 * - void addOpinion(OpinionTopic op): Ajoute une opinion à l'utilisateur.
 */
package services;

import models.OpinionTopic;
import models.Topic;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientMonitor extends Remote {
    public void displayMessage(String msg) throws RemoteException;
    public User getUser() throws RemoteException;
    public void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException;
    public void propose(Topic t)throws RemoteException;
    public void addFollower(String username) throws RemoteException;
    public double requestProof() throws RemoteException;
    public double answer_the_call() throws RemoteException;
    public int accepts_communication(String username) throws RemoteException;
    public void addOpinion(OpinionTopic op) throws RemoteException;
}
