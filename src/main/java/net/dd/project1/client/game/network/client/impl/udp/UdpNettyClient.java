package net.dd.project1.client.game.network.client.impl.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.RequiredArgsConstructor;
import net.dd.project1.client.game.network.client.NetworkClient;
import net.dd.project1.client.game.network.client.connection.ClientConnectionHandler;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class UdpNettyClient implements NetworkClient {

  private final ClientConnectionHandler connectionHandler;

  @Override
  public void connect(String host, int port) {
    System.out.println("Connect to " + host + ":" + port);
    EventLoopGroup workGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
    Bootstrap bootstrap = new Bootstrap()
            .group(workGroup)
            .channelFactory(() -> new ClientChannel(() -> Epoll.isAvailable() ? new EpollDatagramChannel() : new NioDatagramChannel()))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_REUSEADDR, true)
            .handler(this.connectionHandler);
    try {
      bootstrap.connect(new InetSocketAddress(host, port)).sync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
