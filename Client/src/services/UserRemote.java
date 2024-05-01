package services;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserRemote extends Remote {
    public void direBonjour(int a) throws RemoteException;
    public ClientMonitor getClientMonitor(String username) throws RemoteException;
    public void addListener(ClientMonitor c) throws RemoteException;
}
