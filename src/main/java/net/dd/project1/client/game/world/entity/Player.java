package net.dd.project1.client.game.world.entity;

public class Player extends LivingEntity {

  public Player(int x, int y, int size) {
    this.setPosition(x, y);
    this.setSize(size, size);
    this.setHealth(20);
  }

}
