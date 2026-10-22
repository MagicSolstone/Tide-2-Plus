package net.minecraft.client.resources.model.geometry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collection;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class QuadCollection$Builder {
   private final Builder<BakedQuad> unculledFaces = ImmutableList.builder();
   private final Multimap<Direction, BakedQuad> culledFaces = ArrayListMultimap.create();

   public QuadCollection$Builder addCulledFace(final Direction direction, final BakedQuad quad) {
      this.culledFaces.put(direction, quad);
      return this;
   }

   public QuadCollection$Builder addUnculledFace(final BakedQuad quad) {
      this.unculledFaces.add(quad);
      return this;
   }

   public QuadCollection$Builder addAll(final QuadCollection quadCollection) {
      this.culledFaces.putAll(Direction.UP, quadCollection.up);
      this.culledFaces.putAll(Direction.DOWN, quadCollection.down);
      this.culledFaces.putAll(Direction.NORTH, quadCollection.north);
      this.culledFaces.putAll(Direction.SOUTH, quadCollection.south);
      this.culledFaces.putAll(Direction.EAST, quadCollection.east);
      this.culledFaces.putAll(Direction.WEST, quadCollection.west);
      this.unculledFaces.addAll(quadCollection.unculled);
      return this;
   }

   private static QuadCollection createFromSublists(
      final List<BakedQuad> all,
      final int unculledCount,
      final int northCount,
      final int southCount,
      final int eastCount,
      final int westCount,
      final int upCount,
      final int downCount
   ) {
      int index = 0;
      int var16;
      List<BakedQuad> unculled = all.subList(index, var16 = index + unculledCount);
      List<BakedQuad> north = all.subList(var16, index = var16 + northCount);
      int var18;
      List<BakedQuad> south = all.subList(index, var18 = index + southCount);
      List<BakedQuad> east = all.subList(var18, index = var18 + eastCount);
      int var20;
      List<BakedQuad> west = all.subList(index, var20 = index + westCount);
      List<BakedQuad> up = all.subList(var20, index = var20 + upCount);
      List<BakedQuad> down = all.subList(index, index + downCount);
      return new QuadCollection(all, unculled, north, south, east, west, up, down);
   }

   public QuadCollection build() {
      ImmutableList<BakedQuad> unculledFaces = this.unculledFaces.build();
      if (this.culledFaces.isEmpty()) {
         return unculledFaces.isEmpty()
            ? QuadCollection.EMPTY
            : new QuadCollection(unculledFaces, unculledFaces, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
      } else {
         Builder<BakedQuad> quads = ImmutableList.builder();
         quads.addAll(unculledFaces);
         Collection<BakedQuad> north = this.culledFaces.get(Direction.NORTH);
         quads.addAll(north);
         Collection<BakedQuad> south = this.culledFaces.get(Direction.SOUTH);
         quads.addAll(south);
         Collection<BakedQuad> east = this.culledFaces.get(Direction.EAST);
         quads.addAll(east);
         Collection<BakedQuad> west = this.culledFaces.get(Direction.WEST);
         quads.addAll(west);
         Collection<BakedQuad> up = this.culledFaces.get(Direction.UP);
         quads.addAll(up);
         Collection<BakedQuad> down = this.culledFaces.get(Direction.DOWN);
         quads.addAll(down);
         return createFromSublists(quads.build(), unculledFaces.size(), north.size(), south.size(), east.size(), west.size(), up.size(), down.size());
      }
   }
}
