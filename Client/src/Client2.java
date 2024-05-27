import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

import models.Monitor;
import models.User;
import models.UserType;
import services.UserRemote;

public class Client2 {
    private User user;
    public static void main(String[] args) {
        try {
            User thisuser = new User("koceila3",new Date("21/10/1945"), UserType.REGULAR_USER);
            Monitor monitor = new Monitor(thisuser);
            //System.out.println("tapez le username d'un utilisateur : ");
            //Scanner sc = new Scanner(System.in);

            // Service of names
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
            // Finding the remote object
            UserRemote stub = (UserRemote) registry.lookup("Server");
            stub.addListener(monitor);



        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}