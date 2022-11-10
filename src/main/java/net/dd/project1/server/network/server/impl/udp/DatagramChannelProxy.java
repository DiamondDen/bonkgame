package net.dd.project1.server.network.server.impl.udp;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.function.Supplier;

public class DatagramChannelProxy implements Channel {

  public static final String LISTENER_HANDLER_NAME = "udp-listener-handler";

  protected final DatagramChannel listener;
  protected final DefaultChannelPipeline pipeline;

  public DatagramChannelProxy(Supplier<? extends DatagramChannel> ioChannelSupplier) {
    this.listener = ioChannelSupplier.get();
    this.pipeline = this.newChannelPipeline();

  }

  protected DefaultChannelPipeline newChannelPipeline() {
    return new DefaultChannelPipeline(this) {
      @Override
      protected void onUnhandledInboundException(Throwable cause) {
        if (cause instanceof ClosedChannelException) {
          ReferenceCountUtil.safeRelease(cause);
          return;
        }
        super.onUnhandledInboundException(cause);
      }
    };
  }

  @Override
  public ChannelConfig config() {
    return this.listener.config();
  }

  @Override
  public SocketAddress localAddress() {
    return listener.localAddress();
  }

  @Override
  public SocketAddress remoteAddress() {
    return listener.remoteAddress();
  }

  @Override
  public int compareTo(Channel o) {
    return listener.compareTo(o);
  }

  @Override
  public ChannelId id() {
    return listener.id();
  }

  @Override
  public EventLoop eventLoop() {
    return listener.eventLoop();
  }

  @Override
  public Channel parent() {
    return listener.parent();
  }

  @Override
  public boolean isOpen() {
    return listener.isOpen();
  }

  @Override
  public boolean isRegistered() {
    return listener.isRegistered();
  }

  @Override
  public boolean isActive() {
    return listener.isActive();
  }

  @Override
  public ChannelMetadata metadata() {
    return listener.metadata();
  }

  @Override
  public ChannelFuture closeFuture() {
    return listener.closeFuture();
  }

  @Override
  public boolean isWritable() {
    return listener.isWritable();
  }

  @Override
  public long bytesBeforeUnwritable() {
    return listener.bytesBeforeUnwritable();
  }

  @Override
  public long bytesBeforeWritable() {
    return listener.bytesBeforeWritable();
  }

  @Override
  public Unsafe unsafe() {
    return listener.unsafe();
  }

  @Override
  public ChannelPipeline pipeline() {
    return listener.pipeline();
  }

  @Override
  public ByteBufAllocator alloc() {
    return listener.alloc();
  }

  @Override
  public Channel read() {
    return listener.read();
  }

  @Override
  public Channel flush() {
    return listener.flush();
  }

  @Override
  public <T> Attribute<T> attr(AttributeKey<T> key) {
    return listener.attr(key);
  }

  @Override
  public <T> boolean hasAttr(AttributeKey<T> key) {
    return listener.hasAttr(key);
  }

  @Override
  public ChannelFuture bind(SocketAddress localAddress) {
    return pipeline.bind(localAddress);
  }

  @Override
  public ChannelFuture connect(SocketAddress remoteAddress) {
    return pipeline.connect(remoteAddress);
  }

  @Override
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
    return pipeline.connect(remoteAddress, localAddress);
  }

  @Override
  public ChannelFuture disconnect() {
    return pipeline.disconnect();
  }

  @Override
  public ChannelFuture close() {
    return pipeline.close();
  }

  @Override
  public ChannelFuture deregister() {
    return pipeline.deregister();
  }

  @Override
  public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return pipeline.bind(localAddress, promise);
  }

  @Override
  public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
    return pipeline.connect(remoteAddress, promise);
  }

  @Override
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
    return pipeline.connect(remoteAddress, localAddress, promise);
  }

  @Override
  public ChannelFuture disconnect(ChannelPromise promise) {
    return pipeline.disconnect(promise);
  }

  @Override
  public ChannelFuture close(ChannelPromise promise) {
    return pipeline.close(promise);
  }

  @Override
  public ChannelFuture deregister(ChannelPromise promise) {
    return pipeline.deregister(promise);
  }

  @Override
  public ChannelFuture write(Object msg) {
    return pipeline.write(msg);
  }

  @Override
  public ChannelFuture write(Object msg, ChannelPromise promise) {
    return pipeline.write(msg, promise);
  }

  @Override
  public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
    return pipeline.writeAndFlush(msg, promise);
  }

  @Override
  public ChannelFuture writeAndFlush(Object msg) {
    return pipeline.writeAndFlush(msg);
  }

  @Override
  public ChannelPromise newPromise() {
    return pipeline.newPromise();
  }

  @Override
  public ChannelProgressivePromise newProgressivePromise() {
    return pipeline.newProgressivePromise();
  }

  @Override
  public ChannelFuture newSucceededFuture() {
    return pipeline.newSucceededFuture();
  }

  @Override
  public ChannelFuture newFailedFuture(Throwable cause) {
    return pipeline.newFailedFuture(cause);
  }

  @Override
  public ChannelPromise voidPromise() {
    return pipeline.voidPromise();
  }


}
