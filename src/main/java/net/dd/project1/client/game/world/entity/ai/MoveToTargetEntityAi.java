package net.dd.project1.client.game.world.entity.ai;

import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.math.Vector2f;
import net.dd.project1.client.game.world.entity.Entity;
import net.dd.project1.client.game.world.entity.LivingEntity;

@RequiredArgsConstructor
public class MoveToTargetEntityAi implements EntityAi {

  private final LivingEntity entity;
  private final float speed;

  @Override
  public void tick() {
    Entity targetEntity = this.entity.getTargetEntity();
    if (targetEntity == null) return;
    Vector2f diff = targetEntity.centerPositionAsVector().sub(entity.centerPositionAsVector());
    float length = diff.length();
    if (length > 0) {
      this.entity.move(diff.normalize().setY(0).mul(speed));
    }
  }
}
