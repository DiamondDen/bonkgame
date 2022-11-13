package net.dd.project1.client.game.world.entity.ai;

import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.world.entity.Entity;
import net.dd.project1.client.game.world.entity.LivingEntity;

@RequiredArgsConstructor
public class DamageEntityAi implements EntityAi {

  private final LivingEntity entity;
  private final float distance;
  private final int value;

  @Override
  public void tick() {
    Entity targetEntity = this.entity.getTargetEntity();
    if (targetEntity != null) {
      if (targetEntity.distance(this.entity) < distance) {
        this.entity.attack(targetEntity, this.value);
      }
    }
  }
}
