package Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Scanner;

/**

 *

 在不修改Division类的定义前提下，请用动态代理的方式编写一个代理程序进行除法计算。要求



 1、编写一个动态代理处理器类，扩展除法功能如下：
 a.在除法计算前，可以检验除数是否为0，如果为0，则计算结果为-9999，并输出错误提示：除零错误！
 b.在除法计算后，可以检查是否有余数，如果有余数，则输出余数提示：余数为xxx
 2、编写一个测试类，从键盘输入两个数，生成动态代理对象，通过代理对象进行除法计算，并输出最终结果。
 3、编写其他可能需要的接口。
 4、将动态代理、测试类、相应接口的代码直接拷贝到答题文本框中去。不要上传附件。

 */

public class Test {
    public static void main(String[] args){
        System.out.println("该应用进行除法运算，请分别输出两个整数以空格隔开。");
        Scanner in = new Scanner(System.in);

        int a,b;
        a = in.nextInt();
        b = in.nextInt();
        InvocationHandler handler = new ProxyHandler(new Division());
        Operation operation =  (Operation) Proxy.newProxyInstance(Division.class.getClassLoader(),Division.class.getInterfaces(),handler);
        operation.divide(a,b);
    }
}
