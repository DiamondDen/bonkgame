package net.dd.project1.server.network.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dd.project1.server.GameServer;
import net.dd.project1.server.world.RemotePlayer;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;
import net.dd.project1.shared.network.packets.KeepAlive;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class NetworkClient extends SimpleChannelInboundHandler<Packet> {

  private final GameServer server;
  private final int id;

  private UUID uuid;
  private String username;
  private final Channel channel;

  private AtomicBoolean disconnected = new AtomicBoolean();

  private PacketHandler packetHandler;

  private final RemotePlayer player;

  public NetworkClient(GameServer server, int id, Channel channel) {
    this.server = server;
    this.id = id;
    this.channel = channel;
    this.player = new RemotePlayer(this.id, this.server, this);
  }

  public void sendPacket(Packet packet) {
    EventLoop eventLoop = this.channel.eventLoop();
    if (eventLoop.inEventLoop()) {
      this.channel.writeAndFlush(packet);
    } else {
      eventLoop.execute(() -> {
        this.channel.writeAndFlush(packet);
      });
    }
  }

  public void tick() {
    this.packetHandler.tick();
    this.player.tick();
  }

  public boolean isDisconnected() {
    return this.disconnected.get();
  }

  public void onDisconnect() {
    if (!this.disconnected.compareAndSet(false, true)) return;

    this.packetHandler.onDisconnect();
    System.out.println("Player with id " + this.id + "(" + this.username + ") disconnected");
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
    if (msg instanceof KeepAlive) {
      this.player.setPing(System.currentTimeMillis() - ((KeepAlive) msg).getTime());
      return;
    }

    msg.handle(this.packetHandler);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (!(cause instanceof ReadTimeoutException)) {
      cause.printStackTrace();
    }
    ctx.close();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    this.onDisconnect();
  }
}
