package Proxy;

interface Operation{
    public int divide(int a,int b);
}

public class Division implements Operation{

    @Override
    public int divide(int a, int b){



        int result=-1;

        try{

            System.out.println("开始计算.....");

            result=a/b;

            System.out.println("计算完毕.....");

            return result;

        }catch(Exception e){ }

        return result;

    }

}