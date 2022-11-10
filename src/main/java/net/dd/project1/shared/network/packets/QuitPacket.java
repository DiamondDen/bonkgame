package net.dd.project1.shared.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;
import net.dd.project1.shared.network.handler.PlayPacketHandler;

@Getter
@AllArgsConstructor
public class QuitPacket implements Packet {

  private int id;

  @Override
  public void handle(PacketHandler handler) {
    if (handler instanceof PlayPacketHandler) {
      ((PlayPacketHandler) handler).handleQuit(this);
    }
  }
}
