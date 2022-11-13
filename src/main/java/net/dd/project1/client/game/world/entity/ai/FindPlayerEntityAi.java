package net.dd.project1.client.game.world.entity.ai;

import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.world.World;
import net.dd.project1.client.game.world.entity.LivingEntity;
import net.dd.project1.client.game.world.entity.Player;

@RequiredArgsConstructor
public class FindPlayerEntityAi implements EntityAi {
  private final LivingEntity entity;

  private long lastCheckMs;

  @Override
  public void tick() {
    World world = this.entity.getWorld();
    if (world.nowMs() - this.lastCheckMs > 500) {
      this.lastCheckMs = world.nowMs();

      Player target = world.findNearEntity(Player.class, this.entity);
      if (target != null) {
        this.entity.setTargetEntity(target, 5000L);
      }
    }
  }
}
