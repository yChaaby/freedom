import models.Topic;
import services.UserRemote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Polarimeter {
    private static final double ALPHA = 1.6;
    private static final double K = 1.0;
    private static final double[] INTERVAL = {0,1};
    private UserRemote stub;

    public static void main(String args[]) throws RemoteException, NotBoundException, InterruptedException {
        Polarimeter polarimeter = new Polarimeter();
        polarimeter.connect();
        java.io.Console console = System.console();
        String topic = console.readLine("Enter a topic to evaluate : ");
        while(true){
            System.out.println("----------------");
            polarimeter.printOpinionMatrix(polarimeter.createOpinionMatrix(polarimeter.getOpinionByTopics(topic)));
            System.out.println("La polarization est :"+ polarimeter.calculatePolarization(polarimeter.createOpinionMatrix(polarimeter.getOpinionByTopics(topic))));
            Thread.sleep(5000);
        }


    }
    public ArrayList<Double> getOpinionByTopics(String topic) throws RemoteException {

        ArrayList<Double> opinions = new ArrayList<>();
        opinions = (ArrayList<Double>) this.stub.getUsers().values().stream().filter(usermonitor-> {
            try {
                return (usermonitor.getUser().hasOpinionAbout(new Topic(topic)));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).map(clientMonitor -> {
            try {
                return (clientMonitor.getUser().getOpinion(new Topic(topic)).getOx());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        return opinions;
    }
    public ArrayList<Double>[] createOpinionMatrix(List<Double> opinions) {
        // Initialiser la matrice avec quatre sous-intervalles
        ArrayList<Double>[] matrix = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            matrix[i] = new ArrayList<>();
        }

        // Définir les bornes des sous-intervalles
        double[] bounds = {0.25, 0.5, 0.75, 1.0};

        // Placer les opinions dans les sous-intervalles appropriés
        for (Double opinion : opinions) {
            if (opinion <= bounds[0]) {
                matrix[0].add(opinion);
            } else if (opinion <= bounds[1]) {
                matrix[1].add(opinion);
            } else if (opinion <= bounds[2]) {
                matrix[2].add(opinion);
            } else {
                matrix[3].add(opinion);
            }
        }

        return matrix;
    }
    public void printOpinionMatrix(ArrayList<Double>[] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("Interval " + (i + 1) + ": " + matrix[i]);
        }
    }
    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",12345);
        this.stub = (UserRemote) registry.lookup("Server");

    }
    public double calculatePolarization(List<Double>[] opinionMatrix) {
        double[] midpoints = {0.125, 0.375, 0.625, 0.875};
        int[] counts = new int[4];

        // Calculer le nombre d'opinions dans chaque sous-intervalle
        for (int i = 0; i < opinionMatrix.length; i++) {
            counts[i] = opinionMatrix[i].size();
        }

        double polarization = 0.0;

        // Appliquer la formule de polarisation
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts.length; j++) {
                polarization += Math.pow(counts[i], 1 + ALPHA) * counts[j] * Math.abs(midpoints[i] - midpoints[j]);
            }
        }

        return K * polarization;
    }
    public void displayPolarization(String topic){

    }

}
