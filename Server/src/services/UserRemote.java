package services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    public abstract void direBonjour() throws RemoteException;
}
