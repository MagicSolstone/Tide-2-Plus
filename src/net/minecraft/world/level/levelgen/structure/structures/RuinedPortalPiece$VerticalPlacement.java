package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum RuinedPortalPiece$VerticalPlacement implements StringRepresentable {
   ON_LAND_SURFACE("on_land_surface"),
   PARTLY_BURIED("partly_buried"),
   ON_OCEAN_FLOOR("on_ocean_floor"),
   IN_MOUNTAIN("in_mountain"),
   UNDERGROUND("underground"),
   IN_NETHER("in_nether");

   public static final Codec<RuinedPortalPiece$VerticalPlacement> CODEC = StringRepresentable.fromEnum(RuinedPortalPiece$VerticalPlacement::values);
   private final String name;

   private RuinedPortalPiece$VerticalPlacement(final String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
