package RMI.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIService extends UnicastRemoteObject  implements RemoteService {

    private static final long serialVersionUID = 1L;

    protected RMIService() throws RemoteException {
//        UnicastRemoteObject.exportObject(userHandler,REGISTRY_PORT);
        super();
        // TODO Auto-generated constructor stub
    }

//    @Override
//    public String run() throws RemoteException {
//        System.out.println("invoke my service.");
//
//        return "success";
//    }
//
//    @Override
//    public String run(String name) throws RemoteException{
//        System.out.println("invoke remote service.");
//        Student s=new Student();
//        return s.inquiry(name);
//    }

    @Override
    public String inquiry(String name) throws RemoteException {
        return null;
    }
}
