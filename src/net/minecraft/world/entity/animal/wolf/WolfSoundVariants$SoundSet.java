package net.minecraft.world.entity.animal.wolf;

public enum WolfSoundVariants$SoundSet {
   CLASSIC("classic", "wolf"),
   PUGLIN("puglin", "wolf_puglin"),
   SAD("sad", "wolf_sad"),
   ANGRY("angry", "wolf_angry"),
   GRUMPY("grumpy", "wolf_grumpy"),
   BIG("big", "wolf_big"),
   CUTE("cute", "wolf_cute");

   private final String identifier;
   private final String soundEventIdentifier;

   private WolfSoundVariants$SoundSet(final String identifier, final String soundEventIdentifier) {
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
