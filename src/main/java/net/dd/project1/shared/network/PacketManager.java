package net.dd.project1.shared.network;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {

  private final Map<String, Class<? extends Packet>> packetByNameMap = new HashMap<>();
  private final Map<Class<? extends Packet>, String> nameByPacketMap = new HashMap<>();

  public void registerPacket(String name, Class<? extends Packet> clazz) {
    this.packetByNameMap.put(name, clazz);
    this.nameByPacketMap.put(clazz, name);
  }

  public String getPacketNameByClass(Class<? extends Packet> packetClass) {
    return this.nameByPacketMap.get(packetClass);
  }

  public Class<? extends Packet> getClassByName(String packetName) {
    return this.packetByNameMap.get(packetName);
  }

}
