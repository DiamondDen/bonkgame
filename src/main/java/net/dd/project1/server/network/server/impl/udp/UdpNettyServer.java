package net.dd.project1.server.network.server.impl.udp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.RequiredArgsConstructor;
import net.dd.project1.server.network.server.NetworkServer;
import net.dd.project1.server.network.server.connection.ServerConnectionHandler;

@RequiredArgsConstructor
public class UdpNettyServer implements NetworkServer {

  private final ServerConnectionHandler connectionHandler;

  @Override
  public void start(int port) {
    EventLoopGroup oneGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);

    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(oneGroup);
      bootstrap.channelFactory(() -> new CustomServerChannel(Epoll.isAvailable() ? EpollDatagramChannel::new : NioDatagramChannel::new));
      bootstrap.childHandler(this.connectionHandler);
      System.out.println("Starting server on " + port);
      bootstrap.bind(port).sync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
