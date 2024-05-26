package services;

import models.OpinionTopic;
import models.Topic;
import models.User;
import models.UserType;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientMonitor extends Remote {
    public void displayMessage(String msg) throws RemoteException;
    public User getUser() throws RemoteException;
    public void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException;
    public void propose(Topic t)throws RemoteException;
    public double requestProof() throws RemoteException;
    public double answer_the_call() throws RemoteException;
    public int accepts_communication(String username) throws RemoteException;
    public void addOpinion(OpinionTopic op) throws RemoteException;
}
