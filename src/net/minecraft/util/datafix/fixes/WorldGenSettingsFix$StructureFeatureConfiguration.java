package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

final class WorldGenSettingsFix$StructureFeatureConfiguration {
   public static final Codec<WorldGenSettingsFix$StructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.fieldOf("spacing").forGetter(c -> c.spacing),
            Codec.INT.fieldOf("separation").forGetter(c -> c.separation),
            Codec.INT.fieldOf("salt").forGetter(c -> c.salt)
         )
         .apply(i, WorldGenSettingsFix$StructureFeatureConfiguration::new)
   );
   private final int spacing;
   private final int separation;
   private final int salt;

   public WorldGenSettingsFix$StructureFeatureConfiguration(final int spacing, final int separation, final int salt) {
      this.spacing = spacing;
      this.separation = separation;
      this.salt = salt;
   }

   public <T> Dynamic<T> serialize(final DynamicOps<T> ops) {
      return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap()));
   }
}
