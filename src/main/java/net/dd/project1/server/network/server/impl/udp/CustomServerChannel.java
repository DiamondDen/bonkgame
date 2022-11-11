package net.dd.project1.server.network.server.impl.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import net.dd.project1.shared.network.udp.DatagramChannelProxy;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomServerChannel extends DatagramChannelProxy implements ServerChannel {
  public CustomServerChannel(Supplier<? extends DatagramChannel> ioChannelSupplier) {
    super(ioChannelSupplier);
    this.addDefaultPipeline();
  }

  protected final Map<SocketAddress, Channel> childMap = new HashMap<>();

  public Channel createNewChild(InetSocketAddress remoteAddress) {
    Channel child = new ChildChannel(this, remoteAddress);

    child.closeFuture().addListener(v -> eventLoop().execute(() -> removeChild(remoteAddress, child)));
    childMap.put(remoteAddress, child);

    pipeline()
            .fireChannelRead(child)
            .fireChannelReadComplete(); //register
    return child;
  }

  public Channel getOrCreateNewChild(InetSocketAddress remoteAddress) {
    Channel channel = this.childMap.get(remoteAddress);
    if (channel == null) {
      return this.createNewChild(remoteAddress);
    }
    return channel;
  }

  protected void removeChild(SocketAddress remoteAddress, Channel child) {
    childMap.remove(remoteAddress, child);
  }

  public void fireContent(Channel channel, DatagramPacket datagramPacket) {
    channel.eventLoop().execute(() -> {
      ByteBuf byteBuf = datagramPacket.content();
      try {
        channel.pipeline()
                .fireChannelRead(byteBuf.retain())
                .fireChannelReadComplete();
      } finally {
        byteBuf.release();
      }
    });
  }

  public void addDefaultPipeline() {
    this.pipeline().addLast(new ChannelDuplexHandler() {

      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DatagramPacket) {
          final DatagramPacket datagram = (DatagramPacket) msg;
          try {
            Channel child = getOrCreateNewChild(datagram.sender());
            fireContent(child, datagram.retain());
          } finally {
            datagram.release();
          }
        } else {
          ctx.fireChannelRead(msg);
        }
      }
    });
  }


}
