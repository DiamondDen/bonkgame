package net.dd.project1.shared.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;
import net.dd.project1.shared.network.handler.PlayPacketHandler;

@Getter
@AllArgsConstructor
public class JoinPacket implements Packet {
  private int id;
  private String username;
  private int x, y;
  private int size;

  @Override
  public void handle(PacketHandler handler) {
    if (handler instanceof PlayPacketHandler) {
      ((PlayPacketHandler) handler).handleJoin(this);
    }
  }
}
