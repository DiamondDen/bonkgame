package net.dd.project1.client.game.world.entity.tower;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.WorldObject;
import net.dd.project1.client.game.world.entity.FallingBombEntity;
import net.dd.project1.client.game.world.entity.LivingEntity;
import net.dd.project1.client.game.world.entity.enemies.BombZombieEntity;
import net.dd.project1.client.game.world.entity.enemies.ZombieEntity;

import javax.swing.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class TowerEntity extends LivingEntity {

  public TowerEntity(int x, int y, int width, int height) {
    this.setPosition(x, y);
    this.setSize(width, height);
    this.setHealth(500);
    this.collision = false;
    this.gravity = false;
  }

  @Override
  public void die() {
    super.die();
    JOptionPane.showMessageDialog(null, "Вы победили");
    System.exit(0);
  }

  private int tick;

  @Override
  public void tick(List<WorldObject> elementList) {
    super.tick(elementList);

    double healthPercent = this.getHealth() / (double) this.getMaxHealth();
    int stage;
    if (healthPercent > 0.7f) {
      stage = 0;
    } else if (healthPercent > 0.5f) {
      stage = 1;
    } else {
      stage = 2;
    }

    if (tick % (stage == 0 ? 180 : 85) == 0) {
      this.world.addWorldObject(new ZombieEntity(this.x + this.width / 2, this.y + 5, 30, 20, 1));
    }
    if (stage >= 1 && tick % 450 == 0) {
      this.world.addWorldObject(new ZombieEntity(50, this.y + 5, 30, 15, 2));
    }
    if (stage == 2 && tick % 700 == 0) {
      this.world.addWorldObject(new BombZombieEntity(this.x + this.width / 2, this.y + 5, 25, 5, 10));
    }

    if (tick % (stage == 0 ? 10 : 3) == 0) {
      this.world.addWorldObject(new FallingBombEntity(
              (int) (Math.random() * (this.world.getWidth() * 5)),
              this.world.getHeight(),
              3, 7, new int[] {1, 2, 5} [stage]
      ));
    }

    this.tick++;
  }

  @Override
  public void draw(DrawHelper helper) {
    int width3 = this.width / 3;
    int baseWidth = this.width - width3 * 2;

    DrawColor color = this.isAttacked(helper) ? DrawColor.RED : DrawColor.BLACK;

    drawTower(this.x, this.y, width3, this.height - (int) (this.height * 0.2), color);
    drawTower(this.x + width3, this.y, baseWidth, this.height, color);
    drawTower(this.x + this.width - width3, this.y, width3, this.height - (int) (this.height * 0.2), color);

    helper.fillRect(this.x + width3, this.y, baseWidth, (int) (this.height * 0.25f), DrawColor.WHITE);
    helper.fillCircle(this.x + width3, this.y + (int) (this.height * 0.25f) - baseWidth / 2, baseWidth / 2, DrawColor.WHITE);

    this.drawHealthBar(helper);
  }

  public void drawTower(int x, int y, int width, int height, DrawColor color) {
    glColor3f(color.getRed(), color.getGreen(), color.getBlue());

    glPushMatrix();
    glTranslatef(x, y, 0);
    glBegin(GL_QUAD_STRIP);
    glVertex3f(0, 0, 0.0f);
    glVertex3f(0, height, 0.0f);
    glVertex3f(width, height, 0.0f);
    glVertex3f(width, 0, 0.0f);
    glEnd();
    glPopMatrix();
  }
}
