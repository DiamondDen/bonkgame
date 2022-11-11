package net.dd.project1.server.network.server.connection;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import net.dd.project1.server.GameServer;
import net.dd.project1.server.network.server.NetworkClient;
import net.dd.project1.server.network.server.handler.ServerLoginListener;
import net.dd.project1.shared.network.PacketManager;
import net.dd.project1.shared.network.pipeline.FrameDecoder;
import net.dd.project1.shared.network.pipeline.FrameEncoder;
import net.dd.project1.shared.network.pipeline.PacketDecoder;
import net.dd.project1.shared.network.pipeline.PacketEncoder;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class ServerConnectionHandler extends ChannelInitializer<Channel> {

  private final Gson gson = new Gson();
  private final GameServer server;
  private final PacketManager packetManager;
  private final AtomicInteger id = new AtomicInteger();

  @Override
  protected void initChannel(Channel ch) throws Exception {
    System.out.println("New connection from " + ch.remoteAddress().toString());
    NetworkClient networkClient = new NetworkClient(server, id.incrementAndGet(), ch);

    this.server.addUntrustedConnection(networkClient);

    ch.pipeline()
            .addLast(new FrameEncoder())
            .addLast(new FrameDecoder())
            .addLast(new StringEncoder())
            .addLast(new StringDecoder())
            .addLast(new ReadTimeoutHandler(30))
            .addLast(new PacketEncoder(this.packetManager, this.gson))
            .addLast(new PacketDecoder(this.packetManager, this.gson))
            .addLast(networkClient);
  }
}
