package net.dd.project1.shared.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {

  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    if (!ctx.channel().isActive()) {
      in.clear();
    } else if (in.isReadable(4)) {
      in.markReaderIndex();
      int length = in.readInt();
      if (in.isReadable(length)) {
        out.add(in.readBytes(length));
      } else {
        in.resetReaderIndex();
      }
    }
  }
}
