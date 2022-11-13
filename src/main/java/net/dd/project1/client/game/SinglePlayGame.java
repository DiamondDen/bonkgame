package net.dd.project1.client.game;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.controller.SinglePlayerController;
import net.dd.project1.client.game.world.World;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.entity.tower.TowerEntity;

import javax.swing.*;

public class SinglePlayGame extends Game {

  private int hearts = 3;

  public SinglePlayGame() {
    this.world = new World(this.gameDisplay.getWidth() * 2, this.gameDisplay.getHeight());

    this.addWorldObjects();
    this.respawn();
    this.startTimer();
    this.start();
  }

  @Override
  String getWindowTitle() {
    return "Singleplay";
  }

  public void respawn() {
    this.player = new SinglePlayerController(this, 50, 75, 50);
    this.getWorld().addWorldObject(this.player);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.player.isDied()) {
      this.hearts--;
      this.respawn();
    }
    if (this.hearts <= 0) {
      JOptionPane.showMessageDialog(null, "Вы проиграли");
      System.exit(0);
    }
  }

  public void addWorldObjects() {
    this.getWorld().addWorldObject(new WorldObject(-20, 0, 20, this.world.getHeight()));
    this.getWorld().addWorldObject(new WorldObject(this.world.getWidth(), 0, 20, this.world.getHeight()));
    this.getWorld().addWorldObject(new WorldObject(0, 0, this.world.getWidth(), 20));

    this.getWorld().addWorldObject(new TowerEntity(this.world.getWidth() - 150, 20, 100, 150));
  }

  @Override
  public void render(DrawHelper drawHelper) {
    super.render(drawHelper);

    for (int i = 0; i < this.hearts; i++) {
      drawHelper.fillRect(10 + i * 30, this.world.getHeight() - 30, 20, 20, DrawColor.of(1f, 0f, 0f));
    }
  }
}
