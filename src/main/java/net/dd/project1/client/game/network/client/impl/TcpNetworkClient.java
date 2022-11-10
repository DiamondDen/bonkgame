package net.dd.project1.client.game.network.client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.network.client.NetworkClient;
import net.dd.project1.client.game.network.client.connection.ClientConnectionHandler;

@RequiredArgsConstructor
public class TcpNetworkClient implements NetworkClient {

  private final ClientConnectionHandler connectionHandler;
  @Override
  public void connect(String host, int port) {
    System.out.println("Connect to " + host + ":" + port);
    EventLoopGroup workGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
    Bootstrap bootstrap = new Bootstrap()
            .group(workGroup)
            .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(this.connectionHandler);
    try {
      bootstrap.connect(host, port).sync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
