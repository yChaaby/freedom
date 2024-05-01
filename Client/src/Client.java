import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

import models.User;
import services.UserRemote;

public class Client {
    private User user;
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            // Service of names
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
            // Finding the remote object
            UserRemote stub = (UserRemote) registry.lookup("Server");
            stub.direBonjour();
            sc.nextInt();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}