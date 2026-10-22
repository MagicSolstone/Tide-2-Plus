package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class TrialSpawnerConfigInRegistryFix extends NamedEntityFix {
   private static final Logger LOGGER = LogUtils.getLogger();

   public TrialSpawnerConfigInRegistryFix(final Schema outputSchema) {
      super(outputSchema, false, "TrialSpawnerConfigInRegistryFix", References.BLOCK_ENTITY, "minecraft:trial_spawner");
   }

   public Dynamic<?> fixTag(final Dynamic<Tag> input) {
      Optional<Dynamic<Tag>> normalConfig = input.get("normal_config").result();
      if (normalConfig.isEmpty()) {
         return input;
      } else {
         Optional<Dynamic<Tag>> ominousConfig = input.get("ominous_config").result();
         if (ominousConfig.isEmpty()) {
            return input;
         } else {
            Identifier registryLocation = TrialSpawnerConfigInRegistryFix$VanillaTrialChambers.CONFIGS_TO_KEY
               .get(Pair.of(normalConfig.get(), ominousConfig.get()));
            return registryLocation == null
               ? input
               : input.set("normal_config", input.createString(registryLocation.withSuffix("/normal").toString()))
                  .set("ominous_config", input.createString(registryLocation.withSuffix("/ominous").toString()));
         }
      }
   }

   @Override
   protected Typed<?> fix(final Typed<?> entity) {
      return entity.update(DSL.remainderFinder(), input -> {
         DynamicOps<?> inputType = input.getOps();
         Dynamic<?> result = this.fixTag(input.convert(NbtOps.INSTANCE));
         return result.convert(inputType);
      });
   }
}
