package Exercise.exercise1.RPC;

import Exercise.exercise1.RPC.Server.Server;

public class Test2 {
    public static void main(String[] args) throws Exception {
        Server server1 = new Server(8008);
        server1.start();

    }
}
