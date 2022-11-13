package net.dd.project1.client.game.world.entity;

import lombok.Getter;
import lombok.Setter;
import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.WorldObject;

import java.util.List;

@Getter
@Setter
public class LivingEntity extends Entity {

  protected DrawColor bodyColor = DrawColor.WHITE;
  protected DrawColor eyeColor = DrawColor.WHITE;

  @Getter
  protected Entity targetEntity;
  protected long argTimeMs;
  protected long lastDamageTimeMs;

  protected int health, maxHealth;

  @Override
  public void tick(List<WorldObject> elementList) {
    if (this.health <= 0) {
      this.die();
      return;
    }

    super.tick(elementList);

    if (this.world.nowMs() > this.argTimeMs || (targetEntity != null && targetEntity.isDied())) {
      this.targetEntity = null;
    }
  }


  public void setTargetEntity(Entity entity, long argTimeMs) {
    this.targetEntity = entity;
    this.argTimeMs = this.world.nowMs() + argTimeMs;
  }

  public void damage(Entity source, int value) {
    this.health = Math.max(0, this.health - value);
    this.lastDamageTimeMs = System.currentTimeMillis();
    if (this.health == 0)
      this.die();
  }

  public void setHealth(int health) {
    this.health = health;
    this.maxHealth = Math.max(this.maxHealth, this.health);
  }

  public void drawHealthBar(DrawHelper helper) {
    int healthBarWidth = (int) (this.width * 0.75f);
    int healthBarX = (int) (this.x + this.width / 2f - healthBarWidth / 2f);
    helper.fillRect(healthBarX, this.y + this.height + 5,
            healthBarWidth, 5,
            DrawColor.GREY
    );
    helper.fillRect(healthBarX, this.y + this.height + 5,
            (int) (healthBarWidth * (this.getHealth() / (double) this.getMaxHealth())), 5,
            DrawColor.RED
    );
  }

  public boolean isAttacked(DrawHelper helper) {
    return helper.nowMs() - this.lastDamageTimeMs < 150;
  }

  @Override
  public void draw(DrawHelper helper) {
    helper.fillCircle(this.x, this.y, this.width / 2, this.bodyColor);
    helper.drawCircle(this.x, this.y, this.width / 2,
            this.isAttacked(helper) ? DrawColor.of(1f, 0f, 0f) : DrawColor.BLACK
    );
    int eyeY = (int) (this.y + this.height * 0.5f);
    helper.drawOval(this.x + this.width / 2 - 16, eyeY, 7, 5, DrawColor.BLACK);
    helper.drawOval(this.x + this.width / 2 + 1, eyeY, 7, 5, DrawColor.BLACK);

    helper.fillOval(this.x + this.width / 2 - 9 - 2 , eyeY + 3, 2, 2, this.eyeColor);
    helper.drawOval(this.x + this.width / 2 - 9 - 2 , eyeY + 3, 2, 2, DrawColor.BLACK);
    helper.fillOval(this.x + this.width / 2 + 8 - 2 , eyeY + 3, 2, 2, this.eyeColor);
    helper.drawOval(this.x + this.width / 2 + 8 - 2 , eyeY + 3, 2, 2, DrawColor.BLACK);

    this.drawHealthBar(helper);
  }
}
