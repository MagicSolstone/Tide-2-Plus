package net.minecraft.world.inventory;

import net.minecraft.network.HashedPatchMap$HashGenerator;
import net.minecraft.network.HashedStack;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class RemoteSlot$Synchronized implements RemoteSlot {
   private final HashedPatchMap$HashGenerator hasher;
   @Nullable
   private ItemStack remoteStack = null;
   @Nullable
   private HashedStack remoteHash = null;

   public RemoteSlot$Synchronized(final HashedPatchMap$HashGenerator hasher) {
      this.hasher = hasher;
   }

   @Override
   public void force(final ItemStack outgoing) {
      this.remoteStack = outgoing.copy();
      this.remoteHash = null;
   }

   @Override
   public void receive(final HashedStack incoming) {
      this.remoteStack = null;
      this.remoteHash = incoming;
   }

   @Override
   public boolean matches(final ItemStack local) {
      if (this.remoteStack != null) {
         return ItemStack.matches(this.remoteStack, local);
      } else if (this.remoteHash != null && this.remoteHash.matches(local, this.hasher)) {
         this.remoteStack = local.copy();
         return true;
      } else {
         return false;
      }
   }

   public void copyFrom(final RemoteSlot$Synchronized other) {
      this.remoteStack = other.remoteStack;
      this.remoteHash = other.remoteHash;
   }
}
