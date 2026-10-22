package net.minecraft.network;

import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;
import net.minecraft.network.protocol.Packet;
import org.slf4j.Logger;

public class PacketProcessor implements AutoCloseable {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Queue<PacketProcessor$ListenerAndPacket<?>> packetsToBeHandled = Queues.newConcurrentLinkedQueue();
   private final Thread runningThread;
   private boolean closed;

   public PacketProcessor(final Thread runningThread) {
      this.runningThread = runningThread;
   }

   public boolean isSameThread() {
      return Thread.currentThread() == this.runningThread;
   }

   public <T extends PacketListener> void scheduleIfPossible(final T listener, final Packet<T> packet) {
      if (this.closed) {
         throw new RejectedExecutionException("Server already shutting down");
      } else {
         this.packetsToBeHandled.add(new PacketProcessor$ListenerAndPacket<>(listener, packet));
      }
   }

   public void processQueuedPackets() {
      if (!this.closed) {
         while (!this.packetsToBeHandled.isEmpty()) {
            this.packetsToBeHandled.poll().handle();
         }
      }
   }

   @Override
   public void close() {
      this.closed = true;
   }
}
