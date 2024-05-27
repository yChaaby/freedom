package models;
import services.ClientMonitor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable ;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor extends UnicastRemoteObject implements ClientMonitor, Serializable {

    private User user;
    public final Lock lock = new ReentrantLock();
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
        System.out.println("je suis la");
        this.lock.unlock();
        System.out.println("je enenen");
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
            System.out.println(username+" is following you ;-)");
        }
        lock.unlock();
    }
    @Override

    public synchronized void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException {
        this.lock.lock();

       if(this.user.isFirstInteraction(op.getUser())){
           this.user.addInfluenceDegree(op.getUser().getUsername(),Math.random());
       }

       if (!this.user.hasOpinionAbout(op.getTopic())){
           this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),Math.random()));
       }

       //  am I a CRITICAL_THINKER ?
        if(this.user.getUserType() == UserType.CRITICAL_THINKER && ((op.getUser().getUserType() != UserType.INFLUENCER) || (op.getUser().getUserType() != UserType.PROPOSER)))
        {
            this.user.displayOpinions();
            System.out.println("A proof? : ");
            double proof = clientMonitor.requestProof();
            System.out.println("Proof requested: " + proof);
            if(proof < 0.70)
            {
                System.out.println("Proof not sufficient. Opinion rejected !");
                this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),this.user.getOpinion(op.getTopic()).getOx()));
                this.user.displayOpinions();
                return;
            }
        }
        if(this.user.getUserType() == UserType.CRITICAL_THINKER)
            System.out.println("Proof accepted :)");
        if(this.user.getUserType() == UserType.CRITICAL_THINKER && ((op.getUser().getUserType() != UserType.INFLUENCER) || (op.getUser().getUserType() != UserType.PROPOSER)))
        {
            double Iab = this.user.getInfluenceDegree(op.getUser());
            double OA = op.getOx();
            double OB = this.user.getOpinion(op.getTopic()).getOx();
            double newOB = OB + (OA - OB ) * Iab;
            this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),newOB));
            System.out.println("your receive op :"+this.user.getOpinion(op.getTopic()).toString());
        }
       this.lock.unlock();
    }
    public void setUser(User user) throws RemoteException{
        this.user = user;
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
        Scanner scanner = new Scanner(System.in);
        System.err.print("What's your proof? : ");
        return Double.parseDouble(scanner.nextLine());
    }

    public double answer_the_call() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        System.err.print("Are you going to answer this call? [Choose a number between 0 and 1]: ");
        return Double.parseDouble(scanner.nextLine());

    }

    public int accepts_communication(String username) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.err.print(this.user.getUsername() + ", Would you like to chat with " + username + "?" + "[0 to refuse or 1 to accept ] :");
        return (int) Double.parseDouble(scanner.nextLine());
    }
}