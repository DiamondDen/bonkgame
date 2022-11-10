package net.dd.project1.client.game;

import net.dd.project1.client.game.controller.MultiPlayerController;
import net.dd.project1.client.game.network.client.NetworkManager;
import net.dd.project1.client.game.network.client.handler.ClientLoginListener;
import net.dd.project1.client.game.world.WorldObject;

public class MultiPlayGame extends Game {

  private final NetworkManager networkManager;

  public MultiPlayGame(String host, int port) {
    this.networkManager = new NetworkManager(this);

    this.networkManager.setHandler(new ClientLoginListener(this.networkManager));
    this.networkManager.connect(host, port);

    this.worldObjectList.add(new WorldObject(0, this.gameDisplay.getHeight() - 20, this.gameDisplay.getWidth(), 20));
    this.worldObjectList.add(new WorldObject(0, 0, 20, this.gameDisplay.getHeight()));
    this.worldObjectList.add(new WorldObject(this.gameDisplay.getWidth() - 20, 0, 20, this.gameDisplay.getHeight()));
    this.worldObjectList.add(new WorldObject(0, 0, this.gameDisplay.getWidth(), 20));

    this.worldObjectList.add(new MultiPlayerController(this, this.networkManager, 200, 200, 50));

    this.startTimer();
    this.start();
  }

  @Override
  String getWindowTitle() {
    return "Multiplay";
  }

  @Override
  public void tick() {
    this.networkManager.update();
    super.tick();
  }
}
