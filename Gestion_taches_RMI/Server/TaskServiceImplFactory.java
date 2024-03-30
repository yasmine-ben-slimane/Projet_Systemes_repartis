import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TaskServiceImplFactory extends UnicastRemoteObject implements TaskServiceFactoryInterface {
    public TaskServiceImplFactory() throws RemoteException {
        super();
    }

    @Override
    public TaskServiceInterface createTaskService() throws RemoteException {
        return new TaskServiceImpl();
    }
}
