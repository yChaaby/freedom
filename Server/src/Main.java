
import services.UserRemote;
import server.Server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Give me a port :");
            int port = 12345;//scanner.nextInt();
            Server server = new Server();
            // Making the object available for remote invocations
            UserRemote stub = (UserRemote) UnicastRemoteObject.exportObject(server, port);
            LocateRegistry.createRegistry(port);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
            registry.bind("Server", stub);

            System.err.println("Server ready");


        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}