package net.dd.project1.client.game.world;

import lombok.AllArgsConstructor;
import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.display.Render;
import net.dd.project1.client.game.world.type.RectObject;

import java.util.List;

@AllArgsConstructor
public class WorldObject implements Render, RectObject {
  public int x, y;
  protected int width, height;

  public void tick(List<WorldObject> elementList) {

  }

  public boolean isDied() {
    return false;
  }

  public int calculateYOffset(WorldObject other, int v) {
    int rectA_X1 = other.x;
    int rectA_X2 = rectA_X1 + other.width;

    int rectA_Y1 = other.y;
    int rectA_Y2 = rectA_Y1 + other.height;

    int rectB_X1 = this.x;
    int rectB_X2 = rectB_X1 + this.width;

    int rectB_Y1 = this.y;
    int rectB_Y2 = rectB_Y1 + this.height;

    if (rectA_X1 <= rectB_X2 && rectA_X2 >= rectB_X1) {
      if (v > 0.0D && rectA_Y2 <= rectB_Y1) {
        v = Math.min(v, rectB_Y1 - rectA_Y2);
      } else if (v < 0.0D && rectA_Y1 >= rectB_Y2) {
        v = Math.max(v, rectB_Y2 - rectA_Y1);
      }
    }

    /*
    if (rectA_X1 <= rectB_X2 && rectA_X2 >= rectB_X1) {
      double distanceX = Math.pow(other.x - this.x, 2);
      double distanceY = Math.pow(other.y - this.y, 2);
      double distance = Math.sqrt(distanceX + distanceY);

      double j = Math.sqrt(this.width * this.width + this.height * this.height) / 2;
      System.out.println(distance + " " + j + " " + (distance - j) + " " + (distance - j - other.width / 2f ));
      if (distance - j - other.width / 2f < 0) {
        if (v > 0.0D && rectA_Y2 <= rectB_Y1) {
          v = Math.min(v, rectB_Y1 - rectA_Y2);
        } else if (v < 0.0D && rectA_Y1 >= rectB_Y2) {
          v = Math.max(v, rectB_Y2 - rectA_Y1);
        }
      }
    }*/

    return v;
  }

  public int calculateXOffset(WorldObject other, int v) {
    int rectA_X1 = other.x;
    int rectA_X2 = rectA_X1 + other.width;

    int rectA_Y1 = other.y;
    int rectA_Y2 = rectA_Y1 + other.height;

    int rectB_X1 = this.x;
    int rectB_X2 = rectB_X1 + this.width;

    int rectB_Y1 = this.y;
    int rectB_Y2 = rectB_Y1 + this.height;

    if (rectA_Y1 < rectB_Y2 && rectA_Y2 > rectB_Y1) {
      if (v > 0.0D && rectA_X2 <= rectB_X1) {
        int d1 = rectB_X1 - rectA_X2;
        if (d1 < v) v = d1;
      } else if (v < 0.0D && rectA_X1 >= rectB_X2) {
        int d0 = rectB_X2 - rectA_X1;
        if (d0 > v) v = d0;
      }
    }

    return v;
  }

  @Override
  public void draw(DrawHelper helper) {
    helper.fillRect(this.x, this.y, this.width, this.height, DrawColor.of(0.45f, 0.45f, 0.45f));
  }
}
