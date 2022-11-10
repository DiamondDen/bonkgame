package net.dd.project1.shared.network;

import net.dd.project1.shared.network.packets.*;

public class GamePacketManager extends PacketManager {

  public void registerAllPackets() {
    this.registerPacket("login", LoginPacket.class);
    this.registerPacket("move", MovePacket.class);
    this.registerPacket("keepalive", KeepAlive.class);
    this.registerPacket("join", JoinPacket.class);
    this.registerPacket("quit", QuitPacket.class);
  }

}
