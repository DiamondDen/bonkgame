package net.dd.project1.client.game.network.client.impl.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import net.dd.project1.shared.network.udp.DatagramChannelProxy;

import java.util.function.Supplier;

public class ClientChannel extends DatagramChannelProxy {
  public ClientChannel(Supplier<? extends DatagramChannel> ioChannelSupplier) {
    super(ioChannelSupplier);
    addDefaultPipeline();
  }

  public void addDefaultPipeline() {
    pipeline().addLast(new ClientHandler());
  }

  protected class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      if (msg instanceof DatagramPacket) {
        try {
          final DatagramPacket datagram = (DatagramPacket) msg;
          if (datagram.sender() == null || datagram.sender().equals(remoteAddress())) {
            ctx.fireChannelRead(datagram.content().retain());
          }
        } finally {
          ReferenceCountUtil.release(msg);
        }
      } else {
        ctx.fireChannelRead(msg);
      }
    }
  }
}
