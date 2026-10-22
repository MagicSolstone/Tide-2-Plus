package net.minecraft.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record Dye(int defaultColor) implements ItemTintSource {
   public static final MapCodec<Dye> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(Dye::defaultColor)).apply(i, Dye::new)
   );

   @Override
   public int calculate(final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner) {
      return DyedItemColor.getOrDefault(itemStack, this.defaultColor);
   }

   @Override
   public MapCodec<Dye> type() {
      return MAP_CODEC;
   }
}
