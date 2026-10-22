package net.minecraft.world.entity.animal.fox;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

public enum Fox$Variant implements StringRepresentable {
   RED(0, "red"),
   SNOW(1, "snow");

   public static final Fox$Variant DEFAULT = RED;
   public static final Codec<Fox$Variant> CODEC = StringRepresentable.fromEnum(Fox$Variant::values);
   private static final IntFunction<Fox$Variant> BY_ID = ByIdMap.continuous(Fox$Variant::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Fox$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Fox$Variant::getId);
   private final int id;
   private final String name;

   private Fox$Variant(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int getId() {
      return this.id;
   }

   public static Fox$Variant byId(final int id) {
      return BY_ID.apply(id);
   }

   public static Fox$Variant byBiome(final Holder<Biome> biome) {
      return biome.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED;
   }
}
