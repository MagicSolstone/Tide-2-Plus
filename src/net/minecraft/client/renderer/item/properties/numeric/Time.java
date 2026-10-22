package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class Time extends NeedleDirectionHelper implements RangeSelectItemModelProperty {
   public static final MapCodec<Time> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Codec.BOOL.optionalFieldOf("wobble", true).forGetter(NeedleDirectionHelper::wobble),
            Time$TimeSource.CODEC.fieldOf("source").forGetter(o -> o.source)
         )
         .apply(i, Time::new)
   );
   private final Time$TimeSource source;
   private final RandomSource randomSource = RandomSource.create();
   private final NeedleDirectionHelper$Wobbler wobbler;

   public Time(final boolean wooble, final Time$TimeSource source) {
      super(wooble);
      this.source = source;
      this.wobbler = this.newWobbler(0.9F);
   }

   @Override
   protected float calculate(final ItemStack itemStack, final ClientLevel level, final int seed, final ItemOwner owner) {
      float targetRotation = this.source.get(level, itemStack, owner, this.randomSource);
      long gameTime = level.getGameTime();
      if (this.wobbler.shouldUpdate(gameTime)) {
         this.wobbler.update(gameTime, targetRotation);
      }

      return this.wobbler.rotation();
   }

   @Override
   public MapCodec<Time> type() {
      return MAP_CODEC;
   }
}
