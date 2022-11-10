package net.dd.project1.shared.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dd.project1.client.game.math.Vector2f;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;
import net.dd.project1.shared.network.handler.PlayPacketHandler;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MovePacket implements Packet {

  private int id;
  private final int x, y;
  private final Vector2f motion;

  @Override
  public void handle(PacketHandler handler) {
    if (handler instanceof PlayPacketHandler)
      ((PlayPacketHandler) handler).handleMove(this);
  }
}
