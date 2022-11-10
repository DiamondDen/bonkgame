package net.dd.project1.server.network.server.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import net.dd.project1.server.network.server.NetworkServer;
import net.dd.project1.server.network.server.connection.ServerConnectionHandler;

@RequiredArgsConstructor
public class TcpNettyServer implements NetworkServer {
  private final ServerConnectionHandler connectionHandler;

  @Override
  public void start(int port) {
    EventLoopGroup oneGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(oneGroup, oneGroup)
            .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true);
    bootstrap.childHandler(this.connectionHandler);
    try {
      System.out.println("Starting server on " + port);
      bootstrap.bind(port).sync();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
