package Exercise.exercise1.RPC;

import java.math.BigDecimal;

/**
 *
 */
public interface Card {
     String request(String s);//服务功能
     String login(String id, String password);//登陆服务
     String loginProxy(String id, String password);//登录代理服务
     BigDecimal checkBalance(String id);//查询余额服务
     String withdraw(String id,BigDecimal num);//取钱服务
     String withdrawProxy(String id,BigDecimal num);//取钱代理服务
     String save(String id,BigDecimal num);//存钱服务
}
