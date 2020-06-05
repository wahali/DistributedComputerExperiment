package Exercise.exercise1.RPC.Client;
import java.lang.reflect.*;

/**
 *
 */
// 动态代理类
public class DynamicProxyFactoryClient {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class<T> classType, final String host, final int port) {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object args[]) throws Exception {
                Connector connector = null;
                try {
                    connector = new Connector(host, port);
                    RemoteCall call = new RemoteCall(classType.getName(), method.getName(), method.getParameterTypes(), args);
                    connector.send(call);
                    call = (RemoteCall) connector.receive();
                    Object result = call.getResult();
                    return result;
                } finally {
                    if (connector != null) {
                        connector.close();
                    }

                }

            }
        };

//        System.out.println("代理开始执行");
        return (T)  Proxy.newProxyInstance(classType.getClassLoader(),new Class<?>[]{classType}, handler);
    }
}

