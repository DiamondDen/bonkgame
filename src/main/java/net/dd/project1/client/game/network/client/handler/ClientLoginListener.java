package net.dd.project1.client.game.network.client.handler;

import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.network.client.NetworkManager;
import net.dd.project1.shared.network.handler.LoginPacketHandler;
import net.dd.project1.shared.network.packets.LoginPacket;

@RequiredArgsConstructor
public class ClientLoginListener extends LoginPacketHandler {

  private final NetworkManager networkManager;
  private volatile ConnectionState state;

  @Override
  public void tick() {
    if (this.state == ConnectionState.PLAY) {
      this.networkManager.setHandler(new ClientPlayListener(this.networkManager));
      this.state = ConnectionState.END;
    }
  }

  @Override
  public void handleLogin(LoginPacket loginPacket) {
    this.networkManager.setId(loginPacket.getId());
    this.state = ConnectionState.PLAY;
  }

  enum ConnectionState {
    LOGIN,
    PLAY,
    END;
  }
}
