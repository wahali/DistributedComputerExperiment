package aop1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Scanner;

/**
 * Test
 */
public class TestRestaurant {

    public static void main(String[] args){

        
        InvocationHandler handler = new ProxyHandler(new Restaurant());

        RestaurantInterface restaurant=(RestaurantInterface)Proxy.newProxyInstance
        (Restaurant.class.getClassLoader(), Restaurant.class.getInterfaces(), handler);


        //Restaurant restaurant=new Restaurant();        
        System.out.println("欢迎光临餐厅");      
        Scanner in = new Scanner(System.in);
        String food = in.nextLine();
        in.close();        
        restaurant.order(food);         
        System.out.println("谢谢光临，再见");        
        
    }
}