package AOP.Food;

import Proxy.Division;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    Object obj;

    public ProxyHandler(Division division){
        this.obj = division;
    }
    // invoke 返回值结果类型应该对应真正被调用的方法的返回值，

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(obj,args);
        return null;
    }
}
