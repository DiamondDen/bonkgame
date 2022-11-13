package net.dd.project1.client.game.network.client.handler;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.dd.project1.client.game.Game;
import net.dd.project1.client.game.network.client.NetworkManager;
import net.dd.project1.client.game.world.entity.Player;
import net.dd.project1.client.game.world.entity.WorldRemotePlayer;
import net.dd.project1.shared.network.handler.PlayPacketHandler;
import net.dd.project1.shared.network.packets.JoinPacket;
import net.dd.project1.shared.network.packets.MovePacket;
import net.dd.project1.shared.network.packets.QuitPacket;

public class ClientPlayListener extends PlayPacketHandler {

  private final Game game;
  private final NetworkManager networkManager;

  private final Int2ObjectMap<WorldRemotePlayer> playersMap = new Int2ObjectOpenHashMap<>();

  public ClientPlayListener(NetworkManager networkManager) {
    this.game = networkManager.getGame();
    this.networkManager = networkManager;
  }

  @Override
  public void tick() {

  }

  @Override
  public void handleJoin(JoinPacket joinPacket) {
    WorldRemotePlayer worldPlayer = new WorldRemotePlayer(joinPacket.getX(), joinPacket.getY(), 50);

    this.playersMap.put(joinPacket.getId(), worldPlayer);
    this.game.addWorldObject(worldPlayer);
  }

  @Override
  public void handleQuit(QuitPacket quitPacket) {
    Player worldPlayer = this.playersMap.remove(quitPacket.getId());
    if (worldPlayer != null) {
      worldPlayer.kill();
    }
  }

  @Override
  public void handleMove(MovePacket movePacket) {
    WorldRemotePlayer worldPlayer = this.playersMap.get(movePacket.getId());
    if (worldPlayer != null) {
      worldPlayer.updatePosition(
              movePacket.getX(), movePacket.getY(),
              movePacket.getSequenceNumber(), movePacket.getMotion()
      );
    }
  }
}
