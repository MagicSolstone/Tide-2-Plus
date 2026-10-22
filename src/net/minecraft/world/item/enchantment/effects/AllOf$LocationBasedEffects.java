package net.minecraft.world.item.enchantment.effects;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.phys.Vec3;

public record AllOf$LocationBasedEffects(List<EnchantmentLocationBasedEffect> effects) implements EnchantmentLocationBasedEffect {
   public static final MapCodec<AllOf$LocationBasedEffects> CODEC = AllOf.codec(
      EnchantmentLocationBasedEffect.CODEC, AllOf$LocationBasedEffects::new, AllOf$LocationBasedEffects::effects
   );

   @Override
   public void onChangedBlock(
      final ServerLevel serverLevel,
      final int enchantmentLevel,
      final EnchantedItemInUse item,
      final Entity entity,
      final Vec3 position,
      final boolean becameActive
   ) {
      for (EnchantmentLocationBasedEffect effect : this.effects) {
         effect.onChangedBlock(serverLevel, enchantmentLevel, item, entity, position, becameActive);
      }
   }

   @Override
   public void onDeactivated(final EnchantedItemInUse item, final Entity entity, final Vec3 position, final int level) {
      for (EnchantmentLocationBasedEffect effect : this.effects) {
         effect.onDeactivated(item, entity, position, level);
      }
   }

   @Override
   public MapCodec<AllOf$LocationBasedEffects> codec() {
      return CODEC;
   }
}
