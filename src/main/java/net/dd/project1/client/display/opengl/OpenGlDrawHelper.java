package net.dd.project1.client.display.opengl;

import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class OpenGlDrawHelper implements DrawHelper {
  private static final double DEG2RAD = 3.14159 / 180.0;

  @Override
  public void fillRect(int x, int y, int width, int height, DrawColor color) {
    glColor3f(color.getRed(), color.getGreen(), color.getBlue());

    glPushMatrix();
    glTranslatef(x, y, 0);
    glBegin(GL_QUADS);
    glVertex3f(0, 0, 0.0f);
    glVertex3f(0, height, 0.0f);
    glVertex3f(width, height, 0.0f);
    glVertex3f(width, 0, 0.0f);
    glEnd();
    glPopMatrix();
  }


  @Override
  public void drawCircle(int x, int y, int radius, DrawColor color) {
    drawOval(x, y, radius, radius, color);
  }

  @Override
  public void drawOval(int x, int y, int width, int height, DrawColor color) {
    glColor3f(color.getRed(), color.getGreen(), color.getBlue());

    glPushMatrix();
    glTranslatef(x + width, y + height, 0);
    glBegin(GL_LINE_LOOP);
    for (int i = 0; i < 360; i += 15) {
      float rad = (float) (i * DEG2RAD);
      glVertex2d(Math.cos(rad) * width, Math.sin(rad) * height);
    }
    glEnd();
    glPopMatrix();
  }
}
