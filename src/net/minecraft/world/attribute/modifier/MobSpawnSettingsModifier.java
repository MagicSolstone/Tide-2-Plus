package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.level.biome.MobSpawnSettings;

public interface MobSpawnSettingsModifier extends AttributeModifier<MobSpawnSettings, MobSpawnSettings> {
   static MobSpawnSettingsModifier overlay() {
      return MobSpawnSettingsModifier$Overlay.INSTANCE;
   }

   @Override
   default Codec<MobSpawnSettings> argumentCodec(final EnvironmentAttribute<MobSpawnSettings> attribute) {
      return attribute.valueCodec();
   }

   @Override
   default LerpFunction<MobSpawnSettings> argumentKeyframeLerp(final EnvironmentAttribute<MobSpawnSettings> attribute) {
      return attribute.type().keyframeLerp();
   }
}
