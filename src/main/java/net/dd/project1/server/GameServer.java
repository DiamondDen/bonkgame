package net.dd.project1.server;

import net.dd.project1.server.network.server.NetworkClient;
import net.dd.project1.server.network.server.NetworkServer;
import net.dd.project1.server.network.server.connection.ServerConnectionHandler;
import net.dd.project1.server.network.server.handler.ServerLoginListener;
import net.dd.project1.server.network.server.impl.TcpNettyServer;
import net.dd.project1.server.network.server.impl.udp.UdpNettyServer;
import net.dd.project1.shared.network.GamePacketManager;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.packets.KeepAlive;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServer {
  
  private final NetworkServer networkServer;
  private final GamePacketManager packetManager;
  private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(runnable -> {
    Thread thread = new Thread(runnable, "Server Thread");
    thread.setDaemon(true);
    return thread;
  });

  private final List<NetworkClient> clientList = Collections.synchronizedList(new ArrayList<>());
  private final Map<UUID, NetworkClient> onlinePlayerMap = new HashMap<>();

  public GameServer() {
    this.packetManager = new GamePacketManager();
    this.packetManager.registerAllPackets();

    ServerConnectionHandler connectionHandler = new ServerConnectionHandler(this, this.packetManager);

    this.networkServer = new UdpNettyServer(connectionHandler);
    //this.networkServer = new TcpNettyServer(connectionHandler);
  }

  public Collection<NetworkClient> getOnlinePlayers() {
    return this.onlinePlayerMap.values();
  }

  public void addUntrustedConnection(NetworkClient client) {
    this.clientList.add(client);
    client.setPacketHandler(new ServerLoginListener(client));
  }

  public void newConnection(NetworkClient client) {
    this.onlinePlayerMap.put(client.getUuid(), client);
  }
  
  private void removeConnection(NetworkClient client) {
    this.onlinePlayerMap.remove(client.getUuid());
    // из списка удаляет в tick
  }

  public void broadcast(NetworkClient without, Packet packet) {
    for (NetworkClient onlinePlayer : this.getOnlinePlayers()) {
      if (onlinePlayer == without) continue;

      onlinePlayer.sendPacket(packet);
    }
  }

  public void startServer(int port) {
    this.service.scheduleWithFixedDelay(this::gameTick, 50, 50, TimeUnit.MILLISECONDS);
    this.service.scheduleWithFixedDelay(this::keepAliveUpdate, 5, 5, TimeUnit.SECONDS);

    this.networkServer.start(port);
  }

  public void keepAliveUpdate() {
    long time = System.currentTimeMillis();
    for (NetworkClient networkClient : this.getOnlinePlayers()) {
      networkClient.sendPacket(new KeepAlive(time));
    }
  }

  public void gameTick() {
    this.clientList.removeIf(networkClient -> {
      if (networkClient.isDisconnected()) {
        this.removeConnection(networkClient);
        return true;
      }
      networkClient.tick();
      return false;
    });
  }
}
