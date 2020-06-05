package Exercise.exercise1.RPC.Server;

import Exercise.exercise1.PasswordEncryption.PasswordEncryption;
import Exercise.exercise1.RPC.Card;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * PeterPan666
 * 2020/4/15
 */
public class CardImpl implements Card {
    @Override
    public String toString() {
        return "CardImpl{}";
    }

    @Override
    public String request(String s) {
        return ("本request服务收到发来的信息："+s);
    }
    @Override
    public String login(String id,String password){
        DatabaseConnector databaseConnector = new DatabaseConnector();
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                databaseConnector.setSql("select salt,password,state from user where id = ?");
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setString(1,id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
                //获取状态值
                String state = "";
                if(rs.next()){
                    state = rs.getString("state");
                }
                if("locked".equals(state)){
                    return "locked";
                }
                //获取盐值
                String salt = rs.getString("salt");
                // 获取真正的hash过的密文
                String realpd = rs.getString("password");
                //对输入的密码进行PBKDF salt hash 与真正的密文进行比较
                boolean result = PasswordEncryption.authenticate(password, realpd, salt);
                databaseConnector.getConn().close();     // Close the database
                //System.out.println("关闭数据库连接对象");
                if(result){
                    System.out.println("succeed");
                    return "succeed";
                }
                else {
                    System.out.println("failed");
                    return "failed";
                }
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
        return "error";
    }

    @Override
    public String loginProxy(String id, String password) {
        // TODO 在此处加入读取aop配置文件
//        DynamicProxyFactory.afterargs = args;
        Card card = DynamicProxyFactory.getProxy(Card.class);
//        DynamicProxyFactory.beforeMethod = "";
//        DynamicProxyFactory.afterMethod ="";
//        AopXmlReader.readXml("src\\main\\java\\Exercise\\exercise1\\RPC\\Server\\Aop.xml","login");
//        Class<?>[] args = new Class[]{String.class,String.class};
        String res = card.login(id,password);
        return res;
    }

    /**
     *
     * @param id
     * @return 余额 ，如果返回-1 则表示账号出现错误
     */
    @Override
    public BigDecimal checkBalance(String id) {
        System.out.println("来查余额了");
        DatabaseConnector databaseConnector = new DatabaseConnector();
        BigDecimal res = new BigDecimal(-1);
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                String sql = "select deposit from user where id = ?";
                databaseConnector.setSql(sql);
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(sql);
                preparedStatement.setString(1, id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
                if(rs.next()){
                    //获取余额
                    res = rs.getBigDecimal("deposit");
                }

                databaseConnector.getConn().close();     // Close the database
                //System.out.println("关闭数据库连接对象");
            }
            databaseConnector.close();
            System.out.println("deposit = " + res);
            return res;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("deposit = " + res);
        return res;
    }

    /**
     *
     * @param id
     * @param num
     * @return 取款的成功用户否
     */
    @Override
    public String withdraw(String id, BigDecimal num) {
        System.out.println("来取钱了");
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String res = new String();
        BigDecimal deposit = new BigDecimal(-1);
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                databaseConnector.setSql("select deposit from user where id = ?");
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setString(1,id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
               if(rs.next()){
                   //获取余额
                   deposit = rs.getBigDecimal("deposit");
                   System.out.println("当前余额为" + deposit);
                   System.out.println("取钱金额为" + num);
                   // 余额足够
                   if(deposit.compareTo(num)>=0){
                       System.out.println("取钱成功");
                       deposit = deposit.subtract(num);
                       databaseConnector.setSql("update user set deposit = ? where id = ?");
                       preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                       preparedStatement.setBigDecimal(1,deposit);
                       preparedStatement.setString(2,id);
                       preparedStatement.execute();
                       System.out.println("deposit bocome " + deposit);
                       databaseConnector.getConn().close();     // Close the database
                       System.out.println("succeed");
                       return "succeed";
                   }
                   else {
                       System.out.println("取钱失败，余额不足");
                       databaseConnector.getConn().close();     // Close the database
                       System.out.println("failed");
                       return "failed";
                   }
                   //System.out.println("关闭数据库连接对象");
               }

            }
            databaseConnector.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        res = "error";
        System.out.println("error");
        return res;
    }

    @Override
    public String withdrawProxy(String id, BigDecimal num) {
        Card card = DynamicProxyFactory.getProxy(Card.class);
//        DynamicProxyFactory.beforeMethod = "";
//        DynamicProxyFactory.afterMethod ="";
//        AopXmlReader.readXml("src\\main\\java\\Exercise\\exercise1\\RPC\\Server\\Aop.xml","withdraw");
        String res = card.withdraw(id,num);
        return res;

    }

    @Override
    public String save(String id, BigDecimal num) {
        System.out.println("正在存入 "+ num +"元");
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String res = new String();
        BigDecimal deposit = new BigDecimal(-1);
        try {   // Loads the class object for the mysql driver into the DriverManager.
            databaseConnector.connect();
            if (databaseConnector.getConn() != null) {  //System.out.println("We   have   connected  to  our  database!");
                Statement stmt = databaseConnector.getConn().createStatement();
                databaseConnector.setSql("select deposit from user where id = ?");
                PreparedStatement preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setString(1,id);
                ResultSet rs = preparedStatement.executeQuery();                   //rs 记录查询的返回结果
                if(rs.next()){
                    //获取余额
                    deposit = rs.getBigDecimal("deposit");
                }
                deposit = deposit.add(num);
                System.out.println("deposite become "+ deposit);
                databaseConnector.setSql("update user set deposit = ? where id = ?");
                preparedStatement = databaseConnector.getConn().prepareStatement(databaseConnector.getSql());
                preparedStatement.setBigDecimal(1,deposit);
                preparedStatement.setString(2,id);
                preparedStatement.execute();
                databaseConnector.getConn().close();     // Close the database
                res = "succeed";
                System.out.println("succeed");
                //System.out.println("关闭数据库连接对象");
            }
            databaseConnector.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        res = "error";
        System.out.println("error");
        return res;
    }

    public static void main(String[] args) {
        String id = "123456";
        String password = "123456";
        BigDecimal bigDecimal = new BigDecimal(100);
        CardImpl card = new CardImpl();
        System.out.println(card.withdrawProxy(id,bigDecimal));
    }
}