package RPCLearn;

/**
 * Created by lyric  on 18/3/27.
 */
public class RPCServiceImpl implements  RPCService {

    @Override
    public String request(String s) {
        return ("本request服务收到发来的信息："+s);
    }
}