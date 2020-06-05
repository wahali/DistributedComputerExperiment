package Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class reflection {
        public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
            Class<?> cl = Class.forName("Reflection.ReflectionClass.Rectangle");
            Object obj = cl.newInstance();
            Method[] md1 = cl.getMethods();
//            for (Method m:
//             md1) {
//            System.out.println(m);
//        }
            Method md = cl.getMethod("area",double.class,double.class);
            Method md2 = cl.getMethod("hi");
//            Rectangle rectangle = new Rectangle();
            System.out.println(md.invoke(obj,3.1,3.1));

            //对应于私有方法
//            Method privateMethod = cl.getDeclaredMethod();
//            privateMethod.setAccessible(true);
//    Constructor[] constructors = cl.getConstructors();
//    cl.get
    }
}



