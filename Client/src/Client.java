import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
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
            java.io.Console console = System.console();
            String unT = console.readLine("Enter a topic a sov: ");
            OpinionTopic c = new OpinionTopic(client.user,new Topic(unT),1);
            client.user.addOpinion(c);
            client.user.displayOpinions();
            //client.sendOpinionTo(c,"koceila1");
            client.proposer();
            client.cmdfollow();
            if(client.user.getUserType()==UserType.INFLUENCER){
                console.readLine("Enter to next: ");
                client.diffuserOP();
            }
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

    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
        this.stub = (UserRemote) registry.lookup("Server");
        stub.addListener(monitor);
    }
    public void cmdfollow() throws RemoteException {
        if(this.user.getUserType()==UserType.CONSENSUS_FINDER
                || this.user.getUserType()==UserType.PROPOSER
                || this.user.getUserType()==UserType.INFLUENCER){
            System.err.println("You can't follow !");
            return;
        }
        HashMap<String, ClientMonitor> map= (HashMap<String, ClientMonitor>) this.stub.getUsers();
        HashMap<String, OpinionTopic> map1 = this.user.getOpinions();
        System.out.println("Connected Influencers : ");
        ArrayList<String> infs = new ArrayList<String>();
        for (ClientMonitor inf: map.values()){
            if(inf!=null&&(inf.getUser().getUserType()==UserType.INFLUENCER)) {
                String username = inf.getUser().getUsername();
                System.out.printf("The influencer %s is connected\n",username);
                infs.add(username);
            }
        }


        if (infs.isEmpty()) {
            return;
        }
        String infsel;
        do {
            java.io.Console console = System.console();
            infsel = console.readLine("Enter a influencer: ");
            if(infs.contains(infsel)){break;}
        } while (true);

        this.stub.getClientMonitor(infsel).addFollower(this.user.getUsername());
        System.out.println(infsel+" is followed !!");

    }
    public void diffuserOP() throws RemoteException {
        HashMap<String, OpinionTopic> map1 = this.user.getOpinions();
        System.out.println("Diffuser opinions : ");
        if (map1.isEmpty()) {
            System.out.println("No opinions to diffuse ! add some opinions al hmar !");
            return;
        }
        ArrayList<String> infs = new ArrayList<String>();
        for (String topic: map1.keySet()){
            System.out.printf("Topic : %s your\n",topic);
            infs.add(topic);
        }
        do{
            java.io.Console console = System.console();
            String Topic = console.readLine("Topic : ");
            if(infs.contains(Topic)){
                for(String follower: this.user.getFollowrs()){
                    ClientMonitor tempM=this.stub.getClientMonitor(follower);
                    tempM.sendOpinion(this.user.getOpinion(new Topic(Topic)));
                }
                break;
            }
        }while(true);

    }
    public void proposer() throws RemoteException{
        if (this.user.getUserType() != UserType.PROPOSER) {
            System.err.println("You are not a proposer User.");
            return;
        }
        new Thread(() -> {
            try {

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
            } catch (RemoteException e) {
                System.err.println("RemoteException during propose: " + e.getMessage());
            }
        }).start();
        System.out.println("done.");
    }


}