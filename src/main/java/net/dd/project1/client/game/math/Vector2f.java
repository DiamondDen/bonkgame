package net.dd.project1.client.game.math;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Vector2f {
  public static final Vector2f NUL = new Vector2f(0, 0);

  @Getter
  public float x, y;

  public Vector2f setX(float x) {
    this.x = x;
    return this;
  }

  public Vector2f setY(float y) {
    this.y = y;
    return this;
  }

  public boolean isNul() {
    return this == NUL || x == 0 && y == 0;
  }

  public Vector2f sub(Vector2f other) {
    if (other.isNul())
      return this;
    if (other == this)
      return NUL;
    return new Vector2f(this.x - other.x, this.y - other.y);
  }

  public Vector2f add(Vector2f other) {
    if (other.isNul())
      return this;
    return new Vector2f(this.x + other.x, this.y + other.y);
  }

  public Vector2f add(float x, float y) {
    return new Vector2f(this.x + x, this.y + y);
  }

  public Vector2f mul(Vector2f other) {
    if (other.isNul())
      return NUL;
    return new Vector2f(this.x * other.x, this.y * other.y);
  }

  public Vector2f max(Vector2f other) {
    return new Vector2f(Math.max(this.x, other.x), Math.max(this.y, other.y));
  }

  public Vector2f mul(float value) {
    if (value == 0)
      return NUL;
    return new Vector2f(this.x * value, this.y * value);
  }

  public Vector2f div(Vector2f other) {
    if (this.isNul())
      return NUL;
    return new Vector2f(this.x / other.x, this.y / other.y);
  }

  public Vector2f div(float value) {
    if (this.isNul())
      return NUL;
    return new Vector2f(this.x / value, this.y / value);
  }

  public Vector2f inv() {
    if (this.isNul())
      return NUL;
    return new Vector2f(-this.x, -this.y);
  }

  public Vector2f abs() {
    if (this.x >= 0 && this.y >= 0)
      return this;
    return new Vector2f(Math.abs(this.x), Math.abs(this.y));
  }

  public float length() {
    float x = this.x, y = this.y;
    return (float) Math.sqrt(x * x + y * y);
  }

  public Vector2f normalize() {
    float length = this.length();
    return new Vector2f(this.x / length, this.y / length);
  }

  public boolean isInAABB(Vector2f min, Vector2f max) {
    return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y;
  }

  public boolean inRange(Vector2f min, Vector2f max) {
    return this.x >= min.x && this.y >= min.y && this.x <= max.x && this.y <= max.y;
  }
}
