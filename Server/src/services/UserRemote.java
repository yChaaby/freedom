package services;
import models.UserType;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface UserRemote extends Remote {
    public ClientMonitor getClientMonitor(String username) throws RemoteException;
    public void addListener(ClientMonitor c) throws RemoteException;
    public Map<String, ClientMonitor> getUsers() throws RemoteException;
    public List<ClientMonitor> getClientMonitors() throws RemoteException;

    public void idDisconnected(String name, UserType type) throws RemoteException;
}
