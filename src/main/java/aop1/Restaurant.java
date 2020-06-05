package aop1;



public class Restaurant implements RestaurantInterface {

    Food food;
    @Override
    public void setFood(Food f){
        food=f;
    }

    @Override
    public void order(String foodName) {
        System.out.println("客人点了一份"+foodName);
        //b=new Beef();
        
        setFood( Kitchen.getFood(foodName));
        System.out.println("上菜。。。。");       
        food.eat();              
    } 
    
     
    
    
}