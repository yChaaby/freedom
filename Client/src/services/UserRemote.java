/**
 * L'interface UserRemote définit les méthodes que les objets distants doivent implémenter pour permettre la gestion des utilisateurs
 * et de leurs moniteurs dans un système RMI (Remote Method Invocation).
 *
 * Méthodes:
 * - ClientMonitor getClientMonitor(String username): Retourne le client monitor associé à un nom d'utilisateur donné.
 * - void addListener(ClientMonitor c): Ajoute un client monitor à la liste des écouteurs.
 * - Map<String, ClientMonitor> getUsers(): Retourne une carte des utilisateurs avec leurs moniteurs respectifs.
 * - List<ClientMonitor> getClientMonitors(): Retourne une liste de tous les moniteurs de clients.
 * - void isDisconnected(String name, UserType type): Indique qu'un utilisateur est déconnecté.
 * - void exit_system(String username, UserType type): Gère la sortie d'un utilisateur du système.
 */
package services;

import models.UserType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface UserRemote extends Remote {
    public ClientMonitor getClientMonitor(String username) throws RemoteException;
    public void addListener(ClientMonitor c) throws RemoteException;
    public Map<String, ClientMonitor> getUsers()throws RemoteException;
    public List<ClientMonitor> getClientMonitors() throws RemoteException;
    public void isDisconnected(String name, UserType type) throws RemoteException;
    public void exit_system(String username,UserType type) throws RemoteException;
}
