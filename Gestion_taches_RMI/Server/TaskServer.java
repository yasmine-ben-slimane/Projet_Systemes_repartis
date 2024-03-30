import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TaskServer {
    public static void main(String[] args) {
        try {
            // creer une interface de la fabrique
            TaskServiceFactoryInterface taskServiceFactory = new TaskServiceImplFactory();

            // creer un registre RMI sur le port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // lier la fabrique au rmi registry
            registry.rebind("TaskServiceFactory", taskServiceFactory);
            System.out.println("serveur en ecoute ...");
        } catch (Exception e) {

            System.err.println("erreur du serveur : " + e.toString());
            e.printStackTrace();
        }
    }
}
