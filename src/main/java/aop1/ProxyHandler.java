package aop1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class ProxyHandler implements InvocationHandler {
	
	static String beforeMethod = "";
	static String afterMethod = "";
	
	private RestaurantInterface object;
	
	public ProxyHandler(RestaurantInterface object){
		this.object = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		XmlReader.readXml("aops.xml");
		Class<?> clazz = MyAspect.class;
		//处理before方法
		if(beforeMethod!=null&&beforeMethod.length()>0){
			Method m=clazz.getMethod(beforeMethod);
			Object obj = clazz.newInstance();
			m.invoke(obj);
		}
		
		//处理目标方法
		Object result = method.invoke(object, args);
		
		//处理after方法
		if(afterMethod!=null&&afterMethod.length()>0){
			Method m=clazz.getMethod(afterMethod);
			Object obj = clazz.newInstance();
			m.invoke(obj);
		}
		return result;
	}

}
