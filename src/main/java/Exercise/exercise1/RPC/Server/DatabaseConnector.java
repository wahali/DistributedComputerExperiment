package Exercise.exercise1.RPC.Server;

import java.sql.*;

public class DatabaseConnector {
    private String user = "root";
    private String password = "123123";
    private String url = "jdbc:mysql://localhost:3306/distributedcomputer?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private Connection conn;
    private Statement stmt;
    private String sql;
    private ResultSet rs;

    public void connect(){
        try {   // Loads the class object for the mysql driver into the DriverManager.
            conn = null;
            Class.forName("com.mysql.jdbc.Driver");                //加载驱动
            // Attempt to establish a connection to the specified database via the DriverManager
            conn = DriverManager.getConnection(url, user, password);
            // Check the connection
//            if (conn != null) {  //System.out.println("We   have   connected  to  our  database!");
//
//                Statement stmt = conn.createStatement();
//                String sql = "select * from mysql.student1";
//                ResultSet rs = stmt.executeQuery(sql);                   //rs 记录查询的返回结果
//                int flag = 0;            //用来记录登录状态
//                while (rs.next()) {
//
//                }
//
////                conn.close();     // Close the database
//                //System.out.println("关闭数据库连接对象");
//            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void close() throws SQLException {
        conn.close();
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }


}
