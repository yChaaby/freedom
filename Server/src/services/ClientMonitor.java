package services;

import models.Topic;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientMonitor extends Remote {
    public void displayMessage(String msg) throws RemoteException;
    public User getUser() throws RemoteException;
    public void propose(Topic t);
}
