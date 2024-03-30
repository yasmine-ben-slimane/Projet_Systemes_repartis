import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class TaskClient {
    public static void main(String[] args) {
        try {
            // recuperer le registre RMI du serveur
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // recuperer la fabrique à partir du rmi registry
            TaskServiceFactoryInterface taskServiceFactory = (TaskServiceFactoryInterface) registry.lookup("TaskServiceFactory");

            // creer un service de gestion de taches en utilisant la fabrique
            TaskServiceInterface taskService = taskServiceFactory.createTaskService();
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("-----------------------------");
                System.out.println("Choisissez une opération :");
                System.out.println("1. Recupérer la liste des taches");
                System.out.println("2. Ajouter une nouvelle tache");
                System.out.println("3. Supprimer une tache existante");
                System.out.println("4. Quitter");
                System.out.println("-----------------------------");
                System.out.print("Entrez votre choix : ");
                int choix = scanner.nextInt();
                scanner.nextLine();
                switch (choix) {
                    case 1:
                        List<String> taches = taskService.getAllTasks();
                        System.out.println("liste des taches : " + taches);
                        break;
                    case 2:
                        System.out.print("Entrez la tache a ajouter : ");
                        String nouvelleTache = scanner.nextLine();
                        taskService.addTask(nouvelleTache);
                        break;
                    case 3:
                        System.out.print("Entrez l'index de la tache a supprimer (commence par 0) : ");
                        int indexASupprimer = scanner.nextInt();
                        taskService.removeTask(indexASupprimer);
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 4.");
                }
            }
            System.out.println("sortie...");
        } catch (Exception e) {
            // gère les exceptions
            System.err.println("erreur du client : " + e.toString());
            e.printStackTrace();
        }
    }
}
