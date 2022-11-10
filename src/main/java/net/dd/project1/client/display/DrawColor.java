package net.dd.project1.client.display;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DrawColor {

  public static final DrawColor WHITE = DrawColor.of(1, 1, 1);
  public static final DrawColor BLACK = DrawColor.of(0, 0, 0);

  private final float red, green, blue;

  public static DrawColor of(float r, float g, float b) {
    return new DrawColor(r, g, b);
  }
}
