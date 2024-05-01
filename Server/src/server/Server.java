package server;
import models.OnOfflineUser;
import models.User;
import services.UserRemote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class Server implements UserRemote {
    private List<OnOfflineUser> users;
    public Server() {
        users = new ArrayList<>();
    }
    @Override
    public void direBonjour() throws RemoteException {
        System.out.println("Bonjour");
    }
    public void addUser(User user) throws RemoteException {
        users.add(new OnOfflineUser(user));
    }





}
