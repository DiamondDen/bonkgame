package net.dd.project1.shared.network.pipeline;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.RequiredArgsConstructor;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.PacketManager;

import java.util.List;

@RequiredArgsConstructor
public class PacketEncoder extends MessageToMessageEncoder<Packet> {
  private final PacketManager manager;
  private final Gson gson;

  @Override
  protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
    String name = this.manager.getPacketNameByClass(msg.getClass());

    if (name == null) {
      throw new RuntimeException("Unknown packet " + msg.getClass().getName());
    }

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", name);
    jsonObject.add("data", this.gson.toJsonTree(msg));

    out.add(jsonObject.toString());
  }
}
