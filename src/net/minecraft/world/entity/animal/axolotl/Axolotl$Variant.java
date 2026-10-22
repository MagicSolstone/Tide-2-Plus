package net.minecraft.world.entity.animal.axolotl;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;

public enum Axolotl$Variant implements StringRepresentable {
   LUCY(0, "lucy", true),
   WILD(1, "wild", true),
   GOLD(2, "gold", true),
   CYAN(3, "cyan", true),
   BLUE(4, "blue", false);

   public static final Axolotl$Variant DEFAULT = LUCY;
   private static final IntFunction<Axolotl$Variant> BY_ID = ByIdMap.continuous(Axolotl$Variant::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, Axolotl$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Axolotl$Variant::getId);
   public static final Codec<Axolotl$Variant> CODEC = StringRepresentable.fromEnum(Axolotl$Variant::values);
   @Deprecated
   public static final Codec<Axolotl$Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Axolotl$Variant::getId);
   private final int id;
   private final String name;
   private final boolean common;

   private Axolotl$Variant(final int id, final String name, final boolean common) {
      this.id = id;
      this.name = name;
      this.common = common;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public static Axolotl$Variant byId(final int id) {
      return BY_ID.apply(id);
   }

   public static Axolotl$Variant getCommonSpawnVariant(final RandomSource random) {
      return getSpawnVariant(random, true);
   }

   public static Axolotl$Variant getRareSpawnVariant(final RandomSource random) {
      return getSpawnVariant(random, false);
   }

   private static Axolotl$Variant getSpawnVariant(final RandomSource random, final boolean common) {
      Axolotl$Variant[] validVariants = Arrays.stream(values()).filter(v -> v.common == common).toArray(Axolotl$Variant[]::new);
      return Util.getRandom(validVariants, random);
   }
}
