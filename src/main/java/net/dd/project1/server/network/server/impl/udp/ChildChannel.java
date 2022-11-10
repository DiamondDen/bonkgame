package net.dd.project1.server.network.server.impl.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;

public class ChildChannel extends AbstractChannel {

  private static final ChannelMetadata metadata = new ChannelMetadata(false);
  protected final ChannelPromise connectPromise;
  protected final InetSocketAddress remoteAddress;
  protected volatile boolean open = true;

  public ChildChannel(Channel parent, InetSocketAddress remoteAddress) {
    super(parent);
    connectPromise = newPromise();
    this.remoteAddress = remoteAddress;
  }


  @Override
  protected AbstractUnsafe newUnsafe() {
    return new AbstractUnsafe() {
      public void connect(SocketAddress addr1, SocketAddress addr2, ChannelPromise pr) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  protected boolean isCompatible(EventLoop loop) {
    return true;
  }

  @Override
  protected SocketAddress localAddress0() {
    return parent().localAddress();
  }

  @Override
  protected SocketAddress remoteAddress0() {
    return this.remoteAddress;
  }

  @Override
  protected void doBind(SocketAddress localAddress) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void doDisconnect() throws Exception {
    close();
  }

  @Override
  protected void doClose() throws Exception {
    open = false;
  }

  @Override
  protected void doBeginRead() throws Exception {

  }

  @Override
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public ChannelConfig config() {
    return this.parent().config();
  }

  @Override
  public boolean isOpen() {
    return this.open;
  }

  @Override
  public boolean isActive() {
    return isOpen() && parent().isActive() && connectPromise.isSuccess();
  }

  @Override
  public ChannelMetadata metadata() {
    return metadata;
  }

  public static final ChannelFutureListener INTERNAL_WRITE_LISTENER = future -> {
    if (!future.isSuccess() && !(future.cause() instanceof ClosedChannelException)) {
      future.channel().pipeline().fireExceptionCaught(future.cause());
      future.channel().close();
    }
  };

  protected class WriteHandler extends ChannelOutboundHandlerAdapter {
    protected boolean needsFlush = false;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
      if (msg instanceof ByteBuf) {
        needsFlush = true;
        promise.trySuccess();
        parent().write(new DatagramPacket((ByteBuf) msg, remoteAddress))
                .addListener(INTERNAL_WRITE_LISTENER);
      } else {
        ctx.write(msg, promise);
      }
    }

    @Override
    public void flush(ChannelHandlerContext ctx) {
      if (needsFlush) {
        needsFlush = false;
        parent().flush();
      }
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
      // NOOP
    }
  }
}
