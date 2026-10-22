package net.minecraft.util.profiling.jfr.event;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.Period;
import jdk.jfr.StackTrace;

@Name("minecraft.NetworkSummary")
@Label("Network Summary")
@Category({"Minecraft", "Network"})
@StackTrace(false)
@Period("10 s")
public class NetworkSummaryEvent extends Event {
   public static final String EVENT_NAME = "minecraft.NetworkSummary";
   public static final EventType TYPE = EventType.getEventType(NetworkSummaryEvent.class);
   @Name("remoteAddress")
   @Label("Remote Address")
   public final String remoteAddress;
   @Name("sentBytes")
   @Label("Sent Bytes")
   @DataAmount
   public long sentBytes;
   @Name("sentPackets")
   @Label("Sent Packets")
   public int sentPackets;
   @Name("receivedBytes")
   @Label("Received Bytes")
   @DataAmount
   public long receivedBytes;
   @Name("receivedPackets")
   @Label("Received Packets")
   public int receivedPackets;

   public NetworkSummaryEvent(final String remoteAddress) {
      this.remoteAddress = remoteAddress;
   }
}
