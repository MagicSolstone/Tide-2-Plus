package net.minecraft.world.entity;

public enum Entity$RemovalReason {
   KILLED(true, false),
   DISCARDED(true, false),
   UNLOADED_TO_CHUNK(false, true),
   UNLOADED_WITH_PLAYER(false, false),
   CHANGED_DIMENSION(false, false);

   private final boolean destroy;
   private final boolean save;

   private Entity$RemovalReason(final boolean destroy, final boolean save) {
      this.destroy = destroy;
      this.save = save;
   }

   public boolean shouldDestroy() {
      return this.destroy;
   }

   public boolean shouldSave() {
      return this.save;
   }
}
