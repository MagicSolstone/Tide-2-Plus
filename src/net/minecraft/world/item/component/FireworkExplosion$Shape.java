package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum FireworkExplosion$Shape implements StringRepresentable {
   SMALL_BALL(0, "small_ball"),
   LARGE_BALL(1, "large_ball"),
   STAR(2, "star"),
   CREEPER(3, "creeper"),
   BURST(4, "burst");

   private static final IntFunction<FireworkExplosion$Shape> BY_ID = ByIdMap.continuous(
      FireworkExplosion$Shape::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, FireworkExplosion$Shape> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, FireworkExplosion$Shape::getId);
   public static final Codec<FireworkExplosion$Shape> CODEC = StringRepresentable.fromValues(FireworkExplosion$Shape::values);
   private final int id;
   private final String name;

   private FireworkExplosion$Shape(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   public MutableComponent getName() {
      return Component.translatable("item.minecraft.firework_star.shape." + this.name);
   }

   public int getId() {
      return this.id;
   }

   public static FireworkExplosion$Shape byId(final int id) {
      return BY_ID.apply(id);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
