/**
 * La classe Monitor est une implémentation d'objet distant qui sert de moniteur client dans un système distribué.
 * Elle implémente l'interface ClientMonitor et utilise Java RMI pour l'invocation de méthodes à distance. La classe
 * garantit la sécurité des threads grâce à l'utilisation de ReentrantLock et effectue diverses opérations liées aux utilisateurs.
 *
 * Champs:
 * - User user: L'utilisateur associé à ce moniteur.
 * - Lock lock: Un verrou réentrant pour garantir la sécurité des threads.
 * - String password: Un mot de passe pour le contrôle d'accès à certaines méthodes.
 *
 * Constructeurs:
 * - Monitor(User user, String pass): Initialise le moniteur avec un utilisateur et un mot de passe.
 * - Monitor(): Constructeur par défaut pour l'instanciation d'objet distant.
 *
 * Méthodes:
 * - void llock(String password): Verrouille le moniteur si le mot de passe est correct.
 * - void unllock(String password): Déverrouille le moniteur si le mot de passe est correct.
 * - User getUser(): Retourne l'utilisateur associé à ce moniteur.
 * - synchronized void addFollower(String username): Ajoute un abonné à l'utilisateur si celui-ci est un influenceur.
 * - synchronized void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor): Traite une opinion et met à jour les opinions de l'utilisateur.
 * - synchronized void propose(Topic t): Invite l'utilisateur à donner son avis sur un sujet.
 * - synchronized void displayMessage(String msg): Affiche un message à l'utilisateur.
 * - void addOpinion(OpinionTopic op): Ajoute une opinion aux opinions de l'utilisateur.
 * - double requestProof(): Invite l'utilisateur à fournir une preuve et retourne la valeur fournie.
 * - double answer_the_call(): Invite l'utilisateur à répondre à un appel et retourne la réponse de l'utilisateur.
 * - int accepts_communication(String username): Invite l'utilisateur à accepter ou refuser la communication avec un autre utilisateur.
 * - void setUser(User user): Définit l'utilisateur associé à ce moniteur.
 */
package models;

import services.ClientMonitor;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor extends UnicastRemoteObject implements ClientMonitor, Serializable {

    private User user;
    private final Lock lock = new ReentrantLock(true);
    private String password;


    public Monitor(User user,String pass) throws RemoteException{
    this.user=user;
    this.password=pass;
    }
    public void llock(String password){
        if(this.password!=password){
            System.out.println("YOU DO NOT HAVE THE RIGHT TO ACCES TO THIS");
            return;
        }
        this.lock.lock();
    }
    public void unllock(String password){
        if(this.password!=password){
            System.out.println("YOU DO NOT HAVE THE RIGHT TO ACCES TO THIS");
            return;
        }
        this.lock.unlock();
    }
    public Monitor() throws RemoteException {}
    @Override
    public User getUser() {
        return user;
    }
    @Override
    public synchronized void addFollower(String username) throws RemoteException {
        lock.lock();
        if (this.user.getUserType()==UserType.INFLUENCER){
            this.getUser().addFollower(username);
            System.out.println(username + " is following you ;-)");
        }
        lock.unlock();
    }
    @Override

    public synchronized void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException {
        this.lock.lock();
        if(this.user.getUserType() == UserType.CRITICAL_THINKER && op.getUser().getUserType()==UserType.INFLUENCER){
            this.lock.unlock();
            return;
        }
       //  am I a CRITICAL_THINKER ?
        if(this.user.getUserType() == UserType.CRITICAL_THINKER)
        {
            double proof = clientMonitor.requestProof();
            System.out.println("Proof requested on "+op.getTopic().getIdTopic()+" : " + proof);
            if(proof < 0.70)
            {
                System.out.println("Proof not sufficient. Opinion rejected !");
                this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),this.user.getOpinion(op.getTopic()).getOx()));
                this.user.displayOpinions();
                this.lock.unlock();
                return;
            }
        }

        if(this.user.isFirstInteraction(op.getUser())){
            this.user.addInfluenceDegree(op.getUser().getUsername(),Math.random());
        }

        if (!this.user.hasOpinionAbout(op.getTopic())){
            this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),Math.random()));
        }


            double Iab = this.user.getInfluenceDegree(op.getUser());
            double OA = op.getOx();
            double OB = this.user.getOpinion(op.getTopic()).getOx();
            double newOB = OB + (OA - OB ) * Iab;
            this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),newOB));
            System.out.println("your receive op :"+this.user.getOpinion(op.getTopic()).toString());
            this.lock.unlock();
        }


    @Override
    public synchronized void propose(Topic t){
        this.lock.lock();

        Scanner sc = new Scanner(System.in);
        System.out.println("What do think about ? : "+ t.getIdTopic());
        double op = Double.parseDouble(sc.nextLine());
        this.user.addOpinion(new OpinionTopic(this.user,t,op));
        System.out.printf("Your opinion %.2f on %s is saved ! ",this.user.getOpinion(t).getOx(),t.getIdTopic());
        this.lock.unlock();
    }

    @Override
    public synchronized void displayMessage(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public void addOpinion(OpinionTopic op) throws RemoteException{
        this.user.getOpinions().put(op.getTopic().getIdTopic(), op);
    }

    public double requestProof(){
        this.lock.lock();
        java.io.Console console = System.console();
        double proof = Double.parseDouble(console.readLine("the proof : "));
        this.lock.unlock();
        return proof;
    }

    public double answer_the_call() throws RemoteException{
        this.lock.lock();
        Scanner scanner = new Scanner(System.in);
        System.err.print("Are you going to answer this call? [Choose a number between 0 and 1]: ");
        double answer_call = Double.parseDouble(scanner.nextLine());
        this.lock.unlock();
        return answer_call;

    }

    public int accepts_communication(String username) throws RemoteException {
        lock.lock();
        Scanner scanner = new Scanner(System.in);
        System.err.print(this.user.getUsername() + ", Would you like to chat with " + username + "?" + "[0 to refuse or 1 to accept ] :");
        int answer_call = (int) Double.parseDouble(scanner.nextLine());
        this.lock.unlock();
        return answer_call;
    }

    public void setUser(User user) throws RemoteException{
        this.user = user;
    }
}

