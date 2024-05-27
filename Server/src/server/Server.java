package server;

import models.UserType;
import services.ClientMonitor;
import services.UserRemote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server implements UserRemote{
    private Map<String,ClientMonitor> users;
    public Server() {
        users = new HashMap<>();
    }
    @Override
    public void addListener(ClientMonitor c) throws RemoteException {
        if(users.containsKey(c.getUser().getUsername())){
            throw new InternalError("the user already exists");
        }
        users.put(c.getUser().getUsername(),c);
        System.out.println(c.getUser().getUsername() +" is an " +c.getUser().getUserType() +" connected !");
    }

    public void exit_system(String username,UserType type) throws RemoteException {
        users.remove(username);
        System.out.println(username + " is an " + type + " disconnected !");
    }
   @Override
    public ClientMonitor getClientMonitor(String username) throws RemoteException {
       if (users.containsKey(username)) {
           return users.get(username);
       }
        throw new InternalError("There is no user with username " + username);
    }
    @Override
    public Map<String, ClientMonitor> getUsers() throws RemoteException {
        return users;
    }

    @Override
    public void isDisconnected(String name, UserType type) throws RemoteException{
        System.out.println(name + " is an " + type + " disconnected !");
    }
    public List<ClientMonitor> getClientMonitors() throws RemoteException {
        List<ClientMonitor> myListMonitors = new ArrayList<>();
        for(Map.Entry<String,ClientMonitor> entry : users.entrySet())
        {
            myListMonitors.add(entry.getValue());
        }
        return myListMonitors;
    }
}
