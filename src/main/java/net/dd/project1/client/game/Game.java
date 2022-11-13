package net.dd.project1.client.game;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import net.dd.project1.client.display.DrawColor;
import net.dd.project1.client.display.DrawHelper;
import net.dd.project1.client.display.opengl.GameDisplay;
import net.dd.project1.client.game.world.World;
import net.dd.project1.client.game.world.entity.Player;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
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

  public Player player;

  protected World world;

  private final IntSet pressedKeysSet = new IntOpenHashSet();

  public Game() {
    this.gameDisplay = new GameDisplay(this, this.getWindowTitle());
    this.gameDisplay.setKeyHandler((keyCode, action) -> {
      if (action == 0) {
        this.pressedKeysSet.remove(keyCode);
      } else if (action == 1) {
        this.pressedKeysSet.add(keyCode);
      }
    });
  }

  abstract String getWindowTitle();

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

  public void render(DrawHelper drawHelper) {
    int chunkX = (this.player.getX() + this.player.getWidth() / 2) / this.gameDisplay.getWidth();
    drawHelper.fillRect(
            0, 0,
            this.gameDisplay.getWidth(), this.gameDisplay.getHeight(),
            DrawColor.of(102, 178, 255)
    );
    GL11.glTranslated(-chunkX * this.gameDisplay.getHeight(), 0, 0);
    this.world.render(drawHelper);
    GL11.glTranslated(chunkX * this.gameDisplay.getHeight(), 0, 0);
  }

  public void tick() {
    try {
      this.world.tick();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Произошла ошибка: " + e.getMessage());
      System.exit(1);
    }
  }
}
