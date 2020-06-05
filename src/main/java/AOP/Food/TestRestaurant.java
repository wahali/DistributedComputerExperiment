package AOP.Food;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

/**
 * 下面程序违反了依赖倒置的原则，请按IOC的设计模式进行如下修改：
 *
 *
 *
 * 1、按DIP的原则修改Restaurant类，使得该类可以接受setter方式的依赖注入。
 *
 * 2、修改Kitchen类，实现一个IoC容器，该容器可以根据字符串参数返回一个依赖对象。
 *
 * 3、修改TestRestaurant类的main方法，可以根据键盘输入的将IoC容器Kitchen生成的对象注入到Restaurant，并实现点餐进食
 *
 * 4、将Restaurant、TestRestaurant、Kitchen类的源代码直接复制到答案框中即可。
 *
 *
 *
 * 要求：
 *
 * 1、不得采用任何if等分支判断语句
 *
 * 2、修改完成后，可以根据键盘输入的任意点单的菜名进行点餐下单
 *
 * 3、如饭店有新菜，只需要新建一个类实现Food接口即可，程序其他部分不需要改动
 *
 * 4、为简单起见，暂不考虑键盘输入饭店不存在的菜名的特殊情况
 */

public class TestRestaurant {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {

        Restaurant restaurant=new Restaurant();

        System.out.println("欢迎光临餐厅");

        Scanner in = new Scanner(System.in);

        String food = in.nextLine();

        in.close();

        restaurant.order(food);

        System.out.println("谢谢光临，再见");

    }

}

