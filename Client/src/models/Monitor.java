package models;
import services.ClientMonitor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable ;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor extends UnicastRemoteObject implements ClientMonitor, Serializable {
    private User user;
    private final Lock lock = new ReentrantLock();
    public Monitor(User user) throws RemoteException{
    this.user=user;
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
    public synchronized void sendOpinion(OpinionTopic op) throws RemoteException {
       this.lock.lock();
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



    public void setUser(User user) throws RemoteException{
        this.user = user;
    }
    @Override
    public synchronized void propose(Topic t){
        this.lock.lock();
        Scanner sc = new Scanner(System.in);
        System.out.println("What do think about ? : "+t.getIdTopic());
        double op = Double.parseDouble(sc.nextLine());
        this.user.addOpinion(new OpinionTopic(this.user,t,op));
        System.out.printf("Your opinion %.2f on %s is saved ! ",this.user.getOpinion(t).getOx(),t.getIdTopic());
        this.lock.unlock();
    }

    @Override
    public synchronized void displayMessage(String msg) throws RemoteException {
        System.out.println(msg);
    }

}