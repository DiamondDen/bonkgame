package net.dd.project1.client.game.network.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dd.project1.client.game.Game;
import net.dd.project1.client.game.network.client.connection.ClientConnectionHandler;
import net.dd.project1.client.game.network.client.impl.TcpNetworkClient;
import net.dd.project1.shared.network.GamePacketManager;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.handler.PacketHandler;
import net.dd.project1.shared.network.packets.KeepAlive;
import net.dd.project1.shared.network.packets.LoginPacket;

import java.util.Random;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NetworkManager extends SimpleChannelInboundHandler<Packet> {

  private final Game game;

  @Setter
  private int id;

  private final NetworkClient networkConnection;

  private final GamePacketManager packetManager;
  private Channel channel;

  @Setter
  private PacketHandler handler;

  public NetworkManager(Game game) {
    this.game = game;

    this.packetManager = new GamePacketManager();
    this.packetManager.registerAllPackets();

    ClientConnectionHandler connectionHandler = new ClientConnectionHandler(this, this.packetManager);

    this.networkConnection = new TcpNetworkClient(connectionHandler);
  }

  public void update() {
    if (this.handler != null) {
      this.handler.tick();
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
    if (msg instanceof KeepAlive) {
      this.sendPacket(msg);
      return;
    }
    msg.handle(this.handler);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);

    this.channel = ctx.channel();
    this.sendPacket(new LoginPacket(UUID.randomUUID(), "Player" + new Random().nextInt(10000), 0));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
  }

  public void connect(String host, int port) {
    networkConnection.connect(host, port);
  }

  public void sendPacket(Packet message) {
    EventLoop eventLoop = this.channel.eventLoop();
    if (eventLoop.inEventLoop()) {
      this.channel.writeAndFlush(message);
    } else {
      eventLoop.execute(() -> {
        this.channel.writeAndFlush(message);
      });
    }
  }
}
