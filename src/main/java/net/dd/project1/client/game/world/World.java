package net.dd.project1.client.game.world;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.game.world.entity.Entity;
import net.dd.project1.client.game.world.entity.tower.TowerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class World {

  @Getter
  private final int width, height;

  protected final List<WorldObject> worldObjectList = Collections.synchronizedList(new ArrayList<>());

  // Переделать на один поток когда-нибудь..
  protected final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  public long nowMs() {
    return System.currentTimeMillis();
  }

  public void addWorldObject(WorldObject worldObject) {
    this.readWriteLock.writeLock().lock();
    try {
      worldObject.setWorld(this);
      this.worldObjectList.add(worldObject);
    } finally {
      this.readWriteLock.writeLock().unlock();
    }
  }

  public void tick() {
    try {
      this.readWriteLock.writeLock().lock();
      try {
        for (int i = 0; i < this.worldObjectList.size(); i++) {
          WorldObject worldObject = this.worldObjectList.get(i);
          worldObject.tick(this.worldObjectList);
          if (worldObject.isDied()) {
            this.worldObjectList.remove(i--);
          }
        }
      } finally {
        this.readWriteLock.writeLock().unlock();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public <T extends Entity> T findCollisionEntity(Class<T> clazz, WorldObject source, WorldObject... without) {
    T target = findNearEntity(clazz, source, without);
    if (target != null && target.distance(source) < target.getWidth() * 0.7f) {
      return target;
    }
    return null;
  }

  public <T extends Entity> T findNearEntity(Class<T> clazz, WorldObject source, WorldObject... without) {
    double distance = 9999;
    T value = null;

    l0:
    for (WorldObject worldObject : this.worldObjectList) {
      for (WorldObject object : without)
        if (object == worldObject) continue l0;
      if (clazz.isAssignableFrom(worldObject.getClass())) {
        double lDistance = worldObject.distance(source);
        if (lDistance < distance) {
          distance = lDistance;
          value = (T) worldObject;
        }
      }
    }

    return value;
  }

  public void render(DrawHelper drawHelper) {
    this.readWriteLock.readLock().lock();
    try {
      for (WorldObject worldObject : this.worldObjectList) {
        worldObject.draw(drawHelper);
      }
    } finally {
      this.readWriteLock.readLock().unlock();
    }
  }

}
