package net.dd.project1.shared.network.handler;

import net.dd.project1.shared.network.packets.JoinPacket;
import net.dd.project1.shared.network.packets.MovePacket;
import net.dd.project1.shared.network.packets.QuitPacket;

public abstract class PlayPacketHandler implements PacketHandler {

  public void handleMove(MovePacket movePacket) {

  }

  public void handleJoin(JoinPacket joinPacket) {

  }

  public void handleQuit(QuitPacket quitPacket) {

  }

  @Override
  public void onDisconnect() {

  }
}
