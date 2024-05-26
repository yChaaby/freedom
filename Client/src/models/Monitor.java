package models;

import services.ClientMonitor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable ;
import java.util.Scanner;

public class Monitor extends UnicastRemoteObject implements ClientMonitor, Serializable {
    private final Object lock = new Object();
    private User user;
    public Monitor(User user) throws RemoteException{
    this.user=user;
    }
    public Monitor() throws RemoteException {}

    public User getUser() {
        return user;
    }

    @Override
    public synchronized void sendOpinion(OpinionTopic op, ClientMonitor clientMonitor) throws RemoteException {
       if(this.user.isFirstInteraction(op.getUser())){
           this.user.addInfluenceDegree(op.getUser().getUsername(),Math.random());
       }

       if (!this.user.hasOpinionAbout(op.getTopic())){
           this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),Math.random()));
       }

       //  am I a CRITICAL_THINKER ?
        if(this.user.getUserType() == UserType.CRITICAL_THINKER)
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
        System.out.println("Proof accepted :)");
       double Iab = this.user.getInfluenceDegree(op.getUser());
       double OA = op.getOx();
       double OB = this.user.getOpinion(op.getTopic()).getOx();
       double newOB = OB + (OA - OB ) * Iab;
       this.user.addOpinion(new OpinionTopic(this.user,op.getTopic(),newOB));
       this.user.displayOpinions();
    }


    public void setUser(User user) throws RemoteException{
        this.user = user;
    }

    @Override
    public synchronized void propose(Topic t)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("What do think about ? : "+ t.getIdTopic());
        double op = Double.parseDouble(sc.nextLine());
        this.user.addOpinion(new OpinionTopic(this.user,t,op));
        System.out.printf("Your opinion %.2f on %s is saved ! ",this.user.getOpinion(t).getOx(),t.getIdTopic());
    }

    @Override
    public synchronized void displayMessage(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public double requestProof(){
        Scanner scanner = new Scanner(System.in);
        System.err.print("What's your proof? : ");
        double proof = Double.parseDouble(scanner.nextLine());
        return proof;
    }
}