/**
 * La classe Client représente un client dans le système RMI (Remote Method Invocation).
 * Elle gère les interactions de l'utilisateur, y compris l'envoi et la réception d'opinions,
 * la proposition de nouveaux sujets, et la gestion des abonnés.
 *
 * Champs:
 * - User user: L'utilisateur courant.
 * - Monitor monitor: Le moniteur associé à l'utilisateur.
 * - UserRemote stub: Le stub pour interagir avec le serveur distant.
 * - String password: Le mot de passe pour accéder aux fonctionnalités sécurisées.
 *
 * Méthodes:
 * - public static void main(String[] args): Le point d'entrée du programme client.
 * - private void sendOpinionToUser(): Envoie une opinion à un autre utilisateur.
 * - public void sendOpinionTo(OpinionTopic op, String username): Envoie une opinion à un utilisateur spécifié.
 * - public void addOp(): Ajoute une opinion à l'utilisateur courant.
 * - public Client(): Constructeur qui initialise l'utilisateur, son moniteur et son mot de passe.
 * - public void showMenu(): Affiche le menu principal en fonction du type d'utilisateur.
 * - public void ShowMenuProposer(): Affiche le menu pour un utilisateur de type PROPOSER.
 * - public void ShowMenuInfluencer(): Affiche le menu pour un utilisateur de type INFLUENCER.
 * - public void ShowMenuConsesusFinder(): Affiche le menu pour un utilisateur de type CONSENSUS_FINDER.
 * - private void showMenuRegularUser(): Affiche le menu pour un utilisateur de type REGULAR_USER.
 * - public void checkNotification(): Vérifie les notifications pour l'utilisateur.
 * - public int displayMenuAndGetChoiceProposer(): Affiche le menu et obtient le choix pour un utilisateur de type PROPOSER.
 * - public int displayMenuAndGetChoiceInfulencer(): Affiche le menu et obtient le choix pour un utilisateur de type INFLUENCER.
 * - public int displayMenuAndGetChoiceConsensusFinder(): Affiche le menu et obtient le choix pour un utilisateur de type CONSENSUS_FINDER.
 * - public int displayMenuAndGetChoiceRegularUser(): Affiche le menu et obtient le choix pour un utilisateur de type REGULAR_USER.
 * - public void connect(): Connecte le client au serveur RMI.
 * - public void cmdfollow(): Permet à un utilisateur de suivre un influenceur.
 * - public void diffuserOP(): Diffuse une opinion à tous les abonnés.
 * - public void Talk(): Permet à un CONSENSUS_FINDER de trouver des paires et de les faire discuter.
 * - public void findPairsAndMakeThemTalk(Topic topic): Trouve des paires d'utilisateurs pour discuter d'un sujet spécifique.
 * - public void proposer(): Propose un nouveau sujet à tous les utilisateurs.
 * - public static String passwordMonitor(): Génère un mot de passe aléatoire pour le moniteur.
 */
import models.*;
import services.ClientMonitor;
import services.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Double.parseDouble;



public class Client {
    public User user;
    public Monitor monitor;
    public UserRemote stub;
    private String password;

