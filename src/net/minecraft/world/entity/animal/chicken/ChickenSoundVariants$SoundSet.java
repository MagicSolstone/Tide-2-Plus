package net.minecraft.world.entity.animal.chicken;

public enum ChickenSoundVariants$SoundSet {
   CLASSIC("classic", "chicken"),
   PICKY("picky", "chicken_picky");

   private final String identifier;
   private final String soundEventIdentifier;

   private ChickenSoundVariants$SoundSet(final String identifier, final String soundEventIdentifier) {
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
