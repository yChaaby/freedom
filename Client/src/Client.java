import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import models.*;
import services.ClientMonitor;
import services.UserRemote;

public class Client {
    public User user;
    public Monitor monitor;
    public UserRemote stub;
    public static void main(String[] args)  {
        try {
            Client client = new Client();
            client.connect();
            OpinionTopic c = new OpinionTopic(client.user,new Topic("is the Raja CA the best in the world ?"),1);
            client.user.addOpinion(c);
            client.user.displayOpinions();
            client.sendOpinionTo(c,"koceila1");
            client.proposer();
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
    public void addOpinion(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter opinion (ex: 0.5) / 1) : ");
        float opinion = scanner.nextFloat();
    }
    public Client() throws RemoteException {
        String username;
        Date birthday;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        username = scanner.nextLine();
        System.out.print("Enter your birthday (format DD/MM/YYYY): ");
        birthday = new Date(scanner.nextLine());
        System.out.print("Enter your function (1 REGULAR_USER,\n" +
                "  2  INFLUENCER,\n" +
                "  3  CRITICAL_THINKER,\n" +
                "  4  PROPOSER,\n" +
                "  4  CONSENSUS_FINDER,) : ");
        int functionChoice = scanner.nextInt();
        UserType userType = UserType.REGULAR_USER; // Default

        switch (functionChoice) {
            case 1:
                userType = UserType.REGULAR_USER;
                break;
            case 2:
                userType = UserType.INFLUENCER;
                break;
            case 3:
                userType = UserType.CRITICAL_THINKER;
                break;
            case 4:
                userType = UserType.PROPOSER;
                break;
            case 5:
                userType = UserType.CONSENSUS_FINDER;
                break;
            default:
                System.out.println("Invalid choice. Defaulting to REGULAR_USER.");
                break;
        }
        this.user = new User(username,birthday, userType);
        this.monitor = new Monitor(user);
    }

    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
        this.stub = (UserRemote) registry.lookup("Server");
        stub.addListener(monitor);
    }
    public void proposer() throws RemoteException{
        if (this.user.getUserType() != UserType.PROPOSER) {
            System.err.println("You are not a proposer User.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.err.print("Topic : ");
        String topic = scanner.nextLine();
        scanner.close(); // N'oubliez pas de fermer le scanner

        HashMap<String, ClientMonitor> map = (HashMap<String, ClientMonitor>) this.stub.getUsers();

        // Création d'une liste de CompletableFuture pour chaque proposition
        List<CompletableFuture<Void>> futures = map.entrySet().stream().filter(user-> {
                    try {
                        return (user.getValue().getUser().getUserType()!=UserType.PROPOSER);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(user -> CompletableFuture.runAsync(() -> {
                    try {

                        user.getValue().propose(new Topic(topic));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        // Attendre la complétion de toutes les futures
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("All proposals have been sent and processed.");

    }


}