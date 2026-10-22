package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum WeatheringCopper$WeatherState implements StringRepresentable {
   UNAFFECTED("unaffected"),
   EXPOSED("exposed"),
   WEATHERED("weathered"),
   OXIDIZED("oxidized");

   public static final IntFunction<WeatheringCopper$WeatherState> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap$OutOfBoundsStrategy.CLAMP);
   public static final Codec<WeatheringCopper$WeatherState> CODEC = StringRepresentable.fromEnum(WeatheringCopper$WeatherState::values);
   public static final StreamCodec<ByteBuf, WeatheringCopper$WeatherState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
   private final String name;

   private WeatheringCopper$WeatherState(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public static void forEach(final Consumer<WeatheringCopper$WeatherState> consumer) {
      for (WeatheringCopper$WeatherState weatherState : values()) {
         consumer.accept(weatherState);
      }
   }

   public WeatheringCopper$WeatherState next() {
      return BY_ID.apply(this.ordinal() + 1);
   }

   public WeatheringCopper$WeatherState previous() {
      return BY_ID.apply(this.ordinal() - 1);
   }
}
