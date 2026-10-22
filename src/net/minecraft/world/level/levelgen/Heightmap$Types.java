package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;

public enum Heightmap$Types implements StringRepresentable {
   WORLD_SURFACE_WG(0, "WORLD_SURFACE_WG", Heightmap$Usage.WORLDGEN, Heightmap.NOT_AIR),
   WORLD_SURFACE(1, "WORLD_SURFACE", Heightmap$Usage.CLIENT, Heightmap.NOT_AIR),
   OCEAN_FLOOR_WG(2, "OCEAN_FLOOR_WG", Heightmap$Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
   OCEAN_FLOOR(3, "OCEAN_FLOOR", Heightmap$Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
   MOTION_BLOCKING(4, "MOTION_BLOCKING", Heightmap$Usage.CLIENT, input -> input.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP) || !input.getFluidState().isEmpty()),
   MOTION_BLOCKING_NO_LEAVES(
      5,
      "MOTION_BLOCKING_NO_LEAVES",
      Heightmap$Usage.CLIENT,
      input -> input.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP_NO_LEAVES) || !input.getFluidState().isEmpty()
   );

   public static final Codec<Heightmap$Types> CODEC = StringRepresentable.fromEnum(Heightmap$Types::values);
   private static final IntFunction<Heightmap$Types> BY_ID = ByIdMap.continuous(t -> t.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Heightmap$Types> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, t -> t.id);
   private final int id;
   private final String serializationKey;
   private final Heightmap$Usage usage;
   private final Predicate<BlockState> isOpaque;

   private Heightmap$Types(final int id, final String serializationKey, final Heightmap$Usage usage, final Predicate<BlockState> isOpaque) {
      this.id = id;
      this.serializationKey = serializationKey;
      this.usage = usage;
      this.isOpaque = isOpaque;
   }

   public String getSerializationKey() {
      return this.serializationKey;
   }

   public boolean sendToClient() {
      return this.usage == Heightmap$Usage.CLIENT;
   }

   public boolean keepAfterWorldgen() {
      return this.usage != Heightmap$Usage.WORLDGEN;
   }

   public Predicate<BlockState> isOpaque() {
      return this.isOpaque;
   }

   @Override
   public String getSerializedName() {
      return this.serializationKey;
   }
}
