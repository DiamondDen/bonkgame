package net.dd.project1.client.game.world.entity.projectile;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.entity.Entity;
import net.dd.project1.client.game.world.entity.LivingEntity;

import java.util.List;

public class BombEntity extends Projectile {

  private Entity source;
  private int liveTicks = 30;

  public BombEntity(Entity source, int x, int y, int size) {
    this.source = source;
    this.setPosition(x, y);
    this.setSize(size, size);
    this.gravity = false;
    this.collision = false;
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    if (this.liveTicks-- <= 0) {
      this.die();
      return;
    }

    LivingEntity livingEntity = this.world.findCollisionEntity(LivingEntity.class,this, this.source);
    if (livingEntity != null) {
      livingEntity.damage(this, 7);
      this.die();
      return;
    }

    this.x += this.motion.getX();
  }

  @Override
  public void draw(DrawHelper helper) {
    helper.fillCircle(this.x, this.y, this.width / 2, DrawColor.BLACK);
  }
}
