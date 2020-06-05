package aop1;



/**
 * Kitchen
 */
public class Kitchen {

    final static String CLASS_PATH="test.dc.aop.";

    public static Food getFood(String foodName)
             {
        //根据参数foodName返回相应对象
        //例如，foodName为”Salad“，返回一个Salad对象，foodName为“Beef”，返回一个Beef对象。
        //采用反射，不得出现任何if等分支判断语句，不得修改本方法的签名signature

        try {
            Class<?> clazz=Class.forName(CLASS_PATH+foodName);//full path class name
             return (Food)clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return null;
        
    }
    
}