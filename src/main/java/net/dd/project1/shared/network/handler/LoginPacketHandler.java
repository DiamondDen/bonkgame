package net.dd.project1.shared.network.handler;

import net.dd.project1.shared.network.packets.LoginPacket;

public abstract class LoginPacketHandler implements PacketHandler {
  public void handleLogin(LoginPacket loginPacket) {

  }

  @Override
  public void onDisconnect() {

  }
}
