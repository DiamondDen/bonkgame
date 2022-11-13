package net.dd.project1.client.game.world.entity.ai;

import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.world.entity.Entity;

@RequiredArgsConstructor
public class RandomMoveEntityAi implements EntityAi {

  private final Entity entity;

  private int tick;

  @Override
  public void tick() {
    if (tick++ % 60 == 0) {
      entity.move((float) (Math.random() * 5f), -(float) Math.abs(Math.random() * 20f));
    }
  }
}
