import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskServiceFactoryInterface extends Remote {
    TaskServiceInterface createTaskService() throws RemoteException;
}
