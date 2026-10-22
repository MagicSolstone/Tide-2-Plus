package net.minecraft.server.commands;

enum CloneCommands$Mode {
   FORCE(true),
   MOVE(true),
   NORMAL(false);

   private final boolean canOverlap;

   private CloneCommands$Mode(final boolean canOverlap) {
      this.canOverlap = canOverlap;
   }

   public boolean canOverlap() {
      return this.canOverlap;
   }
}
