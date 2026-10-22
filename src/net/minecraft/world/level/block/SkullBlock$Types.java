package net.minecraft.world.level.block;

public enum SkullBlock$Types implements SkullBlock$Type {
   SKELETON("skeleton"),
   WITHER_SKELETON("wither_skeleton"),
   PLAYER("player"),
   ZOMBIE("zombie"),
   CREEPER("creeper"),
   PIGLIN("piglin"),
   DRAGON("dragon");

   private final String name;

   private SkullBlock$Types(final String name) {
      this.name = name;
      TYPES.put(name, this);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
