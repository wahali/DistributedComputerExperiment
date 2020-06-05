package RPCLearn;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private final static String CLASS_PATH="RPCLearn.";
    private final static String HOST = "localhost";
    private final static int PORT = 8000;

    public static void main(String[] args) throws Exception {

        RPCService service=new RPCServiceImpl();
        String result= service.request("你好！");
        System.out.println("本地执行结果为："+result);

         result=invoke("你好！");
         System.out.println("远程执行结果为："+result);

//        service=  DynamicProxyFactory.getProxy(RPCService.class, HOST, PORT);
//        result=service.request("你好！");
//        System.out.println("动态代理+网络封装方式的远程执行结果为："+result);
//
//        AnotherService anotherService=
//        DynamicProxyFactory.getProxy(AnotherService.class, HOST, PORT);
//        int intResult=anotherService.test(10);
//        System.out.println("result="+intResult);



    }


    public static int invoke(int a){


        return 0;
    }


    public static String invoke(String infomation) throws Exception {
        Socket socket = new Socket("localhost", PORT);
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        InputStream in = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(in);
        RemoteCall call = new RemoteCall(CLASS_PATH+"RPCService", "request", new Class[]{String.class}, new Object[]{infomation});// 向服务器发送Call 对象
        oos.writeObject(call);
        //接收包含了方法执行结果的Call 对象
        call = (RemoteCall) ois.readObject();
        ois.close();
        oos.close();
        socket.close();
        return (String)call.getResult();
    }



}
