package net.minecraft.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record Constant(int value) implements ItemTintSource {
   public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("value").forGetter(Constant::value)).apply(i, Constant::new)
   );

   public Constant(int value) {
      value = ARGB.opaque(value);
      this.value = value;
   }

   @Override
   public int calculate(final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner) {
      return this.value;
   }

   @Override
   public MapCodec<Constant> type() {
      return MAP_CODEC;
   }
}
