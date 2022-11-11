package net.dd.project1.client.game;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import net.dd.project1.client.display.opengl.GameDisplay;
import net.dd.project1.client.game.world.WorldObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class Game {

  protected final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(runnable -> {
    Thread thread = new Thread(runnable, "Client Thread");
    thread.setDaemon(true);
    return thread;
  });

  protected final GameDisplay gameDisplay;

  protected final List<WorldObject> worldObjectList = Collections.synchronizedList(new ArrayList<>());

  private final IntSet pressedKeysSet = new IntOpenHashSet();

  public Game() {
    this.gameDisplay = new GameDisplay(this.worldObjectList, this.getWindowTitle());
    this.gameDisplay.setKeyHandler((keyCode, action) -> {
      if (action == 0) {
        this.pressedKeysSet.remove(keyCode);
      } else if (action == 1) {
        this.pressedKeysSet.add(keyCode);
      }
    });
  }

  abstract String getWindowTitle();

  public void addWorldObject(WorldObject worldObject) {
    this.worldObjectList.add(worldObject);
  }

  public void startTimer() {
    this.service.scheduleAtFixedRate(() -> {
      try {
        this.tick();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }, 16, 16, TimeUnit.MILLISECONDS);
  }

  protected void start() {
    this.gameDisplay.run();
  }

  public void tick() {
    try {
      this.worldObjectList.removeIf(worldObject -> {
        worldObject.tick(this.worldObjectList);
        return worldObject.isDied();
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
