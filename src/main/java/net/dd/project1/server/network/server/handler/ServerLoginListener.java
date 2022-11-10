package net.dd.project1.server.network.server.handler;

import lombok.RequiredArgsConstructor;
import net.dd.project1.server.network.server.NetworkClient;
import net.dd.project1.shared.network.handler.LoginPacketHandler;
import net.dd.project1.shared.network.packets.LoginPacket;

import java.util.UUID;

@RequiredArgsConstructor
public class ServerLoginListener extends LoginPacketHandler {

  private final NetworkClient networkClient;

  private UUID uuid;
  private String username;

  private volatile ConnectionState state = ConnectionState.LOGIN;

  @Override
  public void tick() {
    if (this.state == ConnectionState.PLAY) {
      this.state = ConnectionState.END;

      this.networkClient.setUuid(this.uuid);
      this.networkClient.setUsername(this.username);
      this.networkClient.sendPacket(new LoginPacket(this.uuid, this.username, this.networkClient.getId()));
      this.networkClient.setPacketHandler(new ServerPlayListener(this.networkClient));
      System.out.println("Player " + this.username + " join to game with id " + this.networkClient.getId());
    }
  }

  @Override
  public void handleLogin(LoginPacket loginPacket) {
    if (this.state != ConnectionState.LOGIN) {
      return;
    }

    this.uuid = loginPacket.getUuid();
    this.username = loginPacket.getUsername();

    this.state = ConnectionState.PLAY;
  }

  enum ConnectionState {
    LOGIN,
    PLAY,
    END
  }
}
