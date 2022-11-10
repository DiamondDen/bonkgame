import net.dd.project1.client.game.MultiPlayGame;
import net.dd.project1.client.game.SinglePlayGame;
import net.dd.project1.server.GameServer;

public class Start {

  public static void main(String[] args) {
    //args = new String[] {"--connect-to", "localhost:25577"};

    if (args.length >= 1) {
      switch (args[0]) {
        case "--server":
          GameServer server = new GameServer();
          server.startServer(25577);
          break;
        case "--connect-to":
          if (args.length == 2) {
            String[] split = args[1].split(":");
            String host = split[0];
            int port;
            try {
              port = Integer.parseInt(split[1]);
            } catch (NumberFormatException e) {
              System.err.println("Port is not a integer");
              return;
            }

            new MultiPlayGame(host, port);
          } else {
            System.err.println("Not found host:port");
          }
          break;
      }
    } else {
      new SinglePlayGame();
    }
  }

}
