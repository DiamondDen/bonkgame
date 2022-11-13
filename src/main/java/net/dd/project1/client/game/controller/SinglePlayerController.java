package net.dd.project1.client.game.controller;

import lombok.val;
import net.dd.project1.client.game.Game;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.entity.Player;
import net.dd.project1.client.game.world.entity.projectile.BombEntity;

import java.util.List;

public class SinglePlayerController extends Player {

  private final Game game;

  private long lastThrowBombMs;

  public SinglePlayerController(Game game, int x, int y, int size) {
    super(x, y, size);
    this.game = game;
  }

  public void handleKeyboard() {
    val pressedKeys = game.getPressedKeysSet();

    // up
    if (pressedKeys.contains(32) || pressedKeys.contains(265) || pressedKeys.contains(87)) {
      this.jump();
    }
    // down
    if (pressedKeys.contains(264) || pressedKeys.contains(83)) {
      this.move(0, 0.1f);
    }
    // left
    if (pressedKeys.contains(263) || pressedKeys.contains(65)) {
      this.move(-2f, 0);
    }
    // right
    if (pressedKeys.contains(262) || pressedKeys.contains(68)) {
      this.move(2f, 0);
    }
   // System.out.println(pressedKeys);
    if (pressedKeys.contains(69)) {
      this.shotWithDelay(1);
    }
    if (pressedKeys.contains(81)) {
      this.shotWithDelay(-1);
    }
  }

  public void shotWithDelay(int xDelta) {
    long now = System.currentTimeMillis();
    if (now - this.lastThrowBombMs > 300) {
      this.lastThrowBombMs = now;
      this.shot(xDelta);
    }
  }

  public void shot(int xDelta) {
    int bombSize = 5;
    BombEntity bombEntity = new BombEntity(this,
            this.x + this.width / 2 - bombSize / 2,
            (int) (this.y + this.height / 2 - bombSize / 2 + Math.random() * 10f),
            bombSize / 2
    );
    bombEntity.move0(xDelta * 7, 0);
    this.game.getWorld().addWorldObject(bombEntity);
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    this.handleKeyboard();
    super.tick(elementList);
  }
}
