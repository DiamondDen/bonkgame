package net.dd.project1.client.game.world.entity;

import net.dd.project1.client.game.math.Vector2f;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.entity.ai.EntityAi;

import java.util.ArrayList;
import java.util.List;

public class Entity extends WorldObject {

  protected int id;

  public Vector2f motion = new Vector2f();
  protected boolean onGround;

  protected boolean alive = true;
  protected boolean gravity = true;
  protected boolean noclip = false;

  private long lastAttackTimeMs;

  protected List<EntityAi> entityAiList = new ArrayList<>();

  public void kill() {
    this.alive = false;
  }

  @Override
  public boolean isDied() {
    return !this.alive;
  }

  public void die() {
    if (!this.alive) return;
    this.alive = false;
  }

  public void attack(Entity target, int value) {
    if (target instanceof LivingEntity && this.world.nowMs() - this.lastAttackTimeMs > 500) {
      this.lastAttackTimeMs = this.world.nowMs();
      ((LivingEntity) target).damage(this, value);
    }
  }

  public Vector2f centerPositionAsVector() {
    return new Vector2f(this.x + this.width / 2f, this.y + this.height / 2f);
  }

  public void jump() {
    if (this.onGround) {
      this.move(0, 17.5f);
      this.onGround = false;
    }
  }

  public void move(Vector2f vector2f) {
    this.move(vector2f.getX(), vector2f.getY());
  }

  public void move(float x, float y) {
    if (!this.onGround) {
      x *= 0.3f;
    }
    this.motion = this.motion.add(x, y);
  }

  public void move0(float x, float y) {
    this.motion = this.motion.add(x, y);
  }

  private double log2(float N) {
    return Math.log(N) / Math.log(2);
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    if (this.y < 0) {
      this.die();
      return;
    }

    for (EntityAi entityAi : entityAiList) {
      entityAi.tick();
    }

    if (gravity) {
      this.move(0, -0.77f);
    }

    int x = 0, y = 0;

    // Сопротивление по направлению
    {
      int invert = this.motion.x > 0 ? -1 : 1;
      if (Math.abs(this.motion.x) > 1) {
        this.motion.x += invert * Math.abs(this.motion.x)
                // Когда игрок находится на земле, сопротивление сильнее
                * (this.onGround ? 0.1f : 0.025f);

        double offset = log2(Math.abs(this.motion.x)) * -invert;
        if (!Double.isInfinite(offset) && !Double.isNaN(offset)) {
          x += offset;
        }
      }
    }
    {
      int invert = this.motion.y > 0 ? -1 : 1;
      if (this.motion.y > 0.1 || this.motion.y < -0.1) {
        this.motion.y += invert * Math.abs(this.motion.y) * 0.1f;
      }
      y += this.motion.getY();
    }

    double tmpY = y, tmpX = x;
    if (!noclip) {
      for (WorldObject worldElement : elementList) {
        if (worldElement == this || (worldElement instanceof Entity && !this.collision)) continue;

        x = worldElement.calculateXOffset(this, x);
        y = worldElement.calculateYOffset(this, y);
      }
    }

    // Гасим инерцию
    if (x == 0 && Math.abs(tmpX) > 0) this.motion.x = 0;
    if (y == 0 && Math.abs(tmpY) > 0) this.motion.y = 0;

    this.onGround = y == 0 && tmpY < 0;

    this.x += x;
    this.y += y;
  }

}
