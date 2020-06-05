package RMI.Server;


import java.rmi.*;

public interface RemoteService extends Remote {
//    String run() throws RemoteException;
    String inquiry(String name) throws RemoteException;
}
