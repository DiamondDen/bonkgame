package net.dd.project1.shared.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.LoginPacketHandler;
import net.dd.project1.shared.network.handler.PacketHandler;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class LoginPacket implements Packet {

  private UUID uuid;
  private String username;
  private int id;

  @Override
  public void handle(PacketHandler handler) {
    if (handler instanceof LoginPacketHandler) {
      ((LoginPacketHandler) handler).handleLogin(this);
    }
  }
}
