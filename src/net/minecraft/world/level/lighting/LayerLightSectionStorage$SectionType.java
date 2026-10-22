package net.minecraft.world.level.lighting;

public enum LayerLightSectionStorage$SectionType {
   EMPTY("2"),
   LIGHT_ONLY("1"),
   LIGHT_AND_DATA("0");

   private final String display;

   private LayerLightSectionStorage$SectionType(final String display) {
      this.display = display;
   }

   public String display() {
      return this.display;
   }
}
