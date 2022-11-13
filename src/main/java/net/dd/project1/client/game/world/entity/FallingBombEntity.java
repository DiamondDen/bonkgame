package net.dd.project1.client.game.world.entity;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.WorldObject;

import java.util.List;

public class FallingBombEntity extends Entity {

  private final int damage;

  public FallingBombEntity(int x, int y, int width, int height, int damage) {
    this.setPosition(x, y);
    this.setSize(width, height);
    this.damage = damage;
    this.collision = false;
    this.noclip = true;
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    super.tick(elementList);
    Player target = this.world.findNearEntity(Player.class, this);
    if (target != null && target.distance(this) < 25) {
      this.attack(target, this.damage);
      this.die();
    }
  }

  @Override
  public void draw(DrawHelper helper) {
    helper.fillRect(this.x, this.y, this.width, this.height, DrawColor.BLACK);
  }
}
