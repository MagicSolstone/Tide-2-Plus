package net.minecraft.world.item.equipment.trim;

import net.minecraft.resources.Identifier;

public enum TrimMaterials$Palette {
   QUARTZ("quartz"),
   IRON("iron"),
   IRON_DARKER("iron_darker"),
   NETHERITE("netherite"),
   NETHERITE_DARKER("netherite_darker"),
   REDSTONE("redstone"),
   COPPER("copper"),
   COPPER_DARKER("copper_darker"),
   GOLD("gold"),
   GOLD_DARKER("gold_darker"),
   EMERALD("emerald"),
   DIAMOND("diamond"),
   DIAMOND_DARKER("diamond_darker"),
   LAPIS("lapis"),
   AMETHYST("amethyst"),
   RESIN("resin");

   private final String suffix;
   private final Identifier id;

   private TrimMaterials$Palette(final String name) {
      this.suffix = name;
      this.id = Identifier.withDefaultNamespace("trim/" + name);
   }

   public String suffix() {
      return this.suffix;
   }

   public Identifier id() {
      return this.id;
   }
}
