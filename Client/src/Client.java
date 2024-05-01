import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;
import models.OpinionTopic;
import models.Topic;
import services.ClientMonitor;
import models.Monitor;
import models.User;
import services.UserRemote;

public class Client {
    public User user;
    public Monitor monitor;
    public UserRemote stub;
    public static void main(String[] args)  {
        try {
            User thisuser = new User("mazigh1",new Date("21/10/2001"));
            Client client = new Client(thisuser);
            client.connect();
            OpinionTopic c = new OpinionTopic(client.user,new Topic("is the Raja CA the best in the world ?"),1);
            client.user.addOpinion(c);
            client.user.displayOpinions();
            client.sendOpinionTo(c,"koceila1");
            //ClientMonitor receiver = (ClientMonitor) client.stub.getClientMonitor("koceila1");
            //receiver.displayMessage("Dima Dima RAJA");


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    public void sendOpinionTo(OpinionTopic op, String username) throws RemoteException {
        ClientMonitor receiver = (ClientMonitor) this.stub.getClientMonitor(username);
        receiver.sendOpinion(op);
    }
    public Client(User user) throws RemoteException {
        this.user = user;
        this.monitor = new Monitor(user);
    }
    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
        this.stub = (UserRemote) registry.lookup("Server");
        stub.addListener(monitor);
    }

}