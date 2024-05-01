package models;

import services.ClientMonitor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable ;

public class Monitor extends UnicastRemoteObject implements ClientMonitor, Serializable {
    private User user;
    public Monitor(User user) throws RemoteException{
    this.user=user;
    }
    public Monitor() throws RemoteException {}

    public User getUser() {
        return user;
    }

    @Override
    public void sendOpinion(OpinionTopic op) throws RemoteException {
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
       this.user.displayOpinions();
    }


    public void setUser(User user) throws RemoteException{
        this.user = user;
    }

    @Override
    public void displayMessage(String msg) throws RemoteException {
        System.out.println(msg);
    }

}