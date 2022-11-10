package net.dd.project1.shared.network.handler;

public interface PacketHandler {
  void tick();

  void onDisconnect();
}