    public static void main(String[] args)  {
        try {
            Client client = new Client();
            client.connect();
            /*OpinionTopic c = new OpinionTopic(client.user,new Topic("is the Raja CA the best club in the world ?"),1);
            client.user.addOpinion(c);
            client.user.displayOpinions();
            client.sendOpinionTo(c,"koceila1");
            client.sendOpinionTo(c,"koceila2");
            client.sendOpinionTo(c,"koceila3");
            client.findPairsAndMakeThemTalk(c.getTopic());*/



            client.showMenu();

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    private void sendOpinionToUser() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, ClientMonitor> map= (HashMap<String, ClientMonitor>) stub.getUsers();
        HashMap<String, OpinionTopic> map1 = this.user.getOpinions();
        System.out.println("Connected users");
        for (String username: map.keySet()){
            if(!username.equals(user.getUsername()))
                System.out.println(username + " is connected !");
        }
        System.out.print("Enter username to send opinion to: ");
        String username = scanner.nextLine();
        System.out.println("Your topics");
        for (String topic: map1.keySet()){
            System.out.println(topic+" is connected !");
        }
        System.out.print("Enter topic ID: ");
        String topicId = scanner.nextLine();

        sendOpinionTo(this.user.getOpinion(new Topic(topicId)), username);
    }
    public void sendOpinionTo(OpinionTopic op, String username) throws RemoteException {
        ClientMonitor receiver = (ClientMonitor) stub.getClientMonitor(username);
        new Thread(()->{
            try {
                receiver.sendOpinion(op,this.monitor);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();

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
        this.password = Client.passwordMonitor();
        this.monitor = new Monitor(user,password);

    }
    public  void showMenu(){
        this.monitor.llock(this.password);
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
        this.monitor.unllock(this.password);
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
                        stub.exit_system(user.getUsername(),user.getUserType());
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
                        diffuserOP();
                        break;
                    case 2:
                        addOp();
                        break;
                    case 3:
                        this.user.displayOpinions();
                        break;
                    case 4:
                        this.checkNotification();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        stub.exit_system(user.getUsername(),user.getUserType());
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
                        Talk();
                        System.out.println("dfgh");
                        break;
                    case 2:
                        System.out.println("Exiting...");
                        stub.exit_system(user.getUsername(),user.getUserType());
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
                    cmdfollow();
                    break;
                case 5:
                    checkNotification();
                    System.out.println("checked !");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    stub.exit_system(user.getUsername(),user.getUserType());
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
public void checkNotification() throws InterruptedException {
    this.monitor.unllock(this.password);
    Thread.sleep(100);
    this.monitor.llock(this.password);
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
            System.out.println("4. Check Update");
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
            System.out.println("1. Search for pairs of agents A and B to talk");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");


        }
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine();
        } else {
            scanner.nextLine();
        }

        return choice;
    }


    public int displayMenuAndGetChoiceRegularUser() {
    Scanner scanner = new Scanner(System.in);
    int choice = -1;
    if ((this.user.getUserType() != UserType.PROPOSER)&& (this.user.getUserType() != UserType.INFLUENCER)  && (this.user.getUserType() != UserType.CONSENSUS_FINDER)) {
        System.out.println("1. Add an Opinion");
        System.out.println("2. Display Opinions");
        System.out.println("3. Send Opinion to User");
        System.out.println("4. follow some one ");
        System.out.println("5. checkNotification");
        System.out.println("6. Exit");
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
        stub = (UserRemote) registry.lookup("Server");
        stub.addListener(monitor);
    }
    public void cmdfollow() throws RemoteException {
        if(this.user.getUserType()==UserType.CONSENSUS_FINDER
                || this.user.getUserType()==UserType.PROPOSER
                || this.user.getUserType()==UserType.INFLUENCER){
            System.err.println("You can't follow !");
            return;
        }
        HashMap<String, ClientMonitor> map= (HashMap<String, ClientMonitor>) stub.getUsers();
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
        String finalInfsel = infsel;
        new Thread(()->{
            try {
                this.stub.getClientMonitor(finalInfsel).addFollower(this.user.getUsername());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            System.out.println(finalInfsel +" is followed !!");
        }).start();


    }
    public void diffuserOP() throws RemoteException {
        if(this.user.getUserType()!=UserType.INFLUENCER){
            System.err.println("You can't diffuse !");
            return;
        }

        HashMap<String, OpinionTopic> map1 = this.user.getOpinions();
        System.out.println("Diffuser opinions : ");
        if (map1.isEmpty()) {
            System.out.println("No opinions to diffuse ! add some opinions al hmar !");
            return;
        }
        String Topic;
        ArrayList<String> infs = new ArrayList<String>();
        for (String topic: map1.keySet()){
            System.out.printf("Topic : %s \n",topic);
            infs.add(topic);
        }
        do{
            java.io.Console console = System.console();
            Topic = console.readLine("Topic : ");
            if(infs.contains(Topic)){
                break;
            }
        }while(true);
        String finalTopic = Topic;
        for(String follower: this.user.getFollowrs()){

                new Thread(()->{
                    try {
                        ClientMonitor tempM = this.stub.getClientMonitor(follower);
                        tempM.sendOpinion(this.user.getOpinion(new Topic(finalTopic)),this.monitor);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

            }



    }
    public void Talk() throws RemoteException, ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        List<ClientMonitor> clientMonitors = this.stub.getClientMonitors();
         Set<String> list_topics = new HashSet<>();
        for(ClientMonitor c : clientMonitors)
            if (c != null && c.getUser() != null && c.getUser().getOpinions() != null)
                list_topics.addAll(c.getUser().getOpinions().keySet());

        System.out.println("Your topics :");
        for(String topic : list_topics)
            System.out.println(topic + " is connected");

        System.out.print("Enter topic ID: ");
        String topicId = scanner.nextLine();
        if (topicId == null || topicId.trim().isEmpty()) {
            System.out.println("Invalid topic ID.");
            return;
        }
        Topic topic=new Topic(topicId);
        new Thread(()->{
            try {
                this.findPairsAndMakeThemTalk(topic);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }
    // Cette méthode ci-dessous est implementé uniquement par le CONSENSUS_FINDER :
    public void findPairsAndMakeThemTalk(Topic topic) throws RemoteException, InterruptedException, ExecutionException {
        if(this.user.getUserType() != UserType.CONSENSUS_FINDER)
        {
            System.err.println("You are not a consensus finder. ");
            return;
        }
        // J'ai récupéré tous les monitors existants dans le serveur :)
        List<ClientMonitor> myClientMonitors = stub.getClientMonitors();
        List<OpinionTopic> opinionTopics = new ArrayList<>();

        // Nous avons tous les users qui ont une opinion sur le topic :)
        for(ClientMonitor m : myClientMonitors) {
                opinionTopics.add(m.getUser().getOpinion(topic));
        }
        System.out.println("123456");
        for(OpinionTopic o : opinionTopics) {
            for (OpinionTopic op : opinionTopics) {
                if (op==null) break;
                if (o == null || o.equals(op)) continue;
                if ((o.getUser().getUserType() != UserType.CONSENSUS_FINDER) && (op.getUser().getUserType() != UserType.CONSENSUS_FINDER)) {
                    // On récupére les deux monitors :
                    ClientMonitor c1 = this.stub.getClientMonitor(o.getUser().getUsername());
                    ClientMonitor c2 = this.stub.getClientMonitor(op.getUser().getUsername());

                    System.out.println("1---------> " + o.getOx() + " AND " + op.getOx());
                    ExecutorService executor = Executors.newFixedThreadPool(2);
                    List<Callable<Double>> callables = new ArrayList<>();
                    callables.add(() -> {
                        return (c1.answer_the_call());
                    });callables.add(() -> {
                        return (c2.answer_the_call());
                    });
                    List<Future<Double>> futures = executor.invokeAll(callables);
                    double answer_call_c1 = futures.get(0).get();
                    double answer_call_c2 = futures.get(1).get();
                    // Vérifions si c1 & c2 ont répondu à l'appel :)
                    if (answer_call_c1 >= 0.5 && answer_call_c2 >= 0.5) {
                        futures.clear();
                        // Vérifions s'ils acceptent la communication :)
                        ExecutorService executor2 = Executors.newFixedThreadPool(2);
                        List<Callable<Double>> callables1 = new ArrayList<>();
                        callables1.add(() -> {
                            return (double) c1.accepts_communication(c2.getUser().getUsername());
                        });callables1.add(() -> {
                            return (double) c2.accepts_communication(c1.getUser().getUsername());
                        });
                        List<Future<Double>> futures2 = executor2.invokeAll(callables);

                        if (futures2.get(0).get() == 1 && futures2.get(1).get() == 1) {
                            futures2.clear();
                            double OA = o.getOx();
                            double OB = op.getOx();
                            double averageOpinion = (OA + OB) / 2;
                            c1.addOpinion(new OpinionTopic(c1.getUser(), topic, averageOpinion));
                            c2.addOpinion(new OpinionTopic(c2.getUser(), topic, averageOpinion));
                            return;
                        } else
                            System.out.println("Communication not accepted between " + c1.getUser().getUsername() + " and " + c2.getUser().getUsername());
                    } else
                        System.out.println("Call not answered by " + (answer_call_c1 < 0.5 ? c1.getUser().getUsername() : "") + (answer_call_c2 < 0.5 ? " and " + c2.getUser().getUsername() : ""));
                }
            }
        }
    System.exit(0);
    }

    // Cette méthode va être implementé uniquement par le Proposer :)
    public void proposer() throws RemoteException{
        if (this.user.getUserType() != UserType.PROPOSER) {
            System.err.println("You are not a proposer User.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.err.print(" Enter your Topic : ");
        String topic = scanner.nextLine();
        new Thread(() -> {
            try {
                HashMap<String, ClientMonitor> map = (HashMap<String, ClientMonitor>) stub.getUsers();

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
                //System.out.println("All proposals have been sent and processed.");
            } catch (RemoteException e) {
                System.err.println("RemoteException during propose: " + e.getMessage());
            }
        }).start();
        System.out.println("done.");
    }
    public static String passwordMonitor(){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
        SecureRandom RANDOM = new SecureRandom();
        int passwordLength = 12;
        StringBuilder password = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }



}

