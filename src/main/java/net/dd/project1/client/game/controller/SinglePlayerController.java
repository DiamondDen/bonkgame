package net.dd.project1.client.game.controller;

import lombok.val;
import net.dd.project1.client.game.Game;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.WorldPlayer;

import java.util.List;

public class SinglePlayerController extends WorldPlayer {

  private final Game game;

  public SinglePlayerController(Game game, int x, int y, int size) {
    super(x, y, size);
    this.game = game;
  }

  public void handleKeyboard() {
    val pressedKeys = game.getPressedKeysSet();

    if (pressedKeys.contains(32) || pressedKeys.contains(265)) {
      this.jump();
    }
    if (pressedKeys.contains(264)) {
      this.move(0, 0.1f);
    }
    if (pressedKeys.contains(263)) {
      this.move(-2f, 0);
    }
    if (pressedKeys.contains(262)) {
      this.move(2f, 0);
    }
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    this.handleKeyboard();
    super.tick(elementList);
  }
}
