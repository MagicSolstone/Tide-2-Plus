package net.minecraft.world.entity.animal.pig;

public enum PigSoundVariants$SoundSet {
   CLASSIC("classic", "pig"),
   MINI("mini", "pig_mini"),
   BIG("big", "pig_big");

   private final String identifier;
   private final String soundEventIdentifier;

   private PigSoundVariants$SoundSet(final String identifier, final String soundEventIdentifier) {
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
