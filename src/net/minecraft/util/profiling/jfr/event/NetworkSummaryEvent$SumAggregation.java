package net.minecraft.util.profiling.jfr.event;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class NetworkSummaryEvent$SumAggregation {
   private final AtomicLong sentBytes = new AtomicLong();
   private final AtomicInteger sentPackets = new AtomicInteger();
   private final AtomicLong receivedBytes = new AtomicLong();
   private final AtomicInteger receivedPackets = new AtomicInteger();
   private final NetworkSummaryEvent event;

   public NetworkSummaryEvent$SumAggregation(final String remoteAddress) {
      this.event = new NetworkSummaryEvent(remoteAddress);
      this.event.begin();
   }

   public void trackSentPacket(final int size) {
      this.sentPackets.incrementAndGet();
      this.sentBytes.addAndGet(size);
   }

   public void trackReceivedPacket(final int size) {
      this.receivedPackets.incrementAndGet();
      this.receivedBytes.addAndGet(size);
   }

   public void commitEvent() {
      this.event.sentBytes = this.sentBytes.get();
      this.event.sentPackets = this.sentPackets.get();
      this.event.receivedBytes = this.receivedBytes.get();
      this.event.receivedPackets = this.receivedPackets.get();
      this.event.commit();
   }
}
