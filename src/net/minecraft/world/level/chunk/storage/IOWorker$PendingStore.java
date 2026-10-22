package net.minecraft.world.level.chunk.storage;

import java.util.concurrent.CompletableFuture;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.Nullable;

class IOWorker$PendingStore {
   @Nullable
   private CompoundTag data;
   private final CompletableFuture<Void> result = new CompletableFuture<>();

   public IOWorker$PendingStore(@Nullable final CompoundTag data) {
      this.data = data;
   }

   @Nullable
   private CompoundTag copyData() {
      CompoundTag data = this.data;
      return data == null ? null : data.copy();
   }
}
