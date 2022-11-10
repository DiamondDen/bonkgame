package net.dd.project1.client.game.world;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.type.OvalObject;
import net.dd.project1.client.game.math.Vector2f;

import java.util.List;

public class WorldPlayer extends WorldObject implements OvalObject {

  public Vector2f motion = new Vector2f();
  private boolean onGround;

  private boolean alive = true;

  public WorldPlayer(int x, int y, int size) {
    super(x, y, size, size);
  }

  public void kill() {
    this.alive = false;
  }

  @Override
  public boolean isDied() {
    return !this.alive;
  }

  public void jump() {
    if (this.onGround) {
      this.move(0, -17.5f);
      this.onGround = false;
    }
  }

  public void move(float x, float y) {
    if (!this.onGround) {
      x *= 0.3f;
    }
    this.motion = this.motion.add(x, y);
  }

  public static double log2(float N) {
    return Math.log(N) / Math.log(2);
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    this.move(0, 0.7f);

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
    for (WorldObject worldElement : elementList) {
      if (worldElement == this) continue;

      x = worldElement.calculateXOffset(this, x);
      y = worldElement.calculateYOffset(this, y);
    }

    // Гасим инерцию
    if (x == 0 && Math.abs(tmpX) > 0) this.motion.x = 0;
    if (y == 0 && Math.abs(tmpY) > 0) this.motion.y = 0;

    this.onGround = y == 0 && tmpY > 0;

    this.x += x;
    this.y += y;
  }

  @Override
  public void draw(DrawHelper helper) {
    helper.drawCircle(this.x, this.y, this.width / 2, DrawColor.BLACK);
    int eyeY = this.y + 13;
    helper.drawOval(this.x + this.width / 2 - 16, eyeY, 7, 5, DrawColor.BLACK);
    helper.drawOval(this.x + this.width / 2 + 1, eyeY, 7, 5, DrawColor.BLACK);

    helper.drawOval(this.x + this.width / 2 - 9 - 2 , eyeY + 3, 2, 2, DrawColor.BLACK);
    helper.drawOval(this.x + this.width / 2 + 8 - 2 , eyeY + 3, 2, 2, DrawColor.BLACK);
  }
}
