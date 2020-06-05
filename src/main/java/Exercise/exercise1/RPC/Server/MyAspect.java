package Exercise.exercise1.RPC.Server;

import Exercise.exercise1.PasswordEncryption.PasswordEncryption;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;


public class MyAspect  {
    @Override

    public String toString() {
        return "MyAspect{}";
    }
    //    private int time = 0;
//    public void checkErrorTimes(){
//        System.out.println("测试");
//    }
    // 登陆函数前期未考虑带锁情况，所以返回值只有成功或着失败，在checkErrorTimes时还是需要再次
    // 登录函数之后，判断是否错误三次，错误三次会返回封锁结果，更新登陆状态

    /**
     *
     * @param id
     * @param password
     * @param loginRes login的状态返回值
     * @return
     */
    public void checkErrorTimes(String id,String password,String loginRes){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                databaseConnector.setSql("select state,errorTimes from user where id = ?");
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setString(1,id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
                //获取状态值
                String state = "";
                String salt = "";
                String realpd = "";
                int errorTimes = 0;
                if(rs.next()){
                    state = rs.getString("state");
                    errorTimes = rs.getInt("errorTimes");
                }
                if("locked".equals(state)){
//                    return "locked";
                }
                if("succeed".equals(loginRes)){
                    // 登录成功修改状态
                    System.out.println("succeed");
                    databaseConnector.setSql("update user set state = ? , errorTimes = ? where id = ?");
                    preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                    preparedStatement.setString(1,"active");
                    preparedStatement.setInt(2,0);
                    preparedStatement.setString(3,id);
                    preparedStatement.execute();
//                    return "";
                }
                else {
                    System.out.println("failed");
                    errorTimes ++;
                    if(errorTimes >= 3){
                        databaseConnector.setSql("update user set state = ? , errorTimes = ? where id = ?");
                        preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                        preparedStatement.setString(1,"locked");
                        preparedStatement.setInt(2,errorTimes);
                        preparedStatement.setString(3,id);
                        preparedStatement.execute();
//                        return "locked";
                    }
                    else {
                        databaseConnector.setSql("update user set state = ? , errorTimes = ? where id = ?");
                        preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                        preparedStatement.setString(1, "active");
                        preparedStatement.setInt(2, errorTimes);
                        preparedStatement.setString(3,id);
                        preparedStatement.execute();
                    }

                }

                databaseConnector.getConn().close();     // Close the database
            }
            databaseConnector.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
//        System.out.println("error");
//        return "";
    }

    public void withdrawLog(String id, BigDecimal num,String withdrawRes){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                databaseConnector.setSql("select deposit,state,errorTimes from user where id = ?");
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setString(1,id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
                //获取状态值
                String state = "";
                String salt = "";
                String realpd = "";
                BigDecimal deposit = new BigDecimal(0);
                int errorTimes = 0;
                if(rs.next()){
//                    state = rs.getString("state");
//                    errorTimes = rs.getInt("errorTimes");
                    deposit = rs.getBigDecimal("deposit");
                }
                // 取款成功
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if("succeed".equals(withdrawRes)){
                    System.out.println("succeed");
                    databaseConnector.setSql("insert into transaction values (?,?,?,?,?,?)");
                    preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                    preparedStatement.setString(1,id);
                    preparedStatement.setTimestamp(2,timestamp);
                    preparedStatement.setBigDecimal(3,deposit.add(num));
                    preparedStatement.setBigDecimal(4,deposit);
                    preparedStatement.setBigDecimal(5,num);
                    preparedStatement.setString(6,withdrawRes);
                    preparedStatement.execute();
//                    return "locked";
                } //余额不足了或着出现error了
                else {
                    System.out.println("failed");
                    databaseConnector.setSql("insert into transaction values (?,?,?,?,?,?)");
                    preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                    preparedStatement.setString(1,id);
                    preparedStatement.setTimestamp(2,timestamp);
                    preparedStatement.setBigDecimal(3,deposit);
                    preparedStatement.setBigDecimal(4,deposit);
                    preparedStatement.setBigDecimal(5,num);
                    preparedStatement.setString(6,withdrawRes);
                    preparedStatement.execute();
                }

                databaseConnector.getConn().close();     // Close the database
            }
            databaseConnector.close();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("error");
        return ;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> clazz = MyAspect.class;
        Method m=clazz.getMethod("checkErrorTimes",String.class, String.class);
        System.out.println("after方法的参数列表 " + Arrays.toString(m.getParameterTypes()));
        Object obj = clazz.newInstance();
        Object [] arg = new Object[]{"123456","1234"};
        Object tem = m.invoke(obj,arg);
        System.out.println(tem);
    }


}
