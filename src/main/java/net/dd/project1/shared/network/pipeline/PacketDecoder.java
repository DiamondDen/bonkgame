package net.dd.project1.shared.network.pipeline;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.RequiredArgsConstructor;
import net.dd.project1.shared.network.Packet;
import net.dd.project1.shared.network.PacketManager;

import java.util.List;

@RequiredArgsConstructor
public class PacketDecoder extends MessageToMessageDecoder<String> {
  private final PacketManager manager;
  private final Gson gson;

  @Override
  protected void decode(ChannelHandlerContext ctx, String message, List<Object> out) throws Exception {
    try {
      JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
      String packetName = jsonObject.get("name").getAsString();

      Class<? extends Packet> clazz = this.manager.getClassByName(packetName);
      if (clazz == null) {
        System.err.println("Unknown packet " + packetName);
        return;
      }

      out.add(gson.fromJson(jsonObject.get("data"), clazz));
    } catch (Exception e) {
      System.err.println(message);
      e.printStackTrace();
    }
  }
}
