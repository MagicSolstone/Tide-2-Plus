package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.phys.Vec3;

public record AllOf$EntityEffects(List<EnchantmentEntityEffect> effects) implements EnchantmentEntityEffect {
   public static final MapCodec<AllOf$EntityEffects> CODEC = AllOf.codec(EnchantmentEntityEffect.CODEC, AllOf$EntityEffects::new, AllOf$EntityEffects::effects);

   @Override
   public void apply(final ServerLevel serverLevel, final int enchantmentLevel, final EnchantedItemInUse item, final Entity entity, final Vec3 position) {
      for (EnchantmentEntityEffect effect : this.effects) {
         effect.apply(serverLevel, enchantmentLevel, item, entity, position);
      }
   }

   @Override
   public MapCodec<AllOf$EntityEffects> codec() {
      return CODEC;
   }
}
