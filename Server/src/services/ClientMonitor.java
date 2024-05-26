package services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import models.OpinionTopic;
import models.Topic;
import models.User;

public interface ClientMonitor extends Remote {
    public void displayMessage(String msg) throws RemoteException;
    public User getUser() throws RemoteException;
    public void propose(Topic t)throws RemoteException;
    public void addFollower(String username) throws RemoteException;
    public double requestProof() throws RemoteException;
    public void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException;
    public double answer_the_call() throws RemoteException;
    public int accepts_communication(String username) throws RemoteException;
    public void addOpinion(OpinionTopic op) throws RemoteException;
}
