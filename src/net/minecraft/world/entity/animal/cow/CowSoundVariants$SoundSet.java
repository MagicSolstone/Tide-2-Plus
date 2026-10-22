package net.minecraft.world.entity.animal.cow;

public enum CowSoundVariants$SoundSet {
   CLASSIC("classic", "cow"),
   MOODY("moody", "cow_moody");

   private final String identifier;
   private final String soundEventIdentifier;

   private CowSoundVariants$SoundSet(final String identifier, final String soundEventIdentifier) {
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
