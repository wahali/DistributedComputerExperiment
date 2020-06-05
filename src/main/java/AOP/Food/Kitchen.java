package AOP.Food;

import java.lang.reflect.InvocationTargetException;

public class Kitchen {

    public static Food getFood(String foodName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

//根据参数foodName返回相应对象

//例如，foodName为”Salad“，返回一个Salad对象，foodName为“Beef”，返回一个Beef对象。

//采用反射，不得出现任何if等分支判断语句，不得修改本方法的签名signature
        Class<?> food = Class.forName("Dependency.Food."+foodName);
//        Constructor<?> constructor = food.getConstructor();
////        constructor.setAccessible(true);
        return (Food)food.newInstance();
    }

}