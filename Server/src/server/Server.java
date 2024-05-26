package server;
import services.ClientMonitor;
import services.UserRemote;
import java.io.Serializable;
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
    public void direBonjour(int a) throws RemoteException {
        System.out.println("Bonjour "+a);
    }
    @Override
    public void addListener(ClientMonitor c) throws RemoteException {
        users.put(c.getUser().getUsername(),c);
        System.out.println(c.getUser().getUsername()+" is an "+c.getUser().getUserType() +" connected !");
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
}
