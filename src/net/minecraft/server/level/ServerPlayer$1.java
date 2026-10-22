package net.minecraft.server.level;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.RemoteSlot;
import net.minecraft.world.inventory.RemoteSlot$Synchronized;
import net.minecraft.world.item.ItemStack;

class ServerPlayer$1 implements ContainerSynchronizer {
   private final LoadingCache<TypedDataComponent<?>, Integer> cache;

   ServerPlayer$1(final ServerPlayer this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.cache = CacheBuilder.newBuilder().maximumSize(256L).build(new ServerPlayer$1$1(this));
   }

   @Override
   public void sendInitialData(final AbstractContainerMenu container, final List<ItemStack> slotItems, final ItemStack carriedItem, final int[] dataSlots) {
      this.this$0.connection.send(new ClientboundContainerSetContentPacket(container.containerId, container.incrementStateId(), slotItems, carriedItem));

      for (int slot = 0; slot < dataSlots.length; slot++) {
         this.broadcastDataValue(container, slot, dataSlots[slot]);
      }
   }

   @Override
   public void sendSlotChange(final AbstractContainerMenu container, final int slotIndex, final ItemStack itemStack) {
      this.this$0.connection.send(new ClientboundContainerSetSlotPacket(container.containerId, container.incrementStateId(), slotIndex, itemStack));
   }

   @Override
   public void sendCarriedChange(final AbstractContainerMenu container, final ItemStack itemStack) {
      this.this$0.connection.send(new ClientboundSetCursorItemPacket(itemStack));
   }

   @Override
   public void sendDataChange(final AbstractContainerMenu container, final int id, final int value) {
      this.broadcastDataValue(container, id, value);
   }

   private void broadcastDataValue(final AbstractContainerMenu container, final int id, final int value) {
      this.this$0.connection.send(new ClientboundContainerSetDataPacket(container.containerId, id, value));
   }

   @Override
   public RemoteSlot createSlot() {
      return new RemoteSlot$Synchronized(this.cache::getUnchecked);
   }
}
