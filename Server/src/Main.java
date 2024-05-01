
import services.UserRemote;
import server.Server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String args[]) {

        try {
            Server server = new Server();
            // Making the object available for remote invocations
            UserRemote stub = (UserRemote) UnicastRemoteObject.exportObject(server, 12345);
            LocateRegistry.createRegistry(12345);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 12345);
            registry.bind("Server", stub);

            System.err.println("Server ready");


        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}