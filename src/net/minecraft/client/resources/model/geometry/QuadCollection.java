package net.minecraft.client.resources.model.geometry;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class QuadCollection {
   public static final QuadCollection EMPTY = new QuadCollection(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
   private static final int FLAGS_NOT_COMPUTED = -1;
   private final List<BakedQuad> all;
   private final List<BakedQuad> unculled;
   private final List<BakedQuad> north;
   private final List<BakedQuad> south;
   private final List<BakedQuad> east;
   private final List<BakedQuad> west;
   private final List<BakedQuad> up;
   private final List<BakedQuad> down;
   private int materialFlags = -1;

   private QuadCollection(
      final List<BakedQuad> all,
      final List<BakedQuad> unculled,
      final List<BakedQuad> north,
      final List<BakedQuad> south,
      final List<BakedQuad> east,
      final List<BakedQuad> west,
      final List<BakedQuad> up,
      final List<BakedQuad> down
   ) {
      this.all = all;
      this.unculled = unculled;
      this.north = north;
      this.south = south;
      this.east = east;
      this.west = west;
      this.up = up;
      this.down = down;
   }

   @BakedQuad$MaterialFlags
   private static int computeMaterialFlags(final List<BakedQuad> quads) {
      int flags = 0;

      for (BakedQuad quad : quads) {
         flags |= quad.materialInfo().flags();
      }

      return flags;
   }

   public List<BakedQuad> getQuads(@Nullable final Direction direction) {
      return switch (direction) {
         case null -> this.unculled;
         case NORTH -> this.north;
         case SOUTH -> this.south;
         case EAST -> this.east;
         case WEST -> this.west;
         case UP -> this.up;
         case DOWN -> this.down;
      };
   }

   public List<BakedQuad> getAll() {
      return this.all;
   }

   @BakedQuad$MaterialFlags
   public int materialFlags() {
      if (this.materialFlags == -1) {
         this.materialFlags = computeMaterialFlags(this.all);
      }

      return this.materialFlags;
   }

   public boolean hasMaterialFlag(@BakedQuad$MaterialFlags final int flag) {
      return (this.materialFlags() & flag) != 0;
   }
}
