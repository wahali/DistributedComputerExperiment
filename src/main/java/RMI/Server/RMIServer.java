package RMI.Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 */
public class RMIServer {
    //设定远程服务器注册中心的网络端口和ip地址
    private static  final int REGISTRY_PORT = 9909;
    private static  final String REMOTE_HOST = "localhost";
    private static  final String SERVICE_URL = "rmi://" + REMOTE_HOST + ":" + REGISTRY_PORT;
    private static  final String SERVICE_NAME = "user";
    public static void main(String[] args) throws RemoteException {

        RemoteService userHandler = null;
        
        try {           
            LocateRegistry.createRegistry(REGISTRY_PORT);
            userHandler = new Student();
            UnicastRemoteObject.exportObject(userHandler,REGISTRY_PORT);
            Naming.rebind(SERVICE_URL + "/" + SERVICE_NAME, userHandler);
            System.out.println("rmi server is ready ...");            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




//UnicastRemoteObject.exportObject(userHandler,REGISTRY_PORT);
//Naming.rebind(SERVIC_STRING+"/user", userHandler);