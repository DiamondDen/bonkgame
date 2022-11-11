package net.dd.project1.server.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dd.project1.client.game.math.Vector2f;
import net.dd.project1.server.GameServer;
import net.dd.project1.server.network.server.NetworkClient;
import net.dd.project1.shared.network.packets.MovePacket;

@Getter
@Setter
@RequiredArgsConstructor
public class RemotePlayer {

  private final int id;
  private final GameServer gameServer;
  private final NetworkClient networkClient;

  private long ping;


  private int lastX, lastY;
  private int x, y;
  private Vector2f motion;
  private long moveSequenceNumber;

  public void tick() {
    if (this.x != this.lastX || this.y != this.lastY) {
      this.gameServer.broadcast(this.networkClient, new MovePacket(
              this.id, this.x, this.y, this.moveSequenceNumber, this.motion
      ));

      this.lastX = this.x;
      this.lastY = this.y;
    }
  }

  public void updatePosition(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
