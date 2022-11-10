package net.dd.project1.server.network.server.handler;

import net.dd.project1.server.GameServer;
import net.dd.project1.server.network.server.NetworkClient;
import net.dd.project1.server.world.RemotePlayer;
import net.dd.project1.shared.network.handler.PlayPacketHandler;
import net.dd.project1.shared.network.packets.JoinPacket;
import net.dd.project1.shared.network.packets.MovePacket;
import net.dd.project1.shared.network.packets.QuitPacket;

public class ServerPlayListener extends PlayPacketHandler {

  private final GameServer gameServer;
  private final NetworkClient networkClient;
  private final RemotePlayer player;

  public ServerPlayListener(NetworkClient networkClient) {
    this.networkClient = networkClient;
    this.gameServer = this.networkClient.getServer();
    this.player = this.networkClient.getPlayer();

    this.gameServer.newConnection(networkClient);
  }

  private boolean joined = false;

  @Override
  public void tick() {
    if (!this.joined) {
      this.onPlayerJoin();
      this.joined = true;
    }
  }

  public void onPlayerJoin() {
    JoinPacket myselfJoin = new JoinPacket(
            this.networkClient.getId(),
            this.networkClient.getUsername(),
            this.player.getX(), this.player.getY(),
            50
    );
    for (NetworkClient onlinePlayer : this.gameServer.getOnlinePlayers()) {
      if (onlinePlayer == this.networkClient) continue;
      RemotePlayer otherPlayer = onlinePlayer.getPlayer();

      onlinePlayer.sendPacket(myselfJoin);
      this.networkClient.sendPacket(new JoinPacket(
              onlinePlayer.getId(),
              onlinePlayer.getUsername(),
              otherPlayer.getX(), otherPlayer.getY(),
              50
      ));
    }
  }

  @Override
  public void handleMove(MovePacket movePacket) {
    this.player.updatePosition(movePacket.getX(), movePacket.getY());
    this.player.setMotion(movePacket.getMotion());
  }

  @Override
  public void onDisconnect() {
    QuitPacket quitPacket = new QuitPacket(this.networkClient.getId());
    this.gameServer.broadcast(this.networkClient, quitPacket);
  }
}
