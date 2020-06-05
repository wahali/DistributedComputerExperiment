package AOP.Food;

import java.lang.reflect.InvocationTargetException;

public class Restaurant {



//    Beef b;
    Food f;

    public void order(String foodName) throws IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        System.out.println("客人点了一份"+foodName);

        setFood(foodName);

        System.out.println("上菜。。。。");

        f.eat();

    }
    public void setFood(String foodName) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
        Kitchen kitchen = new Kitchen();
        f = Kitchen.getFood(foodName);
    }


}