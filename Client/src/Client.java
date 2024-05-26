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

import static java.lang.Double.parseDouble;

public class Client {
    public User user;
    public Monitor monitor;
    public UserRemote stub;
    public static void main(String[] args)  {
        try {
            Client client = new Client();
            client.connect();


            /*client.user.displayOpinions();
            client.sendOpinionToUser();*/
            client.showMenu();
            //client.proposer();

            //ClientMonitor receiver = (ClientMonitor) client.stub.getClientMonitor("koceila1");
            //receiver.displayMessage("Dima Dima RAJA");


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    private void sendOpinionToUser() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, ClientMonitor> map= (HashMap<String, ClientMonitor>) this.stub.getUsers();
        HashMap<String, OpinionTopic> map1 = this.user.getOpinions();
        System.out.println("Connected users");
        for (String username: map.keySet()){
            System.out.println(STR."\{username} is connected");
        }
        System.out.print("Enter username to send opinion to: ");
        String username = scanner.nextLine();
        System.out.println("Your topics");
        for (String topic: map1.keySet()){
            System.out.println(STR."\{topic} is connected");
        }
        System.out.print("Enter topic ID: ");
        String topicId = scanner.nextLine();

        sendOpinionTo(this.user.getOpinion(new Topic(topicId)), username);
    }
    public void sendOpinionTo(OpinionTopic op, String username) throws RemoteException {
        ClientMonitor receiver = (ClientMonitor) this.stub.getClientMonitor(username);
        receiver.sendOpinion(op);
    }
    public void addOp(){
        System.out.print("About what topic :");
        Scanner sc = new Scanner(System.in);
        String topicid = sc.nextLine();
        System.out.print("So, what do you think ? :");
        double ox = parseDouble(sc.nextLine());
        OpinionTopic c = new OpinionTopic(this.user,new Topic(topicid), ox);
        this.user.addOpinion(c);
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
        System.out.print("Enter your function (1 REGULAR_USER," +
                "  2  INFLUENCER," +
                "  3  CRITICAL_THINKER," +
                "  4  PROPOSER," +
                "  5  CONSENSUS_FINDER ) : ");
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
    public  void showMenu(){
        if (this.user.getUserType() == UserType.REGULAR_USER){
            showMenuRegularUser();
        }
        else if (this.user.getUserType() ==UserType.PROPOSER){
            ShowMenuProposer();
        }
        else if(this.user.getUserType() ==UserType.INFLUENCER){
            ShowMenuInfluencer();
        }
        else if(this.user.getUserType() ==UserType.CRITICAL_THINKER){
            ShowMenuCriticalThinker();
        }
        else if(this.user.getUserType() ==UserType.CONSENSUS_FINDER){
            ShowMenuConsesusFinder();
        }
    }

    public void ShowMenuProposer() {
        while (true) {
            System.out.println("\n--- Menu ---");
            int choice = displayMenuAndGetChoiceProposer();
            try {
                switch (choice) {
                    case 1:
                        proposer();
                        break;
                    case 2:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e) {
                System.err.println("Error: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    public void ShowMenuInfluencer() {
        while (true) {
            System.out.println("\n--- Menu ---");
            int choice = displayMenuAndGetChoiceInfulencer();
            try {
                switch (choice) {
                    case 1:
                        //InfluenceMethode1
                        System.out.println("InfluenceMethode1");
                        break;
                    case 2:
                        //InfluenceMethode2
                        System.out.println("InfluenceMethode2");
                        break;
                    case 3:
                        //InfluenceMethode3
                        System.out.println("InfluenceMethode3");
                        break;
                    case 4:
                        //InfluenceMethode4
                        System.out.println("InfluenceMethode4");
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e) {
                System.err.println("Error: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    public void ShowMenuCriticalThinker() {
        while (true) {
            System.out.println("\n--- Menu ---");
            int choice = displayMenuAndGetChoiceCriticalThinker();
            try {
                switch (choice) {
                    case 1:
                        //CriticalThinkerMethode1
                        System.out.println("CriticalThinkerMethode1");
                        break;
                    case 2:
                        //CriticalThinkerMethode2
                        System.out.println("CriticalThinkerMethode2");
                        break;
                    case 3:
                        //CriticalThinkerMethode3
                        System.out.println("CriticalThinkerMethode3");
                        break;
                    case 4:
                        //CriticalThinkerMethode4
                        System.out.println("CriticalThinkerMethode4");
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e) {
                System.err.println("Error: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    public void ShowMenuConsesusFinder() {
        while (true) {
            System.out.println("\n--- Menu ---");
            int choice = displayMenuAndGetChoiceConsensusFinder();
            try {
                switch (choice) {
                    case 1:
                        //ConsensusFinderMethode1
                        System.out.println("ConsensusFinderMethode1");
                        break;
                    case 2:
                        //CriticalThinkerMethode2
                        System.out.println("ConsensusFinderMethode2");
                        break;
                    case 3:
                        //CriticalThinkerMethode3
                        System.out.println("ConsensusFinderMethode3");
                        break;
                    case 4:
                        //CriticalThinkerMethode4
                        System.out.println("ConsensusFinder4");
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e) {
                System.err.println("Error: " + e.toString());
                e.printStackTrace();
            }
        }
    }
    private void showMenuRegularUser() {
     while (true) {
        System.out.println("\n--- Menu ---");
        int choice = displayMenuAndGetChoiceRegularUser();

        try {
            switch (choice) {
                case 1:
                    addOp();
                    break;
                case 2:
                    this.user.displayOpinions();
                    break;
                case 3:
                    sendOpinionToUser();
                    break;
                case 4:
                    //followSomeOne()
                    System.out.println("followSomeOne()");
                    break;

                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.toString());
            e.printStackTrace();
        }
    }
}
public int displayMenuAndGetChoiceProposer(){
    Scanner scanner = new Scanner(System.in);
    int choice = -1;
    if ((this.user.getUserType() != UserType.REGULAR_USER)&& (this.user.getUserType() != UserType.INFLUENCER) && (this.user.getUserType() != UserType.CRITICAL_THINKER) && (this.user.getUserType() != UserType.CONSENSUS_FINDER)) {
        System.out.println("1. Propose a topic");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");


    }
    if (scanner.hasNextInt()) {
        choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
    } else {
        scanner.nextLine(); // consume the invalid input
    }

    return choice;
}
public int displayMenuAndGetChoiceInfulencer(){
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        if ((this.user.getUserType() != UserType.REGULAR_USER)&& (this.user.getUserType() != UserType.PROPOSER) && (this.user.getUserType() != UserType.CRITICAL_THINKER) && (this.user.getUserType() != UserType.CONSENSUS_FINDER)) {
            System.out.println("1. InfluencerMethode1");
            System.out.println("2. InfluencerMethode2");
            System.out.println("3. InfluencerMethode3");
            System.out.println("4. InfluencerMethode4");

            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");


        }
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } else {
            scanner.nextLine(); // consume the invalid input
        }

        return choice;
    }
    public int displayMenuAndGetChoiceConsensusFinder(){
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        if ((this.user.getUserType() != UserType.REGULAR_USER)&& (this.user.getUserType() != UserType.PROPOSER) && (this.user.getUserType() != UserType.CRITICAL_THINKER) && (this.user.getUserType() != UserType.INFLUENCER)) {
            System.out.println("1. ConsensusFinderMethode1");
            System.out.println("2. ConsensusFinderMethode2");
            System.out.println("3. ConsensusFinderMethode3");
            System.out.println("4. ConsensusFinderMethode4");

            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");


        }
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } else {
            scanner.nextLine(); // consume the invalid input
        }

        return choice;
    }
    public int displayMenuAndGetChoiceCriticalThinker(){
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        if ((this.user.getUserType() != UserType.REGULAR_USER)&& (this.user.getUserType() != UserType.PROPOSER) && (this.user.getUserType() != UserType.INFLUENCER) && (this.user.getUserType() != UserType.CONSENSUS_FINDER)) {
            System.out.println("1. CriticalThinkerMethode1");
            System.out.println("2. CriticalThinkerMethode2");
            System.out.println("3. CriticalThinkerMethode3");
            System.out.println("4. CriticalThinkerMethode4");

            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");


        }
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } else {
            scanner.nextLine(); // consume the invalid input
        }

        return choice;
    }

    public int displayMenuAndGetChoiceRegularUser() {
    Scanner scanner = new Scanner(System.in);
    int choice = -1;
    if ((this.user.getUserType() != UserType.PROPOSER)&& (this.user.getUserType() != UserType.INFLUENCER) && (this.user.getUserType() != UserType.CRITICAL_THINKER) && (this.user.getUserType() != UserType.CONSENSUS_FINDER)) {
        System.out.println("1. Add an Opinion");
        System.out.println("2. Display Opinions");
        System.out.println("3. Send Opinion to User");
        System.out.println("4. follow some one ");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

    }
    if (scanner.hasNextInt()) {
        choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
    } else {
        scanner.nextLine(); // consume the invalid input
    }

    return choice;
    }
    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
        this.stub = (UserRemote) registry.lookup("Server");
        stub.addListener(monitor);
    }

    public void proposer() throws RemoteException{

        //scanner.close();
        new Thread(() -> {
            try {
                // N'oubliez pas de fermer le scanner
                if (this.user.getUserType() != UserType.PROPOSER) {
                    System.err.println("You are not a proposer User.");
                    return;
                }

                Scanner scanner = new Scanner(System.in);
                System.err.print(" Enter your Topic : ");
                String topic = scanner.nextLine();
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
            } catch (RemoteException e) {
                System.err.println("RemoteException during propose: " + e.getMessage());
            }
        }).start();
        System.out.println("done.");

    }


}