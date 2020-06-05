package Exercise.exercise1.RPC.Server;;


import java.lang.reflect.*;
import java.util.Arrays;

/**
 *
 */
// 动态代理类
public class DynamicProxyFactory {
    public static String beforeMethod = "";
    public static String afterMethod = "";
    public static Class<?>[] beforeClass = null;
    public static Class<?>[] afterClass = null;

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class<T> classType) {
        //AOP 函数不影响被切入的函数，执行类似于日志等功能，且AOP函数使用远程调用
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
                AopXmlReader.readXml("src\\main\\java\\Exercise\\exercise1\\RPC\\Server\\Aop.xml",method.getName());
                System.out.println("beforeMethod  "+ beforeMethod);
                System.out.println("afterMethod  "+ afterMethod);
//                XmlReader.readXml("aops.xml");
                Class<?> clazz = MyAspect.class;
                //处理before方法
                if(beforeMethod!=null&&beforeMethod.length()>0){
                    Method m=clazz.getMethod(beforeMethod);
                    Object obj = clazz.newInstance();
                    m.invoke(obj);
                }
                //处理目标方法
                // 真正被代理的对象都在CardImpl，在此处直接赋值了
                Object result = method.invoke(CardImpl.class.newInstance(), args);
                //处理after方法
                if(afterMethod!=null&&afterMethod.length()>0){
//                    System.out.println("进入afte方法了");
//                    System.out.println("切入点的返回值 " + result);
                    Method m = null;
                    Object [] newarg = new Object[args.length+1];
                    for(int i  = 0;i < args.length;++i){
                        newarg[i] = args[i];
                    }
                    newarg[args.length] = result;
//                    if(tem !=null) {
                        m=clazz.getMethod(afterMethod,afterClass);
//                    } else {
//                        m=clazz.getMethod(afterMethod);
//                    }
//                    System.out.println("after方法的参数列表 " + Arrays.toString(m.getParameterTypes()));
                    Object obj = clazz.newInstance();
                    Object temp = null;
                    if(afterClass !=null) {
                        temp = m.invoke(obj,newarg);
                    }
                    else{
                        temp = m.invoke(obj);
                    }
//                    System.out.println("after方法的返回值 "+temp);
                    if(temp != null) {
                        result = temp;
                    }
                }
                beforeMethod = "";
                afterMethod = "";
                beforeClass = null;
                afterClass = null;
                System.out.println("代理的最终返回值为 "+ result);
                return result;
            }
        };

        System.out.println("代理开始执行");
        return (T)  Proxy.newProxyInstance(classType.getClassLoader(),new Class<?>[]{classType}, handler);
    }
    }

