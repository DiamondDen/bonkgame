package net.dd.project1.client.game.controller;

import net.dd.project1.client.game.Game;
import net.dd.project1.client.game.network.client.NetworkManager;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.shared.network.packets.MovePacket;

import java.util.List;

public class MultiPlayerController extends SinglePlayerController {

  private final NetworkManager networkManager;

  public MultiPlayerController(Game game, NetworkManager networkManager, int x, int y, int size) {
    super(game, x, y, size);
    this.networkManager = networkManager;
  }

  private int lastX, lastY;
  private long sequenceNumber;

  @Override
  public void tick(List<WorldObject> elementList) {
    super.tick(elementList);

    if (this.lastX != this.x || this.lastY != this.y) {
      this.networkManager.sendPacket(new MovePacket(this.x, this.y, this.sequenceNumber++, this.motion));
      this.lastX = this.x;
      this.lastY = this.y;
    }
  }
}
