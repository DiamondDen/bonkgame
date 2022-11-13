package net.dd.project1.client.display;

public interface DrawHelper {
  long nowMs();

  void drawRect(int x, int y, int width, int height, DrawColor color);

  void fillRect(int x, int y, int width, int height, DrawColor color);

  void drawCircle(int x, int y, int radius, DrawColor color);

  void drawOval(int x, int y, int width, int height, DrawColor color);

  void fillCircle(int x, int y, int radius, DrawColor color);

  void fillOval(int x, int y, int width, int height, DrawColor color);
}
