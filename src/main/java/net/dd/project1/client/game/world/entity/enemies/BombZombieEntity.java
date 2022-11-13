package net.dd.project1.client.game.world.entity.enemies;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.game.world.entity.LivingEntity;
import net.dd.project1.client.game.world.entity.ai.ExplosionEntityAi;
import net.dd.project1.client.game.world.entity.ai.FindPlayerEntityAi;
import net.dd.project1.client.game.world.entity.ai.MoveToTargetEntityAi;

public class BombZombieEntity extends LivingEntity implements Zombie {

  public BombZombieEntity(int x, int y, int size, int health, int damage) {
    this.setPosition(x, y);
    this.setSize(size, size);
    this.setHealth(health);
    this.eyeColor = DrawColor.of(184, 41, 28);
    this.bodyColor = DrawColor.of(186, 77, 41);
    this.collision = false;

    this.entityAiList.add(new FindPlayerEntityAi(this));
    this.entityAiList.add(new MoveToTargetEntityAi(this, 1.7f));
    this.entityAiList.add(new ExplosionEntityAi(this, 25, damage));
  }

}
