package net.dd.project1.client.game.world;

import java.util.List;

public class WorldRemotePlayer extends WorldPlayer {

  private long lastUpdatePositionMs;
  private long updateLatencyMs;

  private int prevX, prevY;
  private int nextX, nextY;

  public WorldRemotePlayer(int x, int y, int size) {
    super(x, y, size);

    this.prevX = x;
    this.prevY = y;

    this.updatePosition(x, y);
  }

  public boolean interpolation = true;

  public void updatePosition(int x, int y) {
    if (!interpolation) {
      this.x = x;
      this.y = y;
      return;
    }

    long now = System.currentTimeMillis();

    this.prevX = this.nextX;
    this.prevY = this.nextY;

    this.nextX = x;
    this.nextY = y;

    if (this.lastUpdatePositionMs != 0)
      this.updateLatencyMs = now - this.lastUpdatePositionMs;
    this.lastUpdatePositionMs = now;
  }

  @Override
  public void tick(List<WorldObject> elementList) {
    // у удаленного юзера нет физик

    long nowMs = System.currentTimeMillis();
    long leftTimeMs = nowMs - this.lastUpdatePositionMs;
    double progress = Math.min(1, leftTimeMs / (double) this.updateLatencyMs);

    if (this.interpolation) {
      this.x = (int) (this.prevX + (this.nextX - this.prevX) * progress);
      this.y = (int) (this.prevY + (this.nextY - this.prevY) * progress);
    }
  }
}
