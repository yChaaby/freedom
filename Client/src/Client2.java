import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

import models.Monitor;
import models.User;
import services.UserRemote;

public class Client2 {
    private User user;
    public static void main(String[] args) {
        try {
            User thisuser = new User("koceila1",new Date("21/10/1945"));
            Monitor monitor = new Monitor(thisuser);
            //System.out.println("tapez le username d'un utilisateur : ");
            //Scanner sc = new Scanner(System.in);

            // Service of names
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
            // Finding the remote object
            UserRemote stub = (UserRemote) registry.lookup("Server");
            stub.addListener(monitor);

            //Monitor receiver = stub.getClientMonitor(sc.next());
            //Thread.sleep(100000);



        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}