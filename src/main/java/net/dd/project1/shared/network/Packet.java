package net.dd.project1.shared.network;

import net.dd.project1.shared.network.handler.PacketHandler;

public interface Packet {
  void handle(PacketHandler handler);
}
