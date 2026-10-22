package net.minecraft.world.entity;

public enum Entity$MovementEmission {
   NONE(false, false),
   SOUNDS(true, false),
   EVENTS(false, true),
   ALL(true, true);

   private final boolean sounds;
   private final boolean events;

   private Entity$MovementEmission(final boolean sounds, final boolean events) {
      this.sounds = sounds;
      this.events = events;
   }

   public boolean emitsAnything() {
      return this.events || this.sounds;
   }

   public boolean emitsEvents() {
      return this.events;
   }

   public boolean emitsSounds() {
      return this.sounds;
   }
}
