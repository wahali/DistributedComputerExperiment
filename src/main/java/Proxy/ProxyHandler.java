package Proxy;

import javafx.beans.binding.ObjectExpression;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    Object obj;

    public  ProxyHandler(Division division){
        this.obj = division;
    }
    // invoke 返回值结果类型应该对应真正被调用的方法的返回值，
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int a = (Integer)args[0],b = (Integer)args[1];
        if(b == 0){
            System.out.println("除零错误！");
            return 0;
        }
        int res;
        res = (int) method.invoke(obj,args);
        System.out.println("结果为"+res);
        if(a%b!=0){
            int remainder = a % b;
            System.out.println("余数为" + remainder);
        }
        return res;
    }
}
