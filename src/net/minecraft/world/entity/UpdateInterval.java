package net.minecraft.world.entity;

public interface UpdateInterval {
   UpdateInterval NEVER = new UpdateInterval$1();

   boolean test(int tick);

   int nextInterval(int tick);

   static UpdateInterval periodic(final int period) {
      return new UpdateInterval$2(period);
   }
}
