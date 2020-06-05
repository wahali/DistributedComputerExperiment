package Exercise.exercise1.RPC.Client;

import Exercise.exercise1.RPC.Card;
import Exercise.exercise1.RPC.Server.DatabaseConnector;

import java.math.BigDecimal;
import java.util.*;

public class Client {
        private final static String CLASS_PATH="Exercise.exercise1.RPC.";
//        private final static String HOST = "localhost";
//        private final static int PORT = 8000;

        private String state = "notLoggedIn";
        private DatabaseConnector databaseConnector;
        private Host host = null;

        static Scanner sc =new Scanner(System.in);
        public static String id = ""; // 未登录时记录之前一次登录时的id，登陆后记录当前登录的id
        public static int failedTime = 0;  //记录之前的登录错误次数
        static Vector<Host> activeHost = null;
        static int host_index = 0;

    // 最开始进入服务时，通过配置文件，加载可用的远程主机的信息
    public void loadHost(){
        String filePath = "src/main/java/Exercise/exercise1/RPC/Client/Host.xml";
        activeHost = HostXmlReader.readXml(filePath);
    }
    //在每次进行远程调用之前先选择当前使用的远程主机
    public  void chooseHost(){
        int sz = activeHost.size();
        //随机选择一个远程主机
        host_index = (int) (Math.random() * sz);
        host = activeHost.get(host_index);
    }
    //判断当前是否还有可以使用的远程主机
    public boolean check_host(){
        if(activeHost.size() == 0){
            System.out.println("当前无远程主机可提供服务，请检查配置文件Host.xml");
            return false;
        }
        return true;
    }

    // 当前的远程主机连接不上或者无法提供服务，在本地的主机序列中除去该远程主机
    public void cancel_host(){
        if(activeHost.size()>0){
            System.out.println("主机ip为" + host.getIp() + "对应端口为" +  host.getPort() + "的远程主机暂时无法提供服务！");
            activeHost.remove(host_index);
            host_index = 0;
            host = null;
        }

    }
    public void  loginInterface(){
        loadHost();
        while(true){
            System.out.println("欢迎是使用该ATM！！");
            System.out.println("您当前还没有登陆");
            System.out.println("请输入账号密码进行登陆!!");
//        Console cons = System.console();
            System.out.print("账号：");
            String id = sc.next();
//        String id = cons.readLine();
            System.out.print("密码：");
//        char[] pd = cons.readPassword();
//        String password = Arrays.toString(pd);
            String password = sc.next();
            String state = login(id,password);
            if("succeed".equals(state)){
                System.out.println("登录成功");
                mainInterface();
            }
            else if("failed".equals(state)){
                System.out.println("登录失败");
            }
            else if("locked".equals(state)){
                System.out.println("登录失败");
                System.out.println("连续输错三次密码后账号封锁");
            }
            else if("error".equals(state)){
                System.out.println("登录出错");
            }
        }

    }

    public void mainInterface(){
        while(true){
            System.out.println("欢迎进入主界面!");
            System.out.println("功能列表如下：");
            System.out.println("1、查询余额");
            System.out.println("2、取钱");
            System.out.println("3、存钱");
            System.out.println("4、退出");
            int order = sc.nextInt();
                if(order == 1){
                    checkBalance();
                }
                else if(order == 2){
                    withdraw();
                }
                else if(order == 3){
                    save();
                }
                else if(order == 4){
                    break;
                }
                else{
                    System.out.println("输入不正确。");
                }

        }
        sc.close();
        exit();
    }

    public String login(String id,String password) {
        loadHost();
        String state = "failed";
        Object result = "";
        while(check_host()){
            chooseHost();
            state = "failed";
            Object [] args = new Object[]{id,password};
            Card service=  DynamicProxyFactoryClient.getProxy(Card.class, this.host.getIp(), this.host.getPort());
            try{
                result = service.loginProxy(id,password);
                state = "succeed";
                break;
            }catch (Exception e){
                //出现异常则将当前远程主机移出主机队列
                System.out.println("出现异常:" + e.toString());
                cancel_host();
            }
        }
        if("succeed".equals(state)){
            System.out.println("登录操作成功!");
            if("succeed".equals(result)){
                Client.id = id;
            }
            return (String) result;
        }
        else {
            System.out.println("登录操作失败");
            return "error";
        }

    }
    public void checkBalance(){
        loadHost();
        String state = "failed";
        String res = "";
        Object result = null;
        while(check_host()){
            chooseHost();
            Card service=  DynamicProxyFactoryClient.getProxy(Card.class, this.host.getIp(), this.host.getPort());
            try{
                result = service.checkBalance(id);
                state = "succeed";
                break;
            }catch (Exception e){
                //出现异常则将当前远程主机移出主机队列
                System.out.println("出现异常:" + e.toString());
                cancel_host();
            }
        }
        if("succeed".equals(state)){
            System.out.println("查询余额操作成功!");
            System.out.println("该账户余额为:"+result);
        }
        else {
            System.out.println("查询余额操作失败");
        }

    }

