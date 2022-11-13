package net.dd.project1.client.game.world.entity;

import net.dd.project1.client.game.math.Vector2f;
import net.dd.project1.client.game.world.WorldObject;

import java.util.List;

public class WorldRemotePlayer extends Player {

  public static final boolean INTERPOLATION = true;

  private long lastUpdatePositionMs;
  private long updateLatencyMs;

  private int prevX, prevY;
  private int nextX, nextY;
  private long moveSequenceNumber;

  public WorldRemotePlayer(int x, int y, int size) {
    super(x, y, size);

    this.prevX = x;
    this.prevY = y;

    this.updatePosition(x, y, moveSequenceNumber, Vector2f.NUL);
  }


  public void updatePosition(int x, int y, long sequenceNumber, Vector2f motion) {
    if (this.moveSequenceNumber > sequenceNumber) {
      return;
    }
    this.moveSequenceNumber = sequenceNumber;

    if (!INTERPOLATION) {
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

    if (INTERPOLATION) {
      this.x = (int) (this.prevX + (this.nextX - this.prevX) * progress);
      this.y = (int) (this.prevY + (this.nextY - this.prevY) * progress);
    }
  }
}
