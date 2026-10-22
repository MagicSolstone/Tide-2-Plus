package net.minecraft.world.entity.animal.feline;

public enum CatSoundVariants$SoundSet {
   CLASSIC("classic", "cat"),
   ROYAL("royal", "cat_royal");

   private final String identifier;
   private final String soundEventIdentifier;

   private CatSoundVariants$SoundSet(final String identifier, final String soundEventIdentifier) {
      this.identifier = identifier;
      this.soundEventIdentifier = soundEventIdentifier;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public String getSoundEventIdentifier() {
      return this.soundEventIdentifier;
   }
}