    public void save() {
        loadHost();
        System.out.println("请输入你的存款金额");
        System.out.println("请输入整数");
        BigDecimal bigDecimal = sc.nextBigDecimal();

        String state = "failed";
        String res = "";
        while(check_host()){
            chooseHost();
            Card service=  DynamicProxyFactoryClient.getProxy(Card.class, this.host.getIp(), this.host.getPort());
            try{
                Object result = service.save(id,bigDecimal);
                state = "succeed";
                break;
            }catch (Exception e){
                //出现异常则将当前远程主机移出主机队列
                System.out.println("出现异常:" + e.toString());
                cancel_host();
            }
        }
        if("succeed".equals(state)){
            System.out.println(" 存款操作成功!");
        }
        else {
            System.out.println("存款操作失败");
        }
    }
    public void withdraw(){
        loadHost();
        System.out.println("请输入你的取款金额");
        System.out.println("请输入整数");
        BigDecimal bigDecimal = sc.nextBigDecimal();
        String state = "failed";
        String res = "";
        Object result = null;
        while(check_host()){
            chooseHost();
            Card service=  DynamicProxyFactoryClient.getProxy(Card.class, this.host.getIp(), this.host.getPort());
            try{
                result = service.withdrawProxy(id,bigDecimal);
                state = "succeed";
                break;
            }catch (Exception e){
                //出现异常则将当前远程主机移出主机队列
                System.out.println("出现异常:" + e.toString());
                cancel_host();
            }
        }
        if("succeed".equals(state)){
            System.out.println(" 取款操作成功!");
            if("succeed".equals(result)){
                System.out.println("取款成功!");
            }else if("failed".equals(result)){
                System.out.println("取款失败，余额不足!");
            }
            else{
//                System.out.println("服务器出现问题，可以通知相关人员进行维修");
            }

        }
        else {
            System.out.println("取款操作失败");
        }
    }

    public void exit(){
        System.exit(1);
    }

//    // 远程调用服务器端的服务
//    public  Object call(String methodname,Object[]args) throws Exception {
//        Class<?> clazz = Card.class;
//        Class<?> cla = CardImpl.class;
////        System.out.println(Arrays.toString(cla.getMethods()));
//        Class<?>[] paramTypes = ;//表示方法参数类型
//        Method m = cla.getMethod(methodname);
//        Object res = new Object();
//        Connector connector = null;
//        connector = new Connector(host.getIp(), host.getPort());
//        RemoteCall call = new RemoteCall(clazz.getName(), m.getName(), m.getParameterTypes(), args);
//        connector.send(call);
//        call = (RemoteCall) connector.receive();
//        call.getResult();
//        connector.close();
//        return res;
//    }





    public static void main(String[] args) throws Exception {
        //TODO 客户端基本页面设计
        //TODO 客户端GUI设计
        Client client = new Client();
        client.loginInterface();
//        Card service=new CardImpl();
//        String result= service.request("你好！");
//        System.out.println("本地执行结果为："+result);
//        String id = "123456";
//        service.checkBalance(id);
//
//        BigDecimal bigDecimal = new BigDecimal("10.00");
//        service.save(id,bigDecimal);
//        service.checkBalance(id);
//        service.withdraw(id,bigDecimal);
//        service.checkBalance(id);

//         result=invoke("你好！");
//         System.out.println("远程执行结果为："+result);
//

        //TODO 服务器情况需要有所判断说明，异常情况需要有异常报错。
//        Card service=  DynamicProxyFactory.getProxy(Card.class, client.host.getIp(), client.host.getPort());
////        result=service.request("你好！");
//        String id = "123456";
//        // TODO 捕捉异常，更换新的主机进行远程调用
//        try{
//            BigDecimal bigDecimal = (BigDecimal) service.checkBalance(id);
//            System.out.println(bigDecimal);
//            System.out.println("动态代理+网络封装方式的远程执行结果为："+bigDecimal);
//            BigDecimal withdraw = new BigDecimal("10");
//            service.withdraw(id,withdraw);
//            bigDecimal = (BigDecimal) service.checkBalance(id);
//            System.out.println("动态代理+网络封装方式的远程执行结果为："+bigDecimal);
//        }catch (UndeclaredThrowableException e){
//            client.chooseHost();
//        }



//
//        AnotherService anotherService=
//        DynamicProxyFactory.getProxy(AnotherService.class, HOST, PORT);
//        int intResult=anotherService.test(10);
//        System.out.println("result="+intResult);



    }


//    public static int invoke(int a){
//
//
//        return 0;
//    }
//
//
//    public static String invoke(String infomation) throws Exception {
//        Socket socket = new Socket("localhost", PORT);
//        OutputStream out = socket.getOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(out);
//        InputStream in = socket.getInputStream();
//        ObjectInputStream ois = new ObjectInputStream(in);
//        RemoteCall call = new RemoteCall(CLASS_PATH+"RPCService", "request", new Class[]{String.class}, new Object[]{infomation});// 向服务器发送Call 对象
//        oos.writeObject(call);
//        //接收包含了方法执行结果的Call 对象
//        call = (RemoteCall) ois.readObject();
//        ois.close();
//        oos.close();
//        socket.close();
//        return (String)call.getResult();
//    }

   

}
