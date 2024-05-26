import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

            //client.proposer();


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
            System.out.println(username+" is connected");
        }
        System.out.print("Enter username to send opinion to: ");
        String username = scanner.nextLine();
        System.out.println("Your topics");
        for (String topic: map1.keySet()){
            System.out.println(topic+" is connected");
        }
        System.out.print("Enter topic ID: ");
        String topicId = scanner.nextLine();

        sendOpinionTo(this.user.getOpinion(new Topic(topicId)), username);
    }
    public void sendOpinionTo(OpinionTopic op, String username) throws RemoteException {
        ClientMonitor receiver = (ClientMonitor) this.stub.getClientMonitor(username);
        receiver.sendOpinion(op,this.monitor);
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
            showMenuRegularUser();
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
                        diffuserOP();
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
                    cmdfollow();
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
            System.out.println("1. Diffuse an opinion !");
            System.out.println("2. Add opinion !");
            System.out.println("3. Display opinion !");
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
                    tempM.sendOpinion(this.user.getOpinion(new Topic(Topic)),this.monitor);
                }
                break;
            }
        }while(true);

    }

    // Cette méthode ci-dessous est implementé uniquement par le CONSENSUS_FINDER :
    public void findPairsAndMakeThemTalk(Topic topic) throws RemoteException {
        if(this.user.getUserType() != UserType.CONSENSUS_FINDER)
        {
            System.err.println("You are not a consensus finder. ");
            return;
        }
        // J'ai récupéré tous les monitors existants dans le serveur :)
        List<ClientMonitor> myClientMonitors = this.stub.getClientMonitors();
        if(myClientMonitors.size() < 2)
            return;

        List<OpinionTopic> opinionTopics = new ArrayList<>();

        // Nous avons tous les users qui ont une opinion sur le topic :)
        for(ClientMonitor m : myClientMonitors) {
            opinionTopics.add(m.getUser().getOpinion(topic));
        }

        for(OpinionTopic o : opinionTopics)
            for(OpinionTopic op : opinionTopics)
            {
                if(!o.equals(op)) {
                    if((o.getUser().getUserType() != UserType.CONSENSUS_FINDER) && (op.getUser().getUserType() != UserType.CONSENSUS_FINDER))
                    {
                        // On récupére les deux monitors :
                        ClientMonitor c1 = this.stub.getClientMonitor(o.getUser().getUsername());
                        ClientMonitor c2 = this.stub.getClientMonitor(op.getUser().getUsername());

                        // Vérifions si c1 & c2 ont répondu à l'appel :)
                        if(c1.answer_the_call() >= 0.5 &&  c2.answer_the_call() >= 0.5) {
                            // Vérifions s'ils acceptent la communication :)
                            if(c1.accepts_communication(c2.getUser().getUsername()) == 1 && c2.accepts_communication(c1.getUser().getUsername()) == 1){
                                double OA = o.getOx();
                                double OB = op.getOx();
                                double averageOpinion = (OA + OB) / 2;
                                c1.addOpinion(new OpinionTopic(c1.getUser(),topic,averageOpinion));
                                c2.addOpinion(new OpinionTopic(c2.getUser(),topic,averageOpinion));
                                c1.displayMessage("My new opinion on [" + topic.getIdTopic() + "] : " + c1.getUser().getOpinion(topic).getOx());
                                c2.displayMessage("My new opinion on [" + topic.getIdTopic() + "] : " + c2.getUser().getOpinion(topic).getOx());
                                return;
                            }
                            else
                                System.out.println("Communication not accepted between " + c1.getUser().getUsername() + " and " + c2.getUser().getUsername());
                        }
                        else
                            System.out.println("Call not answered by " + (c1.answer_the_call() < 0.5 ? c1.getUser().getUsername() : "") + (c2.answer_the_call() < 0.5 ? " and " + c2.getUser().getUsername() : ""));
                    }
                }
                else
                    continue;
            }
    }

    // Cette méthode va être implementé uniquement par le Proposer :)
    public void proposer() throws RemoteException{
        if (this.user.getUserType() != UserType.PROPOSER) {
            System.err.println("You are not a proposer User.");
            return;
        }
        new Thread(() -> {
            try {

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

