package aop1;
/**
 * Food
 */
public interface Food {

    void eat();
}

class Beef implements Food{
  
    @Override
    public void eat() {
        // TODO Auto-generated method stub
        System.out.println("吃牛排。。。");
    }
}
class Salad implements Food{

    @Override
    public void eat() {
        // TODO Auto-generated method stub
        System.out.println("吃沙拉。。。");
    }    
}
class Veges{
    
}