package Exercise.exercise1.RPC.Server;


import Exercise.exercise1.RPC.Client.RemoteCall;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.*;
import java.util.*;


public class Server {
    private final static String CLASS_PATH="Exercise.exercise1.RPC.";
    private final static String SERVICE_NAME = "Card";
    private  static int PORT = 8000;

    public static String getClassPath() {
        return CLASS_PATH;
    }

    public static String getServiceName() {
        return SERVICE_NAME;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        Server.PORT = PORT;
    }

    public Map<String, Object> getRemoteObjects() {
        return remoteObjects;
    }

    public void setRemoteObjects(Map<String, Object> remoteObjects) {
        this.remoteObjects = remoteObjects;
    }

    // 存放远程对象的缓存
    private Map<String,Object> remoteObjects = new HashMap<>() ;
    //注册服务：把一个远程对象放到缓存中
    public void register( String className,Object remoteObject) {

        remoteObjects.put( className,remoteObject) ;
         
    }

    public Server(){}

    public Server(int port){
        PORT = port;
    }
    
// 暴露服务，创建基于流的Socket,并在8000 端口监听

    public void exportService() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println(" 服务器启动......");
        //TODO 服务器端使用线程池，进一步优化多线程
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept();
                //每接受一个请求 就创建一个新的线程 负责处理该请求
                new Thread(new SocketHandler(socket)).start();
            }catch (IOException e){
                e.printStackTrace();
            }
//            finally {
//                try{
//                    if(socket!=null)socket.close();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//
//            }


        }
//        serverSocket.close();
    }

public RemoteCall invoke(RemoteCall call) {
    Object result = null;
    try {
        String className = call.getClassName();
        String methodName = call.getMethodName();
        Object[] params = call.getParams();
        Class<?> classType = Class.forName(className);
        Class<?>[] paramTypes = call.getParamTypes();
        Method method = classType.getMethod(methodName, paramTypes);
        // 从hashmap缓存中取出相关的远程对象Object
        Object remoteObject =remoteObjects.get(className);
        if (remoteObject == null) {
            throw new Exception(className + " 的远程对象不存在");
        } else {
            result = method.invoke(remoteObject, params);
            System.out.println("远程调用结束:remotObject:"+remoteObject.toString()+",params:"+ Arrays.toString(params));
        }
    } catch (Exception e) {
        System.out.println("错误："+e.getMessage());
    }
    call.setResult(result);
    
    return call;
}

/**
     * 为远程调用服务创建线程对象
     */
    public class SocketHandler implements Runnable {
        Socket socket;
        public SocketHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try{
                InputStream in = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(in);
                OutputStream out = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out);
                //接收客户发送的Call 对象
                RemoteCall remotecallobj = (RemoteCall) ois.readObject();

                System.out.println(remotecallobj);
                System.out.println("当前线程的socket为"+ socket);
                // 调用相关对象的方法
                System.out.println("calling......");
                remotecallobj = invoke(remotecallobj);
                // 向客户发送包含了执行结果的remotecallobj 对象
                oos.writeObject(remotecallobj);
                ois.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try{
                    if(socket!=null) {
                        socket.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

        }
    }
    public void start() throws Exception {
        register(CLASS_PATH+SERVICE_NAME,new CardImpl());
        exportService();
    }
    public static void main( String args[ ]) throws Exception {
        Server server = new Server() ;
        //把事先创建的RemoteServceImpl 对象加人到服务器的缓存中
        //在服务注册中心注册服务
        server.register(CLASS_PATH+SERVICE_NAME,new CardImpl());
//        server.register("Exercise.exercise1.RPC.RemoteAop",new MyRemoteAop());
//        server.register(CLASS_PATH+"AnotherService",new AnotherServiceImpl());
        server.exportService();//打开网络端口，接受外部请求，执行服务功能，返回结果

    }



}