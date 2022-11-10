package net.dd.project1.client.game;

import net.dd.project1.client.game.controller.SinglePlayerController;
import net.dd.project1.client.game.world.WorldObject;

public class SinglePlayGame extends Game {

  public SinglePlayGame() {
    this.worldObjectList.add(new SinglePlayerController(this, 200, 200, 50));
    this.addWorldObjects();
    this.startTimer();
    this.start();
  }

  @Override
  String getWindowTitle() {
    return "Singleplay";
  }

  public void addWorldObjects() {
    this.worldObjectList.add(new WorldObject(0, this.gameDisplay.getHeight() - 20, this.gameDisplay.getWidth(), 20));
    this.worldObjectList.add(new WorldObject(0, 0, 20, this.gameDisplay.getHeight()));
    this.worldObjectList.add(new WorldObject(this.gameDisplay.getWidth() - 20, 0, 20, this.gameDisplay.getHeight()));
    this.worldObjectList.add(new WorldObject(0, 0, this.gameDisplay.getWidth(), 20));

    for (int i = 0; i < 5; i += 2) {
      int x = 20 + i * 100;
      int y = this.gameDisplay.getHeight() - 50 - i * 25;
      this.worldObjectList.add(new WorldObject(x, y, 90, 15));
    }
    for (int i = 0; i < 5; i += 2) {
      int x = 20 + 510 - i * 100;
      int y = this.gameDisplay.getHeight() - 50 - (6 + i) * 25;
      this.worldObjectList.add(new WorldObject(x, y, 90, 15));
    }
  }

}
