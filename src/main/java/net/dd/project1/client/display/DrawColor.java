package net.dd.project1.client.display;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DrawColor {

  public static final DrawColor WHITE = DrawColor.of(1f, 1f, 1f);
  public static final DrawColor BLACK = DrawColor.of(0f, 0f, 0f);
  public static final DrawColor GREY = DrawColor.of(0.45f, 0.45f, 0.45f);
  public static final DrawColor RED = DrawColor.of(1f, 0f, 0f);
  public static final DrawColor GREEN = DrawColor.of(0f, 1f, 0f);
  public static final DrawColor BLUE = DrawColor.of(0f, 0f, 1f);

  private final float red, green, blue;

  public static DrawColor of(float r, float g, float b) {
    return new DrawColor(r, g, b);
  }
  public static DrawColor of(int r, int g, int b) {
    return new DrawColor(r / 255f, g / 255f, b / 255f);
  }
}
