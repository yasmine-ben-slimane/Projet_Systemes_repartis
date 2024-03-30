import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl extends UnicastRemoteObject implements TaskServiceInterface {
    private List<String> taches;

    protected TaskServiceImpl() throws RemoteException {
        super();
        taches = new ArrayList<>();
    }



    // le mot-clé synchronized est utilisé pour assurer qu'un seul thread accède aux méthodes ci-dessous
    @Override
    public synchronized void addTask(String tache) throws RemoteException {
        taches.add(tache);
        System.out.println("tâche ajoutée : " + tache);
    }

    @Override
    public synchronized void removeTask(int index) throws RemoteException {
        if (index >= 0 && index < taches.size()) {
            String tacheSupprimee = taches.remove(index);
            System.out.println("tache supprimee : " + tacheSupprimee);
        } else {
            System.out.println("index invalide");
        }
    }

    @Override
    public synchronized List<String> getAllTasks() throws RemoteException {
        return taches;
    }
}

