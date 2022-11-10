package net.dd.project1.shared.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;

@Getter
@AllArgsConstructor
public class KeepAlive implements Packet {

  private long time;

  @Override
  public void handle(PacketHandler handler) {

  }
}
