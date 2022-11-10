package net.dd.project1.client.game.network.client.connection;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.network.client.NetworkManager;
import net.dd.project1.shared.network.PacketManager;
import net.dd.project1.shared.network.pipeline.FrameDecoder;
import net.dd.project1.shared.network.pipeline.FrameEncoder;
import net.dd.project1.shared.network.pipeline.PacketDecoder;
import net.dd.project1.shared.network.pipeline.PacketEncoder;

@RequiredArgsConstructor
public class ClientConnectionHandler extends ChannelInitializer<Channel> {

  private final Gson gson = new Gson();
  private final NetworkManager networkManager;
  private final PacketManager packetManager;

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ch.pipeline()
            .addLast(new FrameEncoder())
            .addLast(new FrameDecoder())
            .addLast(new StringEncoder())
            .addLast(new StringDecoder())
            .addLast(new ReadTimeoutHandler(30))
            .addLast(new PacketEncoder(this.packetManager, this.gson))
            .addLast(new PacketDecoder(this.packetManager, this.gson))
            .addLast(networkManager);
  }
}

