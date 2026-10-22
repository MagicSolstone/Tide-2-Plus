package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record NeighborsUpdateRenderer$LastUpdate(int count, int age) {
   private static final NeighborsUpdateRenderer$LastUpdate NONE = new NeighborsUpdateRenderer$LastUpdate(0, Integer.MAX_VALUE);

   public NeighborsUpdateRenderer$LastUpdate tryCount(final int age) {
      if (age == this.age) {
         return new NeighborsUpdateRenderer$LastUpdate(this.count + 1, age);
      } else {
         return age < this.age ? new NeighborsUpdateRenderer$LastUpdate(1, age) : this;
      }
   }
}
