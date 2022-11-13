package net.dd.project1.client.game.world.entity.enemies;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.game.world.entity.LivingEntity;
import net.dd.project1.client.game.world.entity.ai.DamageEntityAi;
import net.dd.project1.client.game.world.entity.ai.FindPlayerEntityAi;
import net.dd.project1.client.game.world.entity.ai.MoveToTargetEntityAi;

public class ZombieEntity extends LivingEntity implements Zombie {

  public ZombieEntity(int x, int y, int size, int health, int damage) {
    this.setPosition(x, y);
    this.setSize(size, size);
    this.setHealth(health);
    this.eyeColor = DrawColor.of(184, 41, 28);
    this.bodyColor = DrawColor.of(62, 168, 50);
    this.collision = false;

    this.entityAiList.add(new FindPlayerEntityAi(this));
    this.entityAiList.add(new MoveToTargetEntityAi(this, 1));
    this.entityAiList.add(new DamageEntityAi(this, 25, damage));
  }

}
